package ir.sublearn.application.tools.version;

import ir.sublearn.application.listener.ResponseListener;
import ir.sublearn.application.tools.iokhttp.IOkHttp;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppVersionChecker {

    public static void app_version_checker(RequestBody formBody, ResponseListener responseListener) {
        IOkHttp iOkHttp = new IOkHttp();
        //        RequestBody formBody = new FormBody.Builder()
//                .add("app_version_code", "bahador")
//                .add("app_version_name", "secure123")
//                .add("signature_key", "secure123")
//                .build();
        iOkHttp.post(IOkHttp.mainhost_url + "/defineit/api/version/app_version_checker.php", formBody, new ResponseListener() {
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
