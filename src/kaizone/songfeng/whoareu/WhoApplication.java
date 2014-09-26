
package kaizone.songfeng.whoareu;

import kaizone.android.b89.util.ImageCache;
import kaizone.songfeng.whoareu.c.VortexRuner;
import android.app.Application;

public class WhoApplication extends Application {

    private ImageCache mImageCache;

    public ImageCache getImageCache() {
        return mImageCache;
    }

    public void setImageCache(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

}
