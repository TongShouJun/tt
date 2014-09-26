
package kaizone.android.b89.util;

import java.io.File;

import android.content.Context;

public class CompressImage {

    public static final String FILE_CACHE_DIR = "855946172690287305";

    public static final int MAX = 300 * 1024;

    private Context mContext;

    private ICompressListener mListener;

    public CompressImage(Context context) {
        mContext = context;
    }

    public void exec(File oriFile, ICompressListener listener) {
        mListener = listener;
        (new CompressTask()).execute(oriFile);
        mListener.start(oriFile);
    }

    private class CompressTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            Object param0 = params[0];
            if (param0 instanceof File) {
                File oriFile = (File) param0;
                File dir = Cache.getDiskCacheDir(mContext, FILE_CACHE_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String name = Utils.date() + ".jpg";
                File newFile = new File(dir, name);
                Utils.compressImage(oriFile, newFile, MAX);
                return newFile;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof File) {
                File newFile = (File) result;
                if (mListener != null) {
                    mListener.end(newFile);
                }
            }
        }

    }

    public interface ICompressListener {
        void start(File file);

        void end(File file);
    }

}
