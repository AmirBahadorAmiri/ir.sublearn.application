package ir.sublearn.application.tools.user_manager;

import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.tools.iokhttp.IOkHttp;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserManager {

    public static void login(RequestBody formBody, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
//        RequestBody formBody = new FormBody.Builder()
//                .add("user_email", "bahador")
//                .add("user_password", "secure123")
//                .build();
        iOkHttp.post(IOkHttp.getLogin(), formBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseListener.onFailure(throwable);
            }
        });
    }

    public static void register(RequestBody formBody, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
        //        RequestBody formBody = new FormBody.Builder()
//                .add("user_name", "bahador")
//                .add("user_email", "bahador")
//                .add("user_password", "secure123")
//                .build();
        iOkHttp.post(IOkHttp.getRegister(), formBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseListener.onFailure(throwable);
            }
        });
    }

    public static void authorization_confirm(RequestBody formBody, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
        //        RequestBody formBody = new FormBody.Builder()
//                .add("user_email", "bahador")
//                .add("api_authorization_code", "secure123")
//                .build();
        iOkHttp.post(IOkHttp.getAuthorization_confirm(), formBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseListener.onFailure(throwable);
            }
        });
    }

    public static void change_password(RequestBody formBody, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
        //        RequestBody formBody = new FormBody.Builder()
//                .add("user_email", "bahador")
//                .add("user_password", "secure123")
//                .add("user_new_password", "secure123")
//                .add("api_authorization_key", "secure123")
//                .build();
        iOkHttp.post(IOkHttp.getChange_password(), formBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseListener.onFailure(throwable);
            }
        });
    }

    public static void forget_passwprd(RequestBody formBody, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
//        RequestBody formBody = new FormBody.Builder()
//                .add("user_email", "bahador")
//                .build();
        iOkHttp.post(IOkHttp.getForget_passwprd(), formBody, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseListener.onFailure(throwable);
            }
        });
    }

}
