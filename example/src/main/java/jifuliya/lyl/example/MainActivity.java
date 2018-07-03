package jifuliya.lyl.example;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import jifuliya.lyl.example.databinding.ActivityMainBinding;
import jifuliya.lyl.portalrouter.PortalRoutor;
import jifuliya.lyl.annotation.PortalRouter;

@PortalRouter(url = "main")
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //如果不设置mode，将默认采用显式模式
        PortalRoutor.get()
                .url("one")
                .route(this);

        //设置显式模式
        PortalRoutor.get()
                .mode(PortalRoutor.MODE_EXPLICIT)
                .url("test")
                .route(this);

        //支持基本类型数据传递以及序列化对象的数据传递
        PortalRoutor.get()
                .mode(PortalRoutor.MODE_EXPLICIT)
                .url("test")
                .data("string", "str")
                .data("int", 1)
                .data("boolean", true)
                .data("float", 1.1)
                .route(this);

        //支持隐式跳转，通过设置action以及category都可以进行跳转
        PortalRoutor.get()
                .mode(PortalRoutor.MODE_IMPLICIT_ACTION, "action")
                .url("test")
                .route(this);

        //支持系统跳转，包括短信，电话等
        PortalRoutor.get()
                .mode(PortalRoutor.MODE_IMPLICIT_SYSTEM,
                        "smsto:xxx",
                        "Intent.ACTION_SENDTO",
                        "test")
                .url("one")
                .route(this);

    }
}
