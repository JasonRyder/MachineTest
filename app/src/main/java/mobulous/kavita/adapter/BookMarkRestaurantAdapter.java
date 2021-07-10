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

import mobulous.kavita.model.RestaurantEntity;

public class BookMarkRestaurantAdapter extends RecyclerView.Adapter<BookMarkRestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private List<RestaurantEntity> restaurantList;

    public BookMarkRestaurantAdapter(Context context, List<RestaurantEntity> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_roe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        final RestaurantEntity restaurant = restaurantList.get(position);
        holder.rating.setText(restaurant.getRating());
        holder.name.setText(restaurant.getName());
        holder.cuisines.setText(restaurant.getCuisines());
        holder.costForTwo.setText(String.format("%s%d%s", restaurant.getCurrency(), restaurant.getAverage_cost_for_two(), " for two"));
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 50)
                .build();
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        shapeDrawable.setFillColor(ColorStateList.valueOf(Color.parseColor("#" + restaurant.getRatingColor())));
        ViewCompat.setBackground(holder.rating, shapeDrawable);
//        Glide.with(context)
//                .load(restaurant.restaurant.photos_url)
//                .placeholder(R.drawable.zomato)
//                .into(holder.image);
        holder.bookmark.setVisibility(View.GONE);

    }

    public void setAllRestaurants(List<RestaurantEntity> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList == null ? 0 : restaurantList.size();
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

}
