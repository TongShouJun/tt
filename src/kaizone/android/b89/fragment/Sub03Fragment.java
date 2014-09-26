
package kaizone.android.b89.fragment;

import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Sub03Fragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView v = new TextView(getActivity());
        v.setText(Sub03Fragment.class.getName());
        Random random = new Random();
        int c = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        v.setTextColor(c);

        return v;
    }

    public static Sub03Fragment newInstance() {
        final Sub03Fragment f = new Sub03Fragment();
        return f;
    }

}
