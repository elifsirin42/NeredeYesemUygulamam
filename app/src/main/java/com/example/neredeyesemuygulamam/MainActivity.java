package com.example.neredeyesemuygulamam;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    //Parmak izi okumasını yapan ve uygulama ilk açıkdığında ekranda olan sınıf.

    private String KEY_NAME="somekeyname";
    private Button button;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Başla Butonuna basıldığı zaman kullanıcıya parmak izi talimatı vermesi için onClick metodu oluşturuldu.
        button=findViewById(R.id.baslabutton);
        textView=findViewById(R.id.aciklama);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Başlamak için Parmak izinizi sensöre okutmalısınız!");
            }
        });

        //Parmak okuma FingerprintManager kullanılarak kodlar oluşturuldu.
        KeyguardManager keyguardManager=(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager=(FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if(!fingerprintManager.isHardwareDetected()){
            Log.e("Hardware","Parmak izi donanımı algılanmadı");
            return;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
        != PackageManager.PERMISSION_GRANTED){
            Log.e("Permission","Parmak izi izni algılanmadı");
            return;
        }
        if(!keyguardManager.isKeyguardSecure()){
            Log.e("Keyquard","Tuş kilidine ulaşılamadı");
            return;
        }
        KeyStore keyStore;
        try {
            keyStore=KeyStore.getInstance("AndroidKeyStore");
        }catch (Exception e){
            Log.e("KeyStore",e.getMessage());
            return;
        }
        KeyGenerator keyGenerator;
        try{
            keyGenerator=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        }catch (Exception e){
            Log.e("keyGenerator",e.getMessage());
            return;
        }
        try{
            keyStore.load(null);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_NAME,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setUserAuthenticationRequired(true)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build());

            keyGenerator.generateKey();
        }catch (Exception e){
            Log.e("Generating keys",e.getMessage());
        }

        Cipher cipher;
        try{
            cipher=Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
            +"/"+KeyProperties.BLOCK_MODE_CBC
            +"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }catch (Exception e){
            Log.e("Cipher",e.getMessage());
            return;
        }
        try{
            keyStore.load(null);
            SecretKey key=(SecretKey) keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE,key);
        }catch (Exception e){
            Log.e("Secret key",e.getMessage());
            return;
        }

        FingerprintManager.CryptoObject cryptoObject=new FingerprintManager.CryptoObject(cipher);

        CancellationSignal cancellationSignal=new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,new AuthenticationHandler(this),null);
    }
}

