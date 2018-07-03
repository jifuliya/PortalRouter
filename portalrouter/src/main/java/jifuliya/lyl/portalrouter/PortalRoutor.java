package jifuliya.lyl.portalrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class PortalRoutor {

    private static final String TAG = "PortalRouter";

    public static final String MODE_EXPLICIT = "explicit";
    public static final String MODE_IMPLICIT_ACTION = "implicit_action";
    public static final String MODE_IMPLICIT_CATEGORY = "implicit_category";
    public static final String MODE_IMPLICIT_SYSTEM = "implicit_system";
    public static final String MODE_RESULT = "result";

    private static volatile PortalRoutor router;
    private AbsAct absAct;
    private HashMap<String, AbsAct> actions;
    private HashMap<String, Class<?>> clsHashMap;
    private DataImplicit dataImplicit;
    private Bundle intentBundle;
    private Intent intent;

    private PortalRoutor() {
        actions = new HashMap<>();
        clsHashMap = new HashMap<>();
        dataImplicit = new DataImplicit();
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
            return new PortalRoutor();
        }
    }

    public PortalRoutor implicitAction() {
        return router;
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
     * @param act save act with act-key which user tag(@PortalAction(action = "user define"))
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
     *            delete act in the map
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
     * @param key   use key to get value in map(HashMap)
     * @param value if user want to router data, can use method
     *              PortalRouter.data(key, value) to post the data
     *              to other Activity(Fragment).
     *              get router data
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

    public PortalRoutor mode(String mode) {
        if (MODE_EXPLICIT.equals(mode)
                || MODE_RESULT.equals(mode)) {
            dataImplicit.setMode(mode);
        } else {
            Log.i(TAG, "mode is not availed, please setMode with availed mode");
            throw new IllegalArgumentException();
        }
        return router;
    }

    public PortalRoutor mode(String mode, String action) {
        if (MODE_IMPLICIT_ACTION.equals(mode)) {
            dataImplicit.setMode(mode);
            dataImplicit.setAction(action);
        } else {
            Log.i(TAG, "mode is not availed, please setMode with availed mode");
            throw new IllegalArgumentException();
        }
        return router;
    }

    public PortalRoutor mode(String mode, String action, String category) {
        if (MODE_IMPLICIT_CATEGORY.equals(mode)) {
            dataImplicit.setMode(mode);
            dataImplicit.setAction(action);
            dataImplicit.setCategory(category);
        } else {
            Log.i(TAG, "mode is not availed, please setMode with availed mode");
            throw new IllegalArgumentException();
        }
        return router;
    }

    public PortalRoutor mode(String mode, String uri, String intentType, String content) {
        if (MODE_IMPLICIT_SYSTEM.equals(mode)) {
            dataImplicit.setMode(mode);
            dataImplicit.setUri(uri);
            dataImplicit.setIntentType(intentType);
            dataImplicit.setContent(content);
        } else {
            Log.i(TAG, "mode is not availed, please setMode with availed mode");
            throw new IllegalArgumentException();
        }
        return router;
    }

    public void route(Context context) {
        if (router != null) {
            PortalRouterResponse response = new PortalRouterResponse();
            PortalRouterRequest request = PortalRouterRequest.get();
            if (absAct == null) {
                PortalAct portalAct = new PortalAct();
                Object mObject = portalAct.portal(context, dataImplicit, request, clsHashMap);
                response.setStatus(PortalRouterResponse.SUCCESS_CODE
                        , PortalRouterResponse.SUCCESS_DESC
                        , mObject);
            } else {
                Object mObject = absAct.portal(context, dataImplicit, request, clsHashMap);
                response.setStatus(PortalRouterResponse.SUCCESS_CODE
                        , PortalRouterResponse.SUCCESS_DESC
                        , mObject);
            }
        } else {
            Log.i(TAG, "router has not been initialized");
        }
        clear();
        Log.i(TAG, "router finish");
    }

    public PortalRoutor routeData(Context context) {
        if (context instanceof Activity) {
            intent = ((Activity) context).getIntent();
            intentBundle = intent.getExtras();
        }
        return router;
    }

    public Object getRouterData(String key) {
        if (intentBundle != null) {
            return intentBundle.get(key);
        } else {
            if (PortalRouterRequest.get().getBundle() != null) {
                return PortalRouterRequest.get().getBundle().get(key);
            } else {
                Log.i(TAG, "bundle is null");
                throw new NullPointerException();
            }
        }
    }

    public void finish() {
        intentBundle = null;
        intent = null;
        PortalRouterRequest.get().clearBundle();
    }

    private void clear() {
        absAct = null;
        dataImplicit.clear();
        PortalRouterRequest.get().clear();
    }

    interface RouterImp {
        void callBack();
    }
}
