
package kaizone.songfeng.whoareu;

import java.io.InputStream;
import java.util.Random;

import kaizone.android.b89.net.Request;
import kaizone.android.b89.util.Utils;
import kaizone.songfeng.whoareu.activity.BaseActivity;
import kaizone.songfeng.whoareu.activity.RegisterActivity;
import kaizone.songfeng.whoareu.activity.WhoContentActivity;
import kaizone.songfeng.whoareu.c.VortexRuner.ITask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WhoMain extends BaseActivity {

    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_main);
        WhoApplication app = (WhoApplication) getApplication();
        //

        mImageView = (ImageView) findViewById(R.id.image01);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhoMain.this, RegisterActivity.class));
            }
        });

        mButton = (Button) findViewById(R.id.btn01);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhoMain.this, WhoContentActivity.class));
            }
        });

        mTextView = (TextView) findViewById(R.id.tv01);

        int i = 0;
        while (i < 1000) {
            i++;
            final int p = i % 10;
            mRuner.post(new ITask() {

                @Override
                public void onUI(Object obj) {
                    if (p == 1 && obj != null) {
                        mTextView.setText(obj.toString());
                        Random random = new Random();
                        int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        mTextView.setBackgroundColor(color);
                    }
                }

                @Override
                public Object onBackstage() {
                    try {
                        InputStream in = Request.getHttpInputStream(
                                "http://yule.2258.com/mingxing/pandian/89847" + p + ".html", null);
                        String str = Utils.streamToString(in);
                        return str;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.who_main, menu);
        return true;
    }

}
