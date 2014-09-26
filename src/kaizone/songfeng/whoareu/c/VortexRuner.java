
package kaizone.songfeng.whoareu.c;

import java.util.concurrent.LinkedBlockingQueue;

public class VortexRuner {

    private TaskLooper mTaskLooper;
    public LinkedBlockingQueue<ITask> mQueue;

    public VortexRuner() {
        mQueue = new LinkedBlockingQueue<VortexRuner.ITask>();
    }

    public void start() {
        mTaskLooper = new TaskLooper(this);
        mTaskLooper.setRuning(true);
        mTaskLooper.start();
    }

    public boolean isRuning() {
        return mTaskLooper.isRuning();
    }

    public void stop() {
        mTaskLooper.setRuning(false);
    }

    public void post(ITask itask) {
        mQueue.add(itask);
    }

    public interface ITask {
        public void onUI(Object obj);

        public Object onBackstage();
    }

}
