
package kaizone.songfeng.whoareu.activity;

import kaizone.android.b89.fragment.WhoPageStubFragment;
import kaizone.songfeng.whoareu.R;
import android.os.Bundle;

public class WhoContentActivity extends BaseActivity {

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_content);
        
        createContent(R.id.content, WhoPageStubFragment.newInstance());
        
    }
    

}
