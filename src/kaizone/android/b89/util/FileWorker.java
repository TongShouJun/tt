
package kaizone.android.b89.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Hashtable;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FileWorker {

    private static final String TAG = "FileWorker";

    private static final int MESSAGE_CLEAR = 0;

    private static final int MESSAGE_DELETE = 1;

    private static final int MESSAGE_LOAD_UPDATE = 10;

    private static final int MESSAGE_LOAD_COMPLETE = 11;

    private static final int MESSAGE_LOAD_EXCEPTION = 12;

    private FileCache mFileCache;

    private Hashtable<String, Downloader> mLoaderTable;

    private Hashtable<String, ProgressBar> mProgressAttchBarMap;

    private Context mContext;

    protected Resources mResources;

    private OnDownLoadListener mDownLoadListener;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.arg1 == MESSAGE_LOAD_UPDATE) {
                int progress = msg.arg2;
                String url = (String) msg.obj;
                if (mDownLoadListener != null) {
                    mDownLoadListener.onUpateProgress(url, progress);
                }

                // 2
                ProgressBar progressbar = (ProgressBar) mProgressAttchBarMap.get(url);
                if (progressbar != null) {
                    progressbar.setProgress(progress);
                }
            }
            //
            else if (msg.arg1 == MESSAGE_LOAD_EXCEPTION) {
                String url = (String) msg.obj;
                // hideView(url);
                if (mDownLoadListener != null) {
                    mDownLoadListener.OnExcetption(url);
                }
            }
            //
            else if (msg.arg1 == MESSAGE_LOAD_COMPLETE) {
                // mProgressAttchBarMap.remove(key)
                String url = (String) msg.obj;
                // hideView(url);
                if (mDownLoadListener != null) {
                    mDownLoadListener.OnComplete(url);
                }
            }
        }
    };

    public FileWorker(Context context) {
        mContext = context;
        mFileCache = new FileCache(mContext);
        mResources = mContext.getResources();
        mLoaderTable = new Hashtable<String, FileWorker.Downloader>();
        mProgressAttchBarMap = new Hashtable<String, ProgressBar>();
    }

    public void check(final String url, final Object obj) {
        Downloader loader = mLoaderTable.get(url);
        if (loader != null && loader.isActive()) {
            updateAttchObjectState(obj);
            loader.setAttchObject(obj);
            loader.updateProgress();
        }
    }

    public void check(String url, FrameLayout frameLayout) {
        if (frameLayout == null) {
            return;
        }
        frameLayout.removeAllViews();
        ProgressBar bar = mProgressAttchBarMap.get(url);
        if (bar == null)
            return;
        FrameLayout barparent = (FrameLayout) bar.getParent();
        if (barparent != null) {
            barparent.removeAllViews();
            barparent.removeView(bar);
            barparent = null;
        }

        // frameLayout.removeView(bar);
        frameLayout.addView(bar, 0);
    }

    private void hideView(String url) {
        ProgressBar bar = mProgressAttchBarMap.get(url);
        if (bar == null)
            return;
        FrameLayout barparent = (FrameLayout) bar.getParent();
        if (barparent != null) {
            barparent.removeAllViews();
        }
        mProgressAttchBarMap.remove(url);
    }

    public boolean isActive(String url) {
        Downloader loader = mLoaderTable.get(url);
        if (loader == null) {
            return false;
        } else {
            if (loader.isActive) {
                return true;
            } else {
                return false;
            }
        }
    }

    // public void loadFile(String url, ProgressBar progressBar) {
    // if (mFileCache.isHasFileCache(url)) {
    // Toast.makeText(mContext, "下载文件已存在", 100).show();
    // } else {
    // Downloader loader = new Downloader(url);
    // loader.start();
    // mProgressAttchBarMap.put(url, progressBar);
    // mLoaderTable.put(url, loader);
    // }
    // }

    // public void loadFile(final String url, final OnDownLoadListener listener)
    // {
    // if (mFileCache.isHasFileCache(url)) {
    // Toast.makeText(mContext, "下载文件已存在", 100).show();
    // } else {
    // if (listener != null) {
    // mDownLoadListener = listener;
    // mDownLoadListener.OnStart();
    // Downloader loader = new Downloader(url);
    // loader.start();
    // mLoaderTable.put(url, loader);
    // }
    // }
    // }

    public void loadFile(final String url) {
        if (mFileCache.isHasFileCache(url)) {
            Toast.makeText(mContext, "下载文件已存在", 100).show();
        } else {
            Downloader downloader = mLoaderTable.get(url);
            if (downloader != null && downloader.isActive) {
                Toast.makeText(mContext, "文件正在下载中...", 100).show();
                return;
            }

            Downloader loader = new Downloader(url);
            loader.start();
            mLoaderTable.put(url, loader);
            if (mDownLoadListener != null) {
                mDownLoadListener.OnStart();
            }
        }
    }

    public void setOnLoadDownListener(OnDownLoadListener listener) {
        mDownLoadListener = listener;
    }

    public void sendLoadException(String url) {
        Message msg = mHandler.obtainMessage();
        msg.arg1 = MESSAGE_LOAD_EXCEPTION;
        msg.obj = url;
        mHandler.sendMessage(msg);
    }

    public void sendLoadComplete(String url) {
        Message msg = mHandler.obtainMessage();
        msg.arg1 = MESSAGE_LOAD_COMPLETE;
        msg.obj = url;
        mHandler.sendMessage(msg);
    }

    public void sendLoadProgress(Object obj, int progress) {
        Message msg = mHandler.obtainMessage();
        msg.arg1 = MESSAGE_LOAD_UPDATE;
        msg.obj = obj;
        msg.arg2 = progress;
        mHandler.sendMessage(msg);
    }

    public void updateAttchObjectState(Object obj) {
        if (obj != null && obj instanceof ProgressBar) {
            ((ProgressBar) obj).setVisibility(View.VISIBLE);
        }
    }

    // public void addFileCache(MagaZinApp app) {
    // FileCache fileCache = ((MagaZinApp) app).getFileCache();
    // if (fileCache == null) {
    // fileCache = new FileCache(mContext);
    // ((MagaZinApp) app).setFileCache(fileCache);
    // }
    // mFileCache = fileCache;
    // }

    public FileCache getFileCache() {
        return mFileCache;
    }

    public class Downloader implements Runnable {
        private String url;

        private int progress;

        private Object attchObject;

        private boolean isActive;

        public Downloader(String url) {
            this.url = url;
        }

        public Downloader(String url, Object obj) {
            this.url = url;
            this.attchObject = obj;
        }

        @Override
        public void run() {
            isActive = true;
            try {
                load(url);
            } catch (Exception e) {
                isActive = false;
                sendLoadException(url);
            }
            isActive = false;
        }

        public void start() {
            new Thread(this).start();
        }

        public String getUrl() {
            return url;
        }

        public boolean isActive() {
            return isActive;
        }

        public void updateProgress() {
            Message msg = mHandler.obtainMessage();
            msg.arg1 = progress;
            msg.obj = attchObject;
            mHandler.sendMessage(msg);
        }

        public Object getAttchObject() {
            return attchObject;
        }

        public void setAttchObject(Object attchObject) {
            this.attchObject = attchObject;
        }

        public File load(String urlString) throws Exception {
            File loadFile = null;
            progress = 0;
            NumberFormat nf = NumberFormat.getPercentInstance();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            int length = conn.getContentLength();
            if (length > 0) {
                InputStream is = conn.getInputStream();
                long sum = 0;
                int end;
                // String name = Cache.hashKeyForDisk(urlString);
                String name = obianName(urlString);
                loadFile = mFileCache.createFile(name);
                FileOutputStream fos = new FileOutputStream(loadFile);
                byte[] buffer = new byte[1024 * 20];
                while ((end = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, end);
                    sum += end;
                    float pro = (float) sum / length;
                    String prostr = nf.format(pro);
                    prostr = prostr.replace("%", "");
                    progress = Integer.parseInt(prostr);
                    // updateProgress();
                    sendLoadProgress(urlString, progress);
                }

                is.close();
                fos.close();
                mFileCache.addFileToCache(loadFile.getName(), loadFile.getAbsolutePath());
                loadFileComplete(urlString);
                sendLoadComplete(urlString);
            } else {
                sendLoadException(urlString);
            }
            return loadFile;
        }
    }

    private void loadFileComplete(String url) {
        if (mLoaderTable != null && !mLoaderTable.isEmpty()) {
            mLoaderTable.remove(url);
        }
    }

    protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            switch ((Integer) params[0]) {
                case MESSAGE_CLEAR:
                    clearCacheInternal();
                    break;
                case MESSAGE_DELETE:
                    if (params[1] != null) {
                        String urlString = String.valueOf(params[1]);
                        deleteCacheInternal(urlString);
                    }
                    break;
            }
            return null;
        }
    }

    private void clearCacheInternal() {
        if (mFileCache != null) {
            mFileCache.clearCache();
        }
    }

    private void deleteCacheInternal(String url) {
        if (mFileCache != null) {
            mFileCache.deleteCache(url);
        }
    }

    public void clearCache() {
        new CacheAsyncTask().execute(MESSAGE_CLEAR);
    }

    public void deleteCache(String url) {
        new CacheAsyncTask().execute(MESSAGE_DELETE, url);
    }

    public static String obianName(String url) {
        if (url == null)
            return "null";
        if (url.length() == 0)
            return "len=0";
        int lastIndex = url.lastIndexOf("/");
        String name = url.substring(lastIndex + 1);
        return name;
    }

    public static String obianPrefix(String url) {
        if (url == null)
            return "null";
        if (url.length() == 0)
            return "len=0";
        int lastIndex = url.lastIndexOf("/");
        String prefix = url.substring(lastIndex + 1);
        int a1 = prefix.lastIndexOf(".");
        prefix = prefix.substring(0, a1);
        return prefix;
    }

    public interface OnDownLoadListener {
        void OnStart();

        void onUpateProgress(Object obj, int progress);

        void OnExcetption(String url);

        void OnComplete(String url);
    }

}
