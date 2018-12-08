package com.fileopener;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

public class FileOpener extends ReactContextBaseJavaModule {

    public FileOpener(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "FileOpener";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void open(String fileArg, String contentType, Promise promise) throws JSONException {
        File file = new File(fileArg);

        if (file.exists()) {
            try {
                Uri path;
                // 判断版本大于等于7.0
                if (Build.VERSION.SDK_INT >= 24) {
                    // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
                    path = FileProvider.getUriForFile(getReactApplicationContext(), getReactApplicationContext().getPackageName() + ".provider", file);
                    // 给目标应用一个临时授权

                } else {
                    path = Uri.fromFile(file);
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, contentType);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getReactApplicationContext().startActivity(intent);

                promise.resolve("Open success!!");
            } catch (android.content.ActivityNotFoundException e) {
                promise.reject("Open error!!");
            }
        } else {
            promise.reject("File not found");
        }
    }

}
