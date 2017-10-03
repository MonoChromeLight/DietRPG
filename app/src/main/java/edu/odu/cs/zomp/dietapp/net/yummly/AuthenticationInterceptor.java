package edu.odu.cs.zomp.dietapp.net.yummly;

import java.io.IOException;

import edu.odu.cs.zomp.dietapp.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String apiKey;
    private String apiSecret;

    public AuthenticationInterceptor() { }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .addHeader("X-Yummly-App-ID", BuildConfig.YUMMLY_APP_KEY)
                .addHeader("X-Yummly-App-Key", BuildConfig.YUMMLY_APP_SECRET)
                .build();

        return chain.proceed(request);
    }
}