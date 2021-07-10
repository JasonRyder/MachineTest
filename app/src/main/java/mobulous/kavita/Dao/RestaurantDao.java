package mobulous.kavita.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import mobulous.kavita.model.RestaurantEntity;

@Dao
public interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RestaurantEntity actorList);

    @Query("SELECT * FROM restaurant")
    List<RestaurantEntity> getAllRestaurants();

    @Query("DELETE FROM restaurant")
    void deleteAll();
}
