package polytechnique.toursita.webService;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    public String _id;

    @SerializedName("idFacebook")
    public String idFacebook;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("FirstName")
    public String FirstName;
}
