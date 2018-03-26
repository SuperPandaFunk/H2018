package polytechnique.toursita.webService;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vincent on 2018-03-26.
 */

interface API {

    @FormUrlEncoded
    @POST("api/auth/registerFacebook")
    Call<BaseResponse> registerFacebook(@Field("token")String fb_token);

}
