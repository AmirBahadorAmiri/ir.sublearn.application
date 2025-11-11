package ir.sublearn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ir.sublearn.tools.mydb.DateConverter;

@Entity(tableName = "word_tb")
@TypeConverters({DateConverter.class})
public class WordModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "persian_word")
    private String persianWord;

    @ColumnInfo(name = "english_word")
    private String englishWord;

    @ColumnInfo(name = "favorite")
    private Boolean favorite = false;

    @ColumnInfo(name = "view_time")
    private long view_time;

    @ColumnInfo(name = "favorite_time")
    private long favorite_time;

    public WordModel(String persianWord, String englishWord) {
        this.persianWord = persianWord;
        this.englishWord = englishWord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersianWord() {
        return persianWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public void setPersianWord(String persianWord) {
        this.persianWord = persianWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public long getView_time() {
        return view_time;
    }

    public void setView_time(long view_time) {
        this.view_time = view_time;
    }

    public long getFavorite_time() {
        return favorite_time;
    }

    public void setFavorite_time(long favorite_time) {
        this.favorite_time = favorite_time;
    }

}
