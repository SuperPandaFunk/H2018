package polytechnique.toursita.webService;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vincent on 2018-03-26.
 */

public class WebService {
    public static final String BASE_URL = "https://api-tourista.herokuapp.com/";

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

    public Call<LocationCreationResponse> createLocation(double lat, double lon, String address, String description, String postedBy ){
        return mService.createLocation(lat, lon, address, description, postedBy);
    }

    public Call<LocationRequestResponse[]> getNearLocations(int distance, LatLng pos){
        return mService.getNearLocations(distance, pos.latitude, pos.longitude);
    }

    public Call<LocationRequestResponse> getLocationId(String id){
        return mService.getLocationId(id);
    }

    public Call<AddCommentResponse> addComment(String placeId, String posterId, String text ){
        return mService.addComment(placeId, posterId, text);
    }

    public Call<ImageUploadResult> addImage(String id, MultipartBody.Part img){
        return mService.addImage(id, img);
    }

}
