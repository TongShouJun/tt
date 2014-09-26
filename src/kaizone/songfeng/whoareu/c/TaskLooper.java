
package kaizone.songfeng.whoareu.c;

import java.util.concurrent.LinkedBlockingQueue;

import kaizone.songfeng.whoareu.c.VortexRuner.ITask;
import android.os.Handler;
import android.os.Message;

public class TaskLooper extends Thread {

    private LinkedBlockingQueue<ITask> mQueue;
    private VortexRuner mRuner;
    private boolean mRuning;
    private CoreHandler mHandler;
    private ITask mCurrITask;

    public TaskLooper(VortexRuner runer) {
        mRuner = runer;
        mHandler = new CoreHandler();
    }

    @Override
    public void run() {
        while (mRuning) {
            mQueue = mRuner.mQueue;
            if (mQueue == null)
                continue;
            if (mQueue.isEmpty())
                continue;
            if (mQueue.peek() == null)
                continue;
            mCurrITask = mQueue.poll();
            if (mCurrITask == null)
                continue;
            Object obj = mCurrITask.onBackstage();
            Message msg = mHandler.obtainMessage();
            msg.obj = obj;
            mHandler.sendMessage(msg);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRuning(boolean b) {
        mRuning = b;
    }

    public boolean isRuning() {
        return mRuning;
    }

    public void destroy() {
        mQueue.clear();
        mRuning = false;
        this.interrupt();
    }

    public class CoreHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Object obj = msg.obj;
            mCurrITask.onUI(obj);
        }
    }

    public void add(ITask iTask) {
        mQueue.add(iTask);
    }

}
