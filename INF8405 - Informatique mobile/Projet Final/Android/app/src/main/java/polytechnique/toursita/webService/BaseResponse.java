package polytechnique.toursita.webService;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vincent on 2018-03-26.
 */

public class BaseResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public String data;

}
