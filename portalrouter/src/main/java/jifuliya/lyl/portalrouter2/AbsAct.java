package jifuliya.lyl.portalrouter2;

import android.content.Context;

import java.util.HashMap;

public interface AbsAct {

    Object portal(Context context, PortalRouterRequest request, HashMap<String, Class<?>> classHashMap);
}
