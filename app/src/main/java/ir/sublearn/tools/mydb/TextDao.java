package ir.sublearn.tools.mydb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import ir.sublearn.models.TextModel;

@Dao
public interface TextDao {

    @Insert
    Completable insert(TextModel text);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(TextModel text);

    @Delete
    Completable delete(TextModel text);

    @Query("SELECT * FROM text_tb order by translation_time DESC")
    Single<List<TextModel>> readAllText();

    @Query("DELETE FROM text_tb")
    Completable deleteAllText();

}
