package ir.sublearn.application.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ir.sublearn.application.tools.mydb.DateConverter;

@Entity(tableName = "user_tb")
@TypeConverters({DateConverter.class})
public class UserModel {
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private int userID;
    @ColumnInfo(name = "user_name")
    private String user_name;
    @ColumnInfo(name = "user_email")
    private String user_email;
    @ColumnInfo(name = "api_authorization_key")
    private String api_authorization_key;

    public UserModel(int userID, String user_name, String user_email, String api_authorization_key) {
        this.userID = userID;
        this.user_name = user_name;
        this.user_email = user_email;
        this.api_authorization_key = api_authorization_key;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getApi_authorization_key() {
        return api_authorization_key;
    }

    public void setApi_authorization_key(String api_authorization_key) {
        this.api_authorization_key = api_authorization_key;
    }
}