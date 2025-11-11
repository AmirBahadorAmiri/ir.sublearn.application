package ir.sublearn.tools.downloader;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ir.sublearn.listener.ResponseListener;
import ir.sublearn.tools.iokhttp.IOkHttp;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class Downloader {

    private Context icontext;
    private IOkHttp iOkHttp;

    public Downloader inject(Context context) {
        if (this.icontext == null)
            this.icontext = context;
        if (this.iOkHttp == null)
            this.iOkHttp = new IOkHttp();
        return this;
    }

    public Uri getMusicFileUri(String fileName) {
        File musicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), fileName);
        if (musicFile.exists()) {
            return Uri.fromFile(musicFile);
        } else {
            return null;
        }
    }

    public void downloadFile(String url, String file_name, ResponseListener responseListener) {
        if (isFileExists(file_name)) {
            responseListener.onSuccess(null);
        } else {
            HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(url))
                    .newBuilder()
                    .build();
            iOkHttp.get(httpUrl, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    saveFiles(response.body().byteStream(), file_name)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@NonNull Boolean aBoolean) {
                                    if (aBoolean)
                                        responseListener.onSuccess(null);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    responseListener.onFailure(e);
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    responseListener.onFailure(throwable);
                }
            });
        }
    }

    public void downloadMusic(String music_url, String music_file_name, ResponseListener responseListener) {
        if (isMusicExists(music_file_name)) {
            responseListener.onSuccess(null);
        } else {
            HttpUrl httpUrl = HttpUrl.parse(music_url)
                    .newBuilder()
                    .build();
            iOkHttp.get(httpUrl, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    saveMusicFile(response.body().byteStream(), music_file_name)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                }

                                @Override
                                public void onNext(@NonNull Boolean aBoolean) {
                                    if (aBoolean) {
                                        responseListener.onSuccess(null);
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    responseListener.onFailure(e);
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    responseListener.onFailure(throwable);
                }
            });
        }
    }

    public void downloadLyric(String lyric_url, String lyric_file_name, String artistName, ResponseListener responseListener) {
        if (isLyricExists(lyric_file_name, artistName)) {
            responseListener.onSuccess(null);
        } else {
            HttpUrl httpUrl = HttpUrl.parse(lyric_url)
                    .newBuilder()
                    .build();
            iOkHttp.get(httpUrl, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    saveLrcFile(response.body().byteStream(), lyric_file_name, artistName)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                }

                                @Override
                                public void onNext(@NonNull Boolean aBoolean) {
                                    if (aBoolean) {
                                        responseListener.onSuccess(null);
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    responseListener.onFailure(e);
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    responseListener.onFailure(throwable);
                }
            });
        }
    }

    public Observable<Boolean> saveMusicFile(InputStream inputStream, String fileName) {
        return Observable.fromCallable(() -> saveMusic(inputStream, fileName))
                .subscribeOn(Schedulers.io());
    }

    public boolean saveMusic(InputStream inputStream, String fileName) {
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }
        File file = new File(musicDir, fileName);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Observable<Boolean> saveLrcFile(InputStream inputStream, String fileName, String artistName) {
        return Observable.fromCallable(() -> saveLyric(inputStream, fileName, artistName))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> saveFiles(InputStream inputStream, String fileName) {
        return Observable.fromCallable(() -> saveFile(inputStream, fileName))
                .subscribeOn(Schedulers.io());
    }

    public boolean saveFile(InputStream inputStream, String fileName) {
        File musicDir = new File(getContext().getExternalFilesDir(null), "json");
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }
        File file = new File(musicDir, fileName);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveLyric(InputStream inputStream, String fileName, String artistName) {
        File musicDir = new File(getContext().getExternalFilesDir(null) + "/lyrics/", artistName);
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }
        File file = new File(musicDir, fileName);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isFileExists(String fileName) {
        File musicFile = new File(getContext().getExternalFilesDir(null) + "/json", fileName);
        return musicFile.exists();
    }

    public boolean isMusicExists(String fileName) {
        File musicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), fileName);
        return musicFile.exists();
    }

    public boolean isLyricExists(String fileName, String artistName) {
        File musicFile = new File(getContext().getExternalFilesDir(null) + "/lyrics/" + artistName, fileName);
        return musicFile.exists();
    }

    public Context getContext() {
        return icontext;
    }
}
