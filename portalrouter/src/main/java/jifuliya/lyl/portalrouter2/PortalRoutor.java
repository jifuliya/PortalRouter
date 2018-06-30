package jifuliya.lyl.portalrouter2;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class PortalRoutor {

    private static final String TAG = "PortalRouter";

    private static volatile PortalRoutor router;
    private AbsAct absAct;
    private HashMap<String, AbsAct> actions;
    private HashMap<String, Class<?>> clsHashMap;

    private PortalRoutor() {
        actions = new HashMap<>();
        clsHashMap = new HashMap<>();
        PortalRouterRequest.initialize();
    }

    public static PortalRoutor initialize() {
        if (router == null) {
            synchronized (PortalRoutor.class) {
                if (router == null) {
                    router = new PortalRoutor();
                }
            }
        }
        return router;
    }

    public static PortalRoutor get() {
        if (router != null) {
            return router;
        } else {
            Log.i(TAG, "Router has not been initialized");
            return null;
        }
    }

    public PortalRoutor url(String url) {
        if (clsHashMap.containsKey(url)) {
            Log.i(TAG, "url find success!");
            PortalRouterRequest.get().setUrl(url);
            return router;
        } else {
            Log.i(TAG, "url do not regist!");
            throw new NullPointerException();
        }
    }

    /**
     * @param act @PortalAction
     * save act with act-key which user tag(@PortalAction(action = "user define"))
     *
     * @return router
     */
    public PortalRoutor act(String act) {
        if (router != null) {
            if (actions.containsKey(act)) {
                absAct = actions.get(act);
            } else {
                Log.i(TAG, "act do not regist!");
            }
        }
        return router;
    }
    /**
     * @param act if user want to delete one action, user can input
     *            the act-key to find the act in the map, and use cancelAct
     *            to delete it.
     * delete act in the map
     *
     * @return router
     */
    public PortalRoutor cancelAct(String act) {
        if (router != null) {
            if (actions.containsKey(act)) {
                actions.remove(act);
            } else {
                Log.i(TAG, "act not in the map!");
            }
        }
        return router;
    }

    /**
     * @param key use key to get value in map(HashMap)
     * @param value if user want to router data, can use method
     *              PortalRouter.data(key, value) to post the data
     *              to other Activity(Fragment).
     * get router data
     *
     * @return map
     */
    public PortalRoutor data(String key, Object value) {
        if (router != null) {
            PortalRouterRequest.get().data(key, value);
        }
        return router;
    }

    /**
     * get router data
     *
     * @return map
     */
    public HashMap<String, Object> getData() {
        return PortalRouterRequest.get().getData();
    }

    /**
     * RouterAdapter get map through this method
     *
     * @return map
     */
    public HashMap<String, Class<?>> getClsMap() {
        return clsHashMap;
    }

    public HashMap<String, AbsAct> getActionMap() {
        return actions;
    }

    public PortalRouterResponse send(Context context) {
        if (router != null) {
            PortalRouterResponse response = new PortalRouterResponse();
            PortalRouterRequest request = PortalRouterRequest.get();
            if (absAct == null) {
                PortalAct portalAct = new PortalAct();
                Object mObject = portalAct.portal(context, request, clsHashMap);
                response.setStatus(PortalRouterResponse.SUCCESS_CODE
                        , PortalRouterResponse.SUCCESS_DESC
                        , mObject);
            } else {
                Object mObject = absAct.portal(context, request, clsHashMap);
                response.setStatus(PortalRouterResponse.SUCCESS_CODE
                        , PortalRouterResponse.SUCCESS_DESC
                        , mObject);
            }
            return response;
        } else {
            Log.i(TAG, "router has not been initialized");
        }
        clear();
        Log.i(TAG, "router go wrong");
        return null;
    }

    private void clear() {
        absAct = null;
        PortalRouterRequest.get().clear();
    }
}
