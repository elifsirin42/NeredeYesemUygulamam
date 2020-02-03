package com.example.neredeyesemuygulamam;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.widget.Toast;

public class AuthenticationHandler extends FingerprintManager.AuthenticationCallback {


    private MainActivity mainActivity;

    public AuthenticationHandler(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        Toast.makeText(mainActivity,"Auth Error"+errString,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        Toast.makeText(mainActivity,"Auth Help: "+helpString,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Toast.makeText(mainActivity,"Auth Succeeded",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(mainActivity, UserLocationActivity.class);
        mainActivity.startActivity(intent);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(mainActivity,"Auth Failed",Toast.LENGTH_LONG).show();
    }
}
