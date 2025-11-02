package ir.sublearn.application.tools.mydb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import ir.sublearn.application.models.UserModel;

@Dao
public interface UserDao {

    @Insert
    Completable insert(UserModel user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(UserModel user);

    @Query("SELECT * FROM user_tb LIMIT 1")
    Single<UserModel> getUser();

    @Delete
    Completable delete(UserModel user);

}
