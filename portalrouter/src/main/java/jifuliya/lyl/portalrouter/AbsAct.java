package jifuliya.lyl.portalrouter;

import android.content.Context;

import java.util.HashMap;

public interface AbsAct {

    Object portal(Context context, DataImplicit dataImplicit, PortalRouterRequest request, HashMap<String, Class<?>> classHashMap);
}
