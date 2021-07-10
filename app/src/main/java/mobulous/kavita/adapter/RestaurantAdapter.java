package mobulous.kavita.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.mobulous.kavita.R;

import java.util.List;

import mobulous.kavita.model.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;
    BookMarkClickListener markClickListener;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList, BookMarkClickListener markClickListener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.markClickListener = markClickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_roe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        final Restaurant restaurant = restaurantList.get(position);
        holder.rating.setText(restaurant.restaurant.user_rating.aggregate_rating);
        holder.name.setText(restaurant.restaurant.name);
        holder.cuisines.setText(restaurant.restaurant.cuisines);
        holder.costForTwo.setText(String.format("%s%d%s", restaurant.restaurant.currency, restaurant.restaurant.average_cost_for_two, " for two"));
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 50)
                .build();
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        shapeDrawable.setFillColor(ColorStateList.valueOf(Color.parseColor("#" + restaurant.restaurant.user_rating.rating_color)));
        ViewCompat.setBackground(holder.rating, shapeDrawable);
//        Glide.with(context)
//                .load(restaurant.restaurant.photos_url)
//                .placeholder(R.drawable.zomato)
//                .into(holder.image);
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markClickListener.bookmarkClickListener(restaurant);
            }
        });

    }

    public void getAllRestaurants(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public TextView cuisines, name, rating, costForTwo;
        public ImageView image, bookmark;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            cuisines = itemView.findViewById(R.id.cuisines);
            rating = itemView.findViewById(R.id.rating);
            bookmark = itemView.findViewById(R.id.iv_bookmark);
            image = itemView.findViewById(R.id.image);
            costForTwo = itemView.findViewById(R.id.costForTwo);
        }
    }

    public interface BookMarkClickListener {
        public void bookmarkClickListener(Restaurant restaurant);
    }
}
