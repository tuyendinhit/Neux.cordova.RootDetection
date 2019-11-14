package Neux.cordova.RootDetection;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PermissionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.*;
import java.io.*;

import android.content.pm.PackageManager;
import android.app.ActivityManager;

import android.Manifest;

/**
 * This class echoes a string called from JavaScript.
 */
public class RootDetection extends CordovaPlugin {
    private static final String[] cheakPackagesList = {
            "com.thirdparty.superuser", "eu.chainfire.supersu", "com.noshufou.android.su",
            "com.koushikdutta.superuser", "com.zachspong.temprootremovejb", "com.ramdroid.appquarantine", "com.topjohnwu.magisk"
    };

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("detect")) {
            this.rootDetect(callbackContext);
            return true;
        }

        return false;
    }

    private void rootDetect(CallbackContext callbackContext) {
        if(this.checkRootByFile() || this.checkRootByPackage()) {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, true);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, false);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    }

    private boolean checkRootByFile() {
        for(String pathDir : System.getenv("PATH").split(":")){
            if(new File(pathDir, "su").exists()) {
                return true;
            }
        }
        return false;
    }

    private  boolean checkRootByPackage() {
        for (String packageUri : cheakPackagesList) {
            if (this.appInstalledOrNot(packageUri)) {
                return true;
            }
        }
        return false;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = this.cordova.getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}
