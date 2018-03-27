package polytechnique.toursita.webService;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vince on 2018-03-27.
 */

public class RegisterResponse {
    @SerializedName("_id")
    public String _id;

    @SerializedName("idFacebook")
    public String idFacebook;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;
}
