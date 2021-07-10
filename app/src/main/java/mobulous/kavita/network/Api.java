package mobulous.kavita.network;

import mobulous.kavita.model.Root;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface Api {
    @GET("api/v2.1/search")
    Call<Root> getAllRestaurants(@Query("q") String queryText,
                                 @Header(Url.USER_KEY) String userKey);
}
