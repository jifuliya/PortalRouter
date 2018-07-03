package jifuliya.lyl.example;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import jifuliya.lyl.annotation.PortalRouter;
import jifuliya.lyl.example.databinding.ActivityTestOneBinding;
import jifuliya.lyl.portalrouter.PortalRoutor;

@PortalRouter(url = "one")
public class TestOneActivity extends AppCompatActivity {

    public static final String TAG = "TestOneActivity";

    private ActivityTestOneBinding testOneBinding;
    private String str;
    private int mInt;
    private boolean aBoolean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testOneBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_one);

        //利用router接收对应数据，不需要getIntent
        str = (String)PortalRoutor.get().routeData(this).getRouterData("string");
        mInt = (Integer) PortalRoutor.get().routeData(this).getRouterData("int");
        aBoolean = (Boolean) PortalRoutor.get().routeData(this).getRouterData("boolean");

        Log.i(TAG, str + mInt + aBoolean);
    }
}
