package ir.sublearn.tools.network_manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetworktManager {

    public static final String PPP = "ppp";
    public static final String PPTP = "pptp";
    public static final String TUN = "tun";

    public static boolean isVpnConnected() {
        try {
            StringBuilder sb = new StringBuilder();
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp()) {
                    sb.append(networkInterface.getName()).append(" ");
                }
            }
            if (sb.toString().contains(TUN) | sb.toString().contains(PPP) | sb.toString().contains(PPTP)) {
                return true;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable() && info.isConnected())
            isConnected = true;
        return isConnected;
    }

    /*
    *
    *
    *  NEW VERSION
    *
    * */


    public static boolean isVPNConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API 23+
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
            if (caps == null) {
                return false;
            }
            // چک کن آیا TRANSPORT_VPN داره یا NET_CAPABILITY_NOT_VPN نداره
            return caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                    !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21+
            // روش جایگزین برای APIهای قدیمی‌تر: چک کردن اینترفیس‌های شبکه
            return isVpnInterfacePresent();
        }
        return false; // برای APIهای زیر 21
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static boolean isVpnInterfacePresent() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (java.util.Iterator<NetworkInterface> iter = Collections.list(niList).iterator(); iter.hasNext(); ) {
                    NetworkInterface intf = iter.next();
                    if (!intf.isUp()) continue;
                    String name = intf.getName().toLowerCase();
                    if (name.contains("tun") || name.contains("ppp") || name.contains("pptp")) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showVpnSnackbar(View view) {
        Snackbar.make(view, "لطفا vpn خود را غیرفعال نمایید", Snackbar.LENGTH_LONG).show();
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false; // در موارد نادر که سرویس در دسترس نیست
        }

        // برای API 23 (Marshmallow) و بالاتر
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        // برای API 21 (Lollipop) تا 22
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network network : networks) {
                NetworkInfo networkInfo = cm.getNetworkInfo(network);
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
            return false;
        }
        // برای APIهای قدیمی‌تر (زیر Lollipop)
        else {
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

}
