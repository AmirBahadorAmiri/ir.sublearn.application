package ir.sublearn.tools.mydb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import ir.sublearn.models.WordModel;

@Dao
public interface WordDao {

    @Insert
    Completable insert(WordModel word);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(WordModel word);

    @Query("SELECT * FROM word_tb WHERE english_word like '%' || :persian || '%' ORDER BY :persian ASC")
    Single<List<WordModel>> readEnglishWord(String persian);

    @Query("SELECT * FROM word_tb WHERE persian_word like '%' || :english || '%' ORDER BY :english DESC")
    Single<List<WordModel>> readPersianWord(String english);

    @Query("SELECT * FROM word_tb WHERE favorite = 1 ORDER BY favorite_time DESC")
    Single<List<WordModel>> readFavorites();

    @Query("SELECT * FROM word_tb WHERE view_time > 0 ORDER BY view_time DESC")
    Single<List<WordModel>> readHistories();

    @Query("update word_tb set view_time = 0 , view_time = 0 where view_time > 0")
    Completable clearHistories();

    @Query("update word_tb set view_time = 0 , view_time = 0 where id = :number")
    Completable clearFromHistories(int number);

    @Query("update word_tb set favorite = 0 where id = :number")
    Completable clearFromFavorites(int number);

    @Query("update word_tb set favorite = 0 where favorite = 1")
    Completable clearFavorites();

    @Query("select * from word_tb where id = :number")
    Single<WordModel> getWordById(int number);

}
