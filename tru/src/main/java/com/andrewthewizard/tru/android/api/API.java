package com.andrewthewizard.tru.android.api;

import com.andrewthewizard.tru.android.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;


public class API {

    private APIService api;
    private static String API_URL = "https://tru.andrewthewizard.com/api/";

    public API() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .build();

        APIService api = retrofit.create(APIService.class);
    }

    public interface APIService {




    }

}
