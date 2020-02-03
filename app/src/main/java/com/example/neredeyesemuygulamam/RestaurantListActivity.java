package com.example.neredeyesemuygulamam;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.neredeyesemuygulamam.Api.ApiClient;
import com.example.neredeyesemuygulamam.Api.ApiInterface;
import com.example.neredeyesemuygulamam.RetrofitModel.Restaurant_;
import com.example.neredeyesemuygulamam.RetrofitModel.Search;

import java.io.Serializable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class RestaurantListActivity extends AppCompatActivity {

    //Konum bilgilerini ve restoran bilgilerini çekerek yakındaki 5 Restoranı alıp listeleyen sınıf.

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ListView listView;
    ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        //UserLocationActivty'den enlem ve boylam değerlerinin çekilmesi sağlandı.
        Intent intent = getIntent();
        double lat = Double.parseDouble(intent.getStringExtra("lat"));
        double longi = Double.parseDouble(intent.getStringExtra("longi"));

        listView = (ListView) findViewById(R.id.listViewlist);

        //Retrofit oluşturduk.
        Retrofit retrofit = ApiClient.getClient();
        api = retrofit.create(ApiInterface.class);

        compositeDisposable.add(api.getRestaurantsBySearch(lat, longi, "real_distance", 5).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Search>() {
                            @Override
                            public void accept(Search example) throws Exception {
                                getData(example);

                            }
                        })

        );

    }

    public void getData(Search example) {

        String[] nameList = new String[5];
        final Bundle bundle = new Bundle();

        //Yakında bulunan 5 tane restaurant bilgilerinin çekilmesi sağlandı.
        // Ayrıca Bundle yardımıyla sayfalar arası bilgi çekimi sağlandı.
        for (int i = 0; i < 5; i++) {

            Restaurant_ restaurant = example.restaurants.get(i).getRestaurant();
            bundle.putSerializable("dataList" + i, (Serializable) restaurant);
            nameList[i] = example.restaurants.get(i).getRestaurant().getName();
        }
        //Restauranların listelenmesi sağlandı.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(RestaurantListActivity.this, RestaurantDetailActivity.class);
                bundle.putSerializable("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}


