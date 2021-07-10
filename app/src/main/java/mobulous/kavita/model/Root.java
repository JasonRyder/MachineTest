package mobulous.kavita.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Root {
    @SerializedName("results_found")
    public int results_found;
    @SerializedName("results_start")
    public int results_start;
    @SerializedName("results_shown")
    public int results_shown;
    @SerializedName("restaurants")
    public List<Restaurant> restaurants;
}
