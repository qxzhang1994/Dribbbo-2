package com.jiuzhang.guojing.dribbbo.dribbble;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.dribbbo.model.Shot;
import com.jiuzhang.guojing.dribbbo.model.User;
import com.jiuzhang.guojing.dribbbo.utils.ModelUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Dribbble {

    private static final String API_URL = "https://api.dribbble.com/v1/";

    private static final String USER_END_POINT = API_URL + "user";
    private static final String SHOTS_END_POINT = API_URL + "shots";

    private static final String SP_AUTH = "auth";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";

    private static OkHttpClient client = new OkHttpClient();

    private static String accessToken;
    private static User user;

    private static Request buildGetRequest(String url) {
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url)
                .build();
    }

    private static Request buildPostRequest(String url, RequestBody requestBody) {
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url)
                .post(requestBody)
                .build();
    }

    private static Request buildDeleteRequest(String url) {
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url)
                .delete()
                .build();
    }

    public static void init(@NonNull Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static void login(@NonNull Context context, String accessToken) throws IOException {
        Dribbble.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        Dribbble.user = getUser();
        storeUser(context, user);
    }

    public static void logout(@NonNull Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);

        accessToken = null;
        user = null;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static User getUser() throws IOException {
        Response response = client
                .newCall(buildGetRequest(USER_END_POINT))
                .execute();
        return ModelUtils.toObject(response.body().string(), new TypeToken<User>(){});
    }

    public static List<Shot> getShots(int page) throws IOException {
        String url = SHOTS_END_POINT + "?page=" + page;
        Response response = client
                .newCall(buildGetRequest(url))
                .execute();
        return ModelUtils.toObject(response.body().string(), new TypeToken<List<Shot>>(){});
    }

    public static Shot getShot(@NonNull String id) throws IOException {
        String url = SHOTS_END_POINT + "/" + id;
        Response response = client
                .newCall(buildGetRequest(url))
                .execute();
        return ModelUtils.toObject(response.body().string(), new TypeToken<Shot>(){});
    }

    public static void likeShot(@NonNull String id) throws IOException {
        String url = SHOTS_END_POINT + "/" + id + "/like";
        Response response = client.newCall(buildPostRequest(url, new FormBody.Builder().build()))
                                  .execute();
        if (response.code() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException(response.message());
        }
    }

    public static void unlikeShot(@NonNull String id) throws IOException {
        String url = SHOTS_END_POINT + "/" + id + "/like";
        Response response = client.newCall(buildDeleteRequest(url))
                                  .execute();
        if (response.code() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException(response.message());
        }
    }

    public static boolean isLikingShot(@NonNull String id) throws IOException {
        String url = SHOTS_END_POINT + "/" + id + "/like";
        Response response = client.newCall(buildGetRequest(url))
                                  .execute();
        switch (response.code()) {
            case HttpURLConnection.HTTP_OK:
                return true;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return false;
            default:
                throw new IOException(response.message());
        }
    }

    public static void storeAccessToken(@NonNull Context context, String token) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                SP_AUTH, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public static String loadAccessToken(@NonNull Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                SP_AUTH, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void storeUser(@NonNull Context context, User user) {
        ModelUtils.save(context, KEY_USER, user);
    }

    public static User loadUser(@NonNull Context context) {
        return ModelUtils.read(context, KEY_USER, new TypeToken<User>(){});
    }

}
