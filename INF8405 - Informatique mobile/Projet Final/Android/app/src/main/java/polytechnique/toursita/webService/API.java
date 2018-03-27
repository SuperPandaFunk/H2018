package polytechnique.toursita.webService;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Vincent on 2018-03-26.
 */

interface API {

    @FormUrlEncoded
    @POST("api/users")
    Call<RegisterResponse> registerFacebook(@Field("idFacebook")String fb_token, @Field("FirstName")String firstName, @Field("LastName")String lastName);

    @GET(WebService.BASE_URL + "api/users/fb/{fbid}")
    Call<RegisterResponse> getUser(@Path("fbid")String fbId);

}
