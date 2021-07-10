package mobulous.kavita.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobulous.kavita.R;

import java.util.List;

import mobulous.kavita.adapter.BookMarkRestaurantAdapter;
import mobulous.kavita.database.AppDatabase;
import mobulous.kavita.database.AppExecutors;
import mobulous.kavita.model.RestaurantEntity;

public class BookMarkActiviy extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView allRestaurantsNoresult;
    private List<RestaurantEntity> restaurantList;
    private BookMarkRestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_activiy);
        recyclerView = findViewById(R.id.bookMarkRestaurantList);
        allRestaurantsNoresult = findViewById(R.id.tv_no_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantAdapter = new BookMarkRestaurantAdapter(this, restaurantList);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<RestaurantEntity> allRestaurants = AppDatabase.getInstance(BookMarkActiviy.this).restaurantDao().getAllRestaurants();
                if (allRestaurants == null || allRestaurants.isEmpty()) {
                    allRestaurantsNoresult.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    return;

                }
                allRestaurantsNoresult.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                restaurantList = allRestaurants;
                recyclerView.setAdapter(restaurantAdapter);
                restaurantAdapter.setAllRestaurants(allRestaurants);
                restaurantAdapter.notifyDataSetChanged();

            }
        });
    }
}