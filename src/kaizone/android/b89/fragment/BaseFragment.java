package kaizone.android.b89.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class BaseFragment extends Fragment {

	private BaseFragment mContent;

	protected int fragmentId = 0;

	public void setFragmentId(int id) {
		fragmentId = id;
	}

	public int getFragmentId() {
		return fragmentId;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mContent = this;
		super.onActivityCreated(savedInstanceState);
	}

	public void switchContent(int layoutId, BaseFragment from, BaseFragment to) {
		if (from != to) {
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(layoutId, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}
	
	public void createChildContent(int layoutId, Fragment bf){
	    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(layoutId, bf);
        ft.commit();
	}
	
	public void replaceChildContent(int layoutId, Fragment bf){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(layoutId, bf);
        ft.commit();
    }
	
}
