package polytechnique.toursita.webService;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Vincent on 2018-03-26.
 */

interface API {

    @FormUrlEncoded
    @POST("api/users")
    Call<RegisterResponse> registerFacebook(@Field("idFacebook")String fb_token, @Field("FirstName")String firstName, @Field("LastName")String lastName);

    @FormUrlEncoded
    @POST("api/locations")
    Call<LocationCreationResponse> createLocation(@Field("lat")double lat, @Field("lon")double lon, @Field("address")String address, @Field("description")String description, @Field("postedBy")String postedBy);

    @FormUrlEncoded
    @POST("api/locations/comment/{id}")
    Call<AddCommentResponse> addComment(@Path("id")String id, @Field("postedBy")String postedBy, @Field("text")String text);

    @Multipart
    @POST("api/locations/image/{id}")
    Call<ImageUploadResult> addImage(@Path("id")String id , @Part MultipartBody.Part img);

    @GET(WebService.BASE_URL + "api/users/fb/{fbid}")
    Call<RegisterResponse> getUser(@Path("fbid")String fbId);

    @GET(WebService.BASE_URL + "api/locations/near/{distance}/{lat}/{lon}")
    Call<LocationRequestResponse[]> getNearLocations(@Path("distance")int distance, @Path("lat")double lat, @Path("lon")double lon);

    @GET(WebService.BASE_URL + "api/locations/{id}")
    Call<LocationRequestResponse> getLocationId(@Path("id")String id);

}
