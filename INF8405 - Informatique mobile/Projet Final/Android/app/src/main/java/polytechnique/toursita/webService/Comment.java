package polytechnique.toursita.webService;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("text")
    public String text;

    @SerializedName("_id")
    public String _id;

    @SerializedName("postedBy")
    public User postedBy;
}
