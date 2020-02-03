package com.example.neredeyesemuygulamam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neredeyesemuygulamam.RetrofitModel.Restaurant;
import com.example.neredeyesemuygulamam.RetrofitModel.Restaurant_;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    //Tıklanan Restoranın Detaylı olarak açıklanmalarının çekildiği sınıf.

    ImageView imageView;
    TextView resName;
    TextView address;
    List<Restaurant_> dataList=new ArrayList<>();
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        resName=findViewById(R.id.restaurantName);
        address=findViewById(R.id.address);
        imageView=findViewById(R.id.restaurantPhoto);

        //Bundle yardımıyla tıklanan restoranın poziyon bilgisi alındı.
        Intent i=getIntent();
        Bundle bundle = i.getExtras();
        position=(int)bundle.getSerializable("position");


        for (int k=0;k<5;k++){
            String str="dataList"+k;
            dataList.add((Restaurant_) bundle.getSerializable(str));

        }
        try {
            Picasso.get().load(dataList.get(position).getThumb()).into(imageView);
        }catch (Exception e){

        }

        resName.setText(dataList.get(position).getName());
        address.setText(dataList.get(position).toString());


    }
}
