
package kaizone.android.b89.net;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;

public class Request {
    public final static String TAG = "Request";

    public final static String HTTP_GET = "http_get";
    public final static String HTTP_POST = "http_post";
    public static String version = "1.0";

    public static InputStream getHttpInputStream(String url, Listener listener)
            throws Exception {
        url = url.replaceAll(" ", "%20");
        HttpGet request = new HttpGet(url);
        request.addHeader("charset", HTTP.UTF_8);
        request.addHeader("version", version);
        HttpClient client = new DefaultHttpClient();
        InputStream in = null;
        HttpResponse response = null;
        response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            // Log.e(TAG, entity.toString());
            if (listener != null) {
                listener.onComplete();
            }
            in = entity.getContent();
            return in;
        } else {
            if (listener != null) {
                listener.onFaile();
            }
        }
        return null;
    }

    public static InputStream postHttpInputStream(String url, Listener listener)
            throws Exception {
        String host = parseHostUrl(url);
        MultipartEntity mulentity = parseParams(url);
        //
        HttpClient client = new DefaultHttpClient();
        //
        HttpPost request = new HttpPost(host);
        request.addHeader("charset", HTTP.UTF_8);
        request.addHeader("version", version);
        request.setEntity(mulentity);

        InputStream in = null;
        HttpResponse response = null;
        response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            // Log.e(TAG, entity.toString());
            if (listener != null) {
                listener.onComplete();
            }
            in = entity.getContent();
            return in;
        } else {
            if (listener != null) {
                listener.onFaile();
            }
        }
        return null;
    }

    public static String parseHostUrl(final String url) {
        if (TextUtils.isEmpty(url))
            return null;
        int k = url.indexOf("?");
        String str1 = url.substring(0, k);
        return str1;
    }

    public static MultipartEntity parseParams(final String url) throws Exception {
        if (TextUtils.isEmpty(url))
            return null;
        MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        int k = url.indexOf("?");
        String str2 = url.substring(k + 1, url.length());
        String[] str3 = str2.split("&");
        for (int i = 0; i < str3.length; i++) {
            String f = str3[i];
            String[] mf = f.split("=");
            if (mf != null && mf.length >= 2) {
                final String key = mf[0];
                final String value = mf[1];

                if (key.contains("file")) {
                    if (value != null && !value.equals("null")) {
                        mulentity.addPart(key, new FileBody(new File(value)));
                    } else {
                        mulentity.addPart(key, null);
                    }
                }
                //
                else {
                    mulentity.addPart(key, new StringBody(value, Charset.forName(HTTP.UTF_8)));
                }
            }
        }
        return mulentity;
    }

    public interface Listener {
        void onStart();

        void onComplete();

        void onExeception(String exception);

        void onFaile();
    }

}
