package ru.mail.sergey_balotnikov.literaturetranslator.utils;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Interpreter {

    private Interpreter(){}

    public static final String LOG_TAG = "SVB";
    private static final String GET_TRANSLATE = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200227T144238Z.3bc94a8fddc6d91b.79b116caa7d689417a79c0145f00d025a47a35f5&text=%s&lang=en-ru";

    public static String translatedText(String inputText){
        String translatedString = "";
        OkHttpClient client = new OkHttpClient();
        final String url = String.format(
                GET_TRANSLATE,
                inputText);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            String responseCode = jsonObject.get("code").toString();

            if(responseCode.equals("502")){
                translatedString = "Invalid parameter: \"text\" in request";
            } else {
                translatedString = jsonObject.getJSONArray("text").get(0).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return translatedString;
    }
}
