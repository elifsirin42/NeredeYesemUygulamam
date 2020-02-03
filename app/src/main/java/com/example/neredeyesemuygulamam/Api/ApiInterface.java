package com.example.neredeyesemuygulamam.Api;

import com.example.neredeyesemuygulamam.RetrofitModel.Search;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {


    //use key to use zomato api
    @Headers({
            "Accept: application/json","user-key: 414112f9c553f1172e9306bac10930ab"
    })//take restaurant information from zomato api with location parameters
    @GET("search")
    Observable<Search> getRestaurantsBySearch(@Query("lat") Double latitude,
                                               @Query("lon") Double longtitude,
                                               @Query("sort") String string,
                                               @Query("count") int count);

}