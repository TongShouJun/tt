
package kaizone.android.b89.net;

import java.io.InputStream;

import kaizone.android.b89.util.Cache;
import kaizone.android.b89.util.Utils;
import kaizone.songfeng.whoareu.BuildConfig;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class DataControl {

    private static final String TAG = "DataControl";

    private static long sLastUpdateTime;

    private static String sLastUpdateUrl;

    private Context mContext;

    private IDataUpdateListener mListener;

    private ProgressDialog mProgressDialog;

    private String mTipsText;

    private boolean mShowTips;

    public DataControl(Context context) {
        mContext = context;
        mShowTips = true;
    }

    public void setShowTips(boolean show) {
        mShowTips = show;
    }

    public void setTipsText(String text) {
        mTipsText = text;
    }

    public void progressShow() {
        if (!mShowTips)
            return;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (TextUtils.isEmpty(mTipsText)) {
            mProgressDialog.setMessage("加载数据中。。。");
        } else {
            mProgressDialog.setMessage(mTipsText);
        }

        mProgressDialog.show();
    }

    public void progressHide() {
        if (!mShowTips)
            return;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void exec(String url) {
        progressShow();
        if (!TextUtils.isEmpty(sLastUpdateUrl) && sLastUpdateUrl.equals(url)) {
            long time = System.currentTimeMillis();
            long duration = time - sLastUpdateTime;
            if (duration < 500) {
                Object result = getParseSaveFile(url);
                foreUpdate(result, url);
                // Log.e(TAG, "sLastUpateTime ="+sLastUpdateTime+", local 间隔："+
                // duration);
                return;
            }
        }
        (new BackTask()).execute(url);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "" + url);
        }
    };

    public void exec(String url, String type) {
        progressShow();
        if (!TextUtils.isEmpty(sLastUpdateUrl) && sLastUpdateUrl.equals(url)) {
            long time = System.currentTimeMillis();
            long duration = time - sLastUpdateTime;
            if (duration < 500) {
                Object result = getParseSaveFile(url);
                foreUpdate(result, url);
                // Log.e(TAG, "sLastUpateTime ="+sLastUpdateTime+", local 间隔："+
                // duration);
                return;
            }
        }
        (new BackTask()).execute(url, type);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "" + url);
        }
    };

    public void addChangeListener(IDataUpdateListener listener) {
        mListener = listener;
    }

    public synchronized Object getParseSaveFile(String url) {
        // Log.e(TAG, "start getParseSaveFile url"+ url);
        if (mListener == null)
            return null;
        String name = Cache.hashKeyForDisk(url);
        InputStream localin = Utils.getSaveFile(mContext, name);
        String jsonString = Utils.streamToString(localin);
        Object obj = mListener.backParse(jsonString, url);
        // Log.e(TAG, "end getParseSaveFile url"+ url);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, jsonString);
        }
        return obj;
    }

    public synchronized void foreUpdate(Object result, String url) {
        // Log.e(TAG, "start foreUpdate url"+ url);
        progressHide();
        if (mListener == null)
            return;
        mListener.foreUpdate(result, url);
        sLastUpdateTime = System.currentTimeMillis();
        sLastUpdateUrl = url;
        // Log.e(TAG, "end foreUpdate url"+ url);
    }

    public interface IDataUpdateListener {

        Object backParse(String datastring, String requeststring);

        void foreUpdate(Object result, String requeststring);

        void exception(final Exception e);

    }

    private class BackTask extends AsyncTask<Object, Object, Object> {
        private String url;

        @Override
        protected Object doInBackground(Object... params) {
            Object obj = null;
            Object data = params[0];
            if (data instanceof String) {
                url = (String) data;
                InputStream netin = null;
                try {
                    if (params.length >= 2 && params[1] != null) {
                        if (params[1] instanceof String) {
                            String type = (String) params[1];
                            if (type.equals(Request.HTTP_GET)) {
                                netin = Request.getHttpInputStream(url, null);
                            }//
                            else if (type.equals(Request.HTTP_POST)) {
                                netin = Request.postHttpInputStream(url, null);
                            }
                        }
                    }
                    //
                    else {
                        netin = Request.getHttpInputStream(url, null);
                    }

                    if (netin != null) {
                        String name = Cache.hashKeyForDisk(url);
                        Utils.saveFile(mContext, name, netin);
                    }
                    obj = getParseSaveFile(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mListener != null) {
                        mListener.exception(e);
                    }
                }
            }
            return obj;
        }

        @Override
        protected void onPostExecute(Object result) {
            foreUpdate(result, url);
        }

    }

}
