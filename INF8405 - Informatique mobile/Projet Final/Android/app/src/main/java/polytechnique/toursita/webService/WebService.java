package polytechnique.toursita.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vincent on 2018-03-26.
 */

public class WebService {
    public static final String BASE_URL = "http://10.200.0.232:3000/";

    private final API mService;

    public WebService(){
        Gson gson = new GsonBuilder()
                .create();

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(5,TimeUnit.SECONDS);

        OkHttpClient httpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        mService = retrofit.create(API.class);
    }

    public Call<RegisterResponse> registerFacebook(String token, String firstName, String lastName) {
        return mService.registerFacebook(token, firstName, lastName);
    }

    public Call<RegisterResponse> getUser(String facebookId){
        return mService.getUser(facebookId);
    }

}
