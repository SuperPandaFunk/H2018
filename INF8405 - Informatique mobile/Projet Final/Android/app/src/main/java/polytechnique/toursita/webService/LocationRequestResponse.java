package polytechnique.toursita.webService;

import com.google.gson.annotations.SerializedName;

public class LocationRequestResponse {
    @SerializedName("Comments")
    public Comment[] comments;

    @SerializedName("Images")
    public ImageData[] images;

    @SerializedName("_id")
    public String _id;

    @SerializedName("postedBy")
    public User postedBy;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lon")
    public double lon;

    @SerializedName("address")
    public String address;

    @SerializedName("description")
    public String description;
}
