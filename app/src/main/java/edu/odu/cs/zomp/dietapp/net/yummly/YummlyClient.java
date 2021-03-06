package edu.odu.cs.zomp.dietapp.net.yummly;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YummlyClient {

    public final static String API_ENDPOINT = "http://api.yummly.com/v1/api/";


    // Precursors
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        // Inject HTTP Log interceptor
        HttpLoggingInterceptor httpLogInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC);
        if (!httpClient.interceptors().contains(httpLogInterceptor))
            httpClient.addInterceptor(httpLogInterceptor);

        // Inject header authentication interceptor
        AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor();
        if (!httpClient.interceptors().contains(authInterceptor))
            httpClient.addInterceptor(authInterceptor);


        retrofitBuilder.client(httpClient.build());
        Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(serviceClass);
    }
}