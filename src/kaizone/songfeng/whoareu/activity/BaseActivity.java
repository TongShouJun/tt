
package kaizone.songfeng.whoareu.activity;

import kaizone.android.b89.fragment.BaseFragment;
import kaizone.songfeng.whoareu.c.VortexRuner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class BaseActivity extends FragmentActivity {

    protected VortexRuner mRuner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRuner();

    }

    public void initRuner() {
        mRuner = new VortexRuner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mRuner != null) {
            mRuner.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRuner != null) {
            mRuner.stop();
        }
    }
    
    public void createContent(int layoutId, BaseFragment bf) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(layoutId, bf);
        ft.commit();
    }

    public void createContent(int layoutId, Fragment bf) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(layoutId, bf);
        ft.commit();
    }

    public void showContent(BaseFragment bf) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(bf);
        ft.commit();
    }

    public void replaceContent(int layoutId, BaseFragment bf) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(layoutId, bf);
        ft.commit();
    }

    public void switchContent(int layoutId, BaseFragment from, BaseFragment to) {
        if (from != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from).add(layoutId, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

}
