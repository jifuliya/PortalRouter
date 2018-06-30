package jifuliya.lyl.portalrouter2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

public class PortalAct implements AbsAct {

    private static final String TAG = "PortalAct";

    private HashMap<String, Class<?>> classHashMap;
    private static volatile PortalAct portalAct;

    public static PortalAct get() {
        if (portalAct == null) {
            synchronized (PortalRoutor.class) {
                if (portalAct == null) {
                    portalAct = new PortalAct();
                }
            }
        }
        return portalAct;
    }

    @Override
    public Object portal(Context context, PortalRouterRequest request, HashMap<String, Class<?>> classHashMap) {

        this.classHashMap = classHashMap;

        if (context instanceof Activity) {
            try {
                Intent intent = new Intent(context, query(request.getUrl()));
                context.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "portal failed, please add the action first!");
        }
        return "portal success";
    }

    private Class query(String action) throws ClassNotFoundException {

        if (classHashMap.containsKey(action)) {
            return classHashMap.get(action);
        }
        Log.i(TAG, "portal failed, please add the action first!");
        return null;
    }
}

