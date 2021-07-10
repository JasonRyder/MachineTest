package mobulous.kavita.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobulous.kavita.R;

import java.util.ArrayList;
import java.util.List;

import mobulous.kavita.adapter.RestaurantAdapter;
import mobulous.kavita.database.AppDatabase;
import mobulous.kavita.database.AppExecutors;
import mobulous.kavita.model.Restaurant;
import mobulous.kavita.model.RestaurantEntity;
import mobulous.kavita.model.Root;
import mobulous.kavita.network.Retrofit;
import mobulous.kavita.network.Url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RestaurantAdapter.BookMarkClickListener {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private List<Restaurant> restaurantList;
    private RestaurantAdapter restaurantAdapter;
    private EditText searchRestaurant;
    private TextView tvNoResult;
    private ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        searchRestaurant = findViewById(R.id.search_res);
        tvNoResult = findViewById(R.id.tv_no_result);
        progressCircular = findViewById(R.id.progress_circular);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(this, restaurantList, this);
        db = AppDatabase.getInstance(this);
        searchRestaurant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = textView.getText().toString().trim();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();
                    progressCircular.setVisibility(View.VISIBLE);
                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                    if (view == null) {
                        view = new View(MainActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (TextUtils.isEmpty(searchText)) {
                        Toast.makeText(MainActivity.this, "Enter a text to search", Toast.LENGTH_SHORT).show();
                    } else {
                        searchRestaurantCall(searchText);
                        tvNoResult.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, BookMarkActiviy.class));
        return super.onOptionsItemSelected(item);
    }

    private void searchRestaurantCall(String searchtext) {

        Retrofit retrofit = new Retrofit();
        Call<Root> call = retrofit.api.getAllRestaurants(searchtext, Url.USER_KEY_VALUE);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                progressCircular.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoResult.setVisibility(View.GONE);
                    restaurantList = response.body().restaurants;
                    recyclerView.setAdapter(restaurantAdapter);
                    restaurantAdapter.getAllRestaurants(response.body().restaurants);
                    Log.d("main", "onResponse: " + response.body());
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvNoResult.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressCircular.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void bookmarkClickListener(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setAddress(restaurant.restaurant.location.address);
        restaurantEntity.setAverage_cost_for_two(restaurant.restaurant.average_cost_for_two);
        restaurantEntity.setId(restaurant.restaurant.id);
        restaurantEntity.setRating(restaurant.restaurant.user_rating.aggregate_rating);
        restaurantEntity.setRatingColor(restaurant.restaurant.user_rating.rating_color);
        //restaurantEntity.setImage();
        restaurantEntity.setName(restaurant.restaurant.name);
        restaurantEntity.setCuisines(restaurant.restaurant.cuisines);
        restaurantEntity.setCurrency(restaurant.restaurant.currency);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.restaurantDao().insert(restaurantEntity);
            }
        });
        Toast.makeText(this, "Restaurant bookmarked successfully.", Toast.LENGTH_SHORT).show();
    }
}
