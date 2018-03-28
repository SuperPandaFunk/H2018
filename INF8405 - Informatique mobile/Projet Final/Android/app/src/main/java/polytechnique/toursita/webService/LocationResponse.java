package polytechnique.toursita.webService;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {

    @SerializedName("_id")
    public String _id;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lon")
    public String lon;

    @SerializedName("address")
    public String address;

    @SerializedName("description")
    public String description;

    @SerializedName("postedBy")
    public String postedBy;

    @SerializedName("Comments")
    public String[] comments;

    @SerializedName("Images")
    public Bitmap[] images;
}
