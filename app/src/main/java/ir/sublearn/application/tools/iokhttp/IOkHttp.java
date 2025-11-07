package ir.sublearn.application.tools.iokhttp;

import java.io.IOException;

import ir.sublearn.application.listener.ResponseListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IOkHttp {

    private OkHttpClient client;
    public static final String mainhost_url = "http://10.27.78.126";
//    public static final String mainhost_url = "https://amirbahadoramiri.ir";

    private static String login;
    private static String register;
    private static String authorization_confirm;
    private static String change_password;
    private static String forget_passwprd;
    private static String get_seasons;
    private static String get_episode;
    private static String translate;
    private static String get_all_movies;
    private static String get_series;
    private static String get_all_songs;
    private static String get_top_movies;
    private static String get_top_series;

    public IOkHttp post(String url, RequestBody body, ResponseListener responseListener) {
        if (client == null)
            client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    responseListener.onSuccess(response);
                } else {
                    responseListener.onFailure(new Throwable("error code " + response.code()));
                }
            }
        });
        return this;
    }

    public IOkHttp get(HttpUrl url, ResponseListener responseListener) {
        if (client == null)
            client = new OkHttpClient();
//        HttpUrl httpUrl = HttpUrl.parse("https://example.com/api")
//                .newBuilder()
//                .addQueryParameter("name", "Bahador")
//                .addQueryParameter("age", "30")
//                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    responseListener.onSuccess(response);
                } else {
                    responseListener.onFailure(new Throwable("error code " + response.code()));
                }
            }
        });
        return this;
    }

    public static String getLogin() {
        return mainhost_url+login;
    }

    public static void setLogin(String login) {
        IOkHttp.login = login;
    }

    public static String getRegister() {
        return mainhost_url+register;
    }

    public static void setRegister(String register) {
        IOkHttp.register = register;
    }

    public static String getAuthorization_confirm() {
        return mainhost_url+authorization_confirm;
    }

    public static void setAuthorization_confirm(String authorization_confirm) {
        IOkHttp.authorization_confirm = authorization_confirm;
    }

    public static String getChange_password() {
        return mainhost_url+change_password;
    }

    public static void setChange_password(String change_password) {
        IOkHttp.change_password = change_password;
    }

    public static String getForget_passwprd() {
        return mainhost_url+forget_passwprd;
    }

    public static void setForget_passwprd(String forget_passwprd) {
        IOkHttp.forget_passwprd = forget_passwprd;
    }

    public static String getGet_seasons() {
        return mainhost_url+get_seasons;
    }

    public static void setGet_seasons(String get_seasons) {
        IOkHttp.get_seasons = get_seasons;
    }

    public static String getGet_episode() {
        return mainhost_url+get_episode;
    }

    public static void setGet_episode(String get_episode) {
        IOkHttp.get_episode = get_episode;
    }

    public static String getTranslate() {
        return translate;
    }

    public static void setTranslate(String translate) {
        IOkHttp.translate = translate;
    }

    public static String getGet_all_movies() {
        return mainhost_url+get_all_movies;
    }

    public static void setGet_all_movies(String get_all_movies) {
        IOkHttp.get_all_movies = get_all_movies;
    }

    public static String getGet_series() {
        return mainhost_url+get_series;
    }

    public static void setGet_series(String get_series) {
        IOkHttp.get_series = get_series;
    }

    public static String getGet_all_songs() {
        return mainhost_url+get_all_songs;
    }

    public static void setGet_all_songs(String get_all_songs) {
        IOkHttp.get_all_songs = get_all_songs;
    }

    public static String getGet_top_movies() {
        return mainhost_url+get_top_movies;
    }

    public static void setGet_top_movies(String get_top_movies) {
        IOkHttp.get_top_movies = get_top_movies;
    }

    public static String getGet_top_series() {
        return mainhost_url+get_top_series;
    }

    public static void setGet_top_series(String get_top_series) {
        IOkHttp.get_top_series = get_top_series;
    }
}
