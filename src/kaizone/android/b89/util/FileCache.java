
package kaizone.android.b89.util;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import android.content.Context;

public class FileCache extends Cache {

    public static final String FILE_CACHE_DIR = "Tushuo-Pic";

    private File mHttpCacheDir;

    private LinkedHashMap<String, String> lruFileMap = new LinkedHashMap<String, String>();

    public FileCache(Context context) {
        mHttpCacheDir = getDiskCacheDir(context, FILE_CACHE_DIR);
        initHttpDiskCache();
    }

    private void initHttpDiskCache() {
        if (!mHttpCacheDir.exists()) {
            mHttpCacheDir.mkdirs();
        } else {
            File[] files = mHttpCacheDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                lruFileMap.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    public File createFile(String name) {
        return new File(mHttpCacheDir.getAbsolutePath() + File.separator + name);
    }

    public File createFile(String directoryname, String filename) {
        File directory = new File(mHttpCacheDir.getAbsolutePath() + File.separator + directoryname);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdir();
        }
        return new File(directory, filename);
    }

    public void addFileToCache(String url, String path) {
        lruFileMap.put(url, path);
    }

    public String getFilePath(String url) {
        String urlMD5 = hashKeyForDisk(url);
        String path = lruFileMap.get(urlMD5);
        return path;
    }

    public String getFilePath2(String urlmd5) {
        String path = lruFileMap.get(urlmd5);
        return path;
    }

    public boolean isHasFileCache(String url) {
        String path = getFilePath(url);
        if (path == null)
            return false;
        if (path.equals(""))
            return false;
        else
            return true;
    }

    public void clearCache() {
        Set<String> pathSet = lruFileMap.keySet();
        Iterator<String> iterator = pathSet.iterator();
        while (iterator.hasNext()) {
            String url = iterator.next();
            String path = lruFileMap.get(url);
            File file = new File(path);
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        lruFileMap.clear();
    }

    // public InputStream open(String url, String name) {
    // String path = getFilePath(url);
    // return ZipUtils.open(path, name);
    // }

    public void deleteCache(String url) {
        String urlMD5 = hashKeyForDisk(url);
        String path = lruFileMap.get(urlMD5);
        if (path != null && !path.equals("")) {
            File file = new File(path);
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        lruFileMap.remove(urlMD5);
        // delete unzip
        String unzipName = FileWorker.obianPrefix(url);
        File unzipFile = new File(mHttpCacheDir, unzipName);
        if (unzipFile != null && unzipFile.exists()) {
            unzipFile.delete();
        }
    }
    
    public File getCacheDir() {
        return mHttpCacheDir;
    }

    // public static FileCache findOrCreateCache(MagaZinApp app) {
    // FileCache fileCache = app.getFileCache();
    // if (fileCache == null) {
    // fileCache = new FileCache(app);
    // }
    // return fileCache;
    // }

}
