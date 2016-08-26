
package kaizone.android.b89.fragment;

import java.util.ArrayList;

public class WhoPageStubFragment extends PageStubFragment {

    @Override
    public void initFragment(ArrayList<BaseFragment> fragments) {
        super.initFragment(fragments);
        fragments.add(Sub01Fragment.newInstance());
        fragments.add(Sub02Fragment.newInstance());
        fragments.add(Sub03Fragment.newInstance());
//abcdefghijklmn

    }

    public static WhoPageStubFragment newInstance() {
        final WhoPageStubFragment f = new WhoPageStubFragment();
        return f;
    }

    public static WhoPageStubFragment newInstance(int type) {
        final WhoPageStubFragment f = new WhoPageStubFragment();
        return f;
    }

}
