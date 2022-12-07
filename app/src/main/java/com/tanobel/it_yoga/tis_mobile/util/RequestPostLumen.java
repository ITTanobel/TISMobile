package com.tanobel.it_yoga.tis_mobile.util;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
public class RequestPostLumen {
    JSONObject json,filenya;
    String path,jsonData="";
    Boolean connection;
    Context context;
    RequestBody body;

    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/*");
    //IP server
    String url="http://172.16.16.23/api-approval/public/";
    //String url="https://tanobelfood.com/api-approval/public/";
    InternetConnection internetCon=new InternetConnection();
    public RequestPostLumen(String path, JSONObject json, Context context) {
        if (internetCon.checkConnection(context)) {
            this.json = json;
            this.path = path;
            this.context = context;
            connection = true;
        }else{
            connection = false;
            Toast toast=Toast.makeText(context,"Tidak terhubung ke internet",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String execPost() throws IOException {
        if (connection) {
            try {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                /*
                CertificatePinner certificatePinner = new CertificatePinner.Builder()
                        .add("api.sellbuytime.com", "sha256/kNyjLa1QDS3He+34RywWU2Rq1vM9vadx0tqI2QMBzjY=")
                        .add("api.sellbuytime.com", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
                        .add("api.sellbuytime.com", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
                        .build();
                 */
                OkHttpClient client = new OkHttpClient.Builder()
                        //.certificatePinner(certificatePinner)
                        .addInterceptor(new AddCookiesInterceptor(this.context))
                        .addInterceptor(new ReceivedCookiesInterceptor(this.context))
                        .addInterceptor(logging)
                        .build();
                Log.i("okhttp", "persiapan JSON");
                if (this.json != null) {
                    body = RequestBody.create(JSON, this.json.toString());
                } else {
                    body = RequestBody.create(JSON, "");
                }
                Request newReq = new Request.Builder()
                        .url(url + path)
                        .addHeader("api-key","mpr38rTz49fmw3SUwdp23KJNGOqnG6Tf")
                        .post(body)
                        .build();
                Log.i("okhttp", "Request JSON");

                Response response = client.newCall(newReq).execute();
                jsonData = response.body().string();
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
                jsonData = "";
            }
        }
        return jsonData;
    }

    public void execPostCall(Callback callback) throws IOException {
        if (connection) {
            try {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                /*
                CertificatePinner certificatePinner = new CertificatePinner.Builder()
                        .add("api.sellbuytime.com", "sha256/kNyjLa1QDS3He+34RywWU2Rq1vM9vadx0tqI2QMBzjY=")
                        .add("api.sellbuytime.com", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
                        .add("api.sellbuytime.com", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
                        .build();
                 */
                OkHttpClient client = new OkHttpClient.Builder()
                        //.certificatePinner(certificatePinner)
                        .addInterceptor(new AddCookiesInterceptor(this.context))
                        .addInterceptor(new ReceivedCookiesInterceptor(this.context))
                        .addInterceptor(logging)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();
                Log.i("okhttp", "persiapan JSON");
                if (this.json != null) {
                    body = RequestBody.create(JSON, this.json.toString());
                } else {
                    body = RequestBody.create(JSON, "");
                }
                Request newReq = new Request.Builder()
                        .url(url + path)
                        .addHeader("api-key","mpr38rTz49fmw3SUwdp23KJNGOqnG6Tf")
                        .post(body)
                        .build();
                Log.i("okhttp", "Request JSON");
                client.newCall(newReq).enqueue(callback);
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
                jsonData = "";
            }
        }
    }
    public RequestPostLumen(String path, JSONObject json, JSONObject filenya, Context context){
        if (internetCon.checkConnection(context)) {
            this.path = path;
            this.json = json;
            this.context = context;
            this.filenya = filenya;
        }else{
            Toast toast=Toast.makeText(context,"Internet not available",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void execPostuploadCall(Callback callback) throws UnknownHostException, UnsupportedEncodingException {
        if (internetCon.checkConnection(context)) {
            try {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                /*
                CertificatePinner certificatePinner = new CertificatePinner.Builder()
                        .add("api.sellbuytime.com", "sha256/kNyjLa1QDS3He+34RywWU2Rq1vM9vadx0tqI2QMBzjY=")
                        .add("api.sellbuytime.com", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
                        .add("api.sellbuytime.com", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
                        .build();
                */
                OkHttpClient client = new OkHttpClient.Builder()
                        //.certificatePinner(certificatePinner)
                        .addInterceptor(new AddCookiesInterceptor(this.context))
                        .addInterceptor(new ReceivedCookiesInterceptor(this.context))
                        .addInterceptor(logging)
                        .build();
                Log.i("okhttp", "persiapan file upload");
                MultipartBody.Builder buildernew = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                Iterator<String> keysIterator = json.keys();
                while (keysIterator.hasNext()) {
                    String keyStr = keysIterator.next();
                    String valueStr = json.getString(keyStr);
                    Log.i("data ", keyStr + " " + valueStr);
                    buildernew.addFormDataPart(keyStr, valueStr);
                }
                keysIterator = filenya.keys();
                while (keysIterator.hasNext()) {
                    String keyStr = keysIterator.next();
                    String valueStr = filenya.getString(keyStr);
                    String filename = valueStr.substring(valueStr.lastIndexOf("/") + 1);
                    Log.i("file ", keyStr + " " + filename);
                    File f = new File(valueStr);
                    if (f.exists()) {
                        buildernew.addFormDataPart(keyStr, filename, RequestBody.create(MEDIA_TYPE_IMG, f));
                    }
                }
                MultipartBody requestBody = buildernew.build();
                Request newReq = new Request.Builder()
                        .url(url + path)
                        .post(requestBody)
                        .build();
                Log.i("okhttp", "Request JSON");
                client.newCall(newReq).enqueue(callback);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }
        } else {
            Toast toast=Toast.makeText(context, "Internet not available",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
