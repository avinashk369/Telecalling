package com.techcamino.telecalling.rest;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thinkpad on 12-10-2016.
 */

public class APIClient {

    /**
     * URL - http://192.168.43.193/API/web/index.php/
     * URL - http://telecallingserver.globalgymsoftware.com/api/web/index.php/user/getInterest
     * where service is controller and sodsr000000000000000 is query parameter

     */
    public static final String BASE_URL = "http://192.168.0.107/api/web/index.php/";
    private static Retrofit retrofit = null;




    public static Retrofit getClient() {
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept","application/json")
//                      .addHeader("timestamp", DateUtil.getCurrentTimeStamp())
//                      .addHeader("hash", generateHash())
//                      .addHeader("api_key",apiKey)
//                    //.addHeader("authtoken","authtoken")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.connectTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();


        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
