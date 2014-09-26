
package kaizone.android.b89.fragment;

import java.util.ArrayList;

import kaizone.songfeng.whoareu.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageStubFragment extends BaseFragment {

    public static final String TAG = PageStubFragment.class.getName();

    private ViewPager mViewPager;

    private TabFragmentPagerAdapter mAdapter;

    public ArrayList<BaseFragment> mListFragments = new ArrayList<BaseFragment>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        initFragment(mListFragments);
        View v = inflater.inflate(R.layout.page_stub_fragment_layout, null);
        mViewPager = (ViewPager) v.findViewById(R.id.view_pager);
        mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        return v;
    }

    public void initFragment(ArrayList<BaseFragment> fragments) {

    }

    public void updateListFragment(ArrayList<BaseFragment> newFragments) {
        mAdapter.setFragments(newFragments);
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mViewPager.setOnPageChangeListener(listener);
    }

    class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int itemId) {
            return mListFragments.get(itemId);
            // FragmentHolder.get().createMenuFragment(arg0);
        }

        @Override
        public int getCount() {
            return mListFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        public void setFragments(ArrayList<BaseFragment> fragments) {
            if (mListFragments != null) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                for (Fragment f : mListFragments) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                getChildFragmentManager().executePendingTransactions();
            }
            mListFragments = fragments;
            notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static PageStubFragment newInstance() {
        final PageStubFragment f = new PageStubFragment();
        return f;
    }

    public static PageStubFragment newInstance(int type) {
        final PageStubFragment f = new PageStubFragment();
        return f;
    }
}
