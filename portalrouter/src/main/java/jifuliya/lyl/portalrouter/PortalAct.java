package jifuliya.lyl.portalrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static jifuliya.lyl.portalrouter.Constant.REQUEST_CODE;

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
    public Object portal(Context context, DataImplicit dataImplicit,
                         PortalRouterRequest request,
                         HashMap<String, Class<?>> classHashMap) {

        this.classHashMap = classHashMap;

        //start activity with different mode
        switch (dataImplicit.getMode()) {
            case PortalRoutor.MODE_EXPLICIT:
                if (!request.getData().isEmpty()) {
                    Intent intent = new Intent(context, query(request.getUrl()));
                    for (String key : request.getData().keySet()) {
                        intentSet(key, request, intent);
                    }
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, query(request.getUrl()));
                    context.startActivity(intent);
                }
                break;
            case PortalRoutor.MODE_IMPLICIT_ACTION:
                if (!request.getData().isEmpty()) {
                    Intent intent = new Intent();
                    intent.setAction(dataImplicit.getAction());
                    for (String key : request.getData().keySet()) {
                        intentSet(key, request, intent);
                    }
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(dataImplicit.getAction());
                    context.startActivity(intent);
                }
                break;
            case PortalRoutor.MODE_IMPLICIT_CATEGORY:
                if (!request.getData().isEmpty()) {
                    Intent intent = new Intent();
                    intent.setAction(dataImplicit.getAction());
                    intent.addCategory(dataImplicit.getCategory());
                    for (String key : request.getData().keySet()) {
                        intentSet(key, request, intent);
                    }
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(dataImplicit.getAction());
                    intent.addCategory(dataImplicit.getCategory());
                    context.startActivity(intent);
                }
                break;
            case PortalRoutor.MODE_IMPLICIT_SYSTEM:
                Uri systemUri = null;
                Intent systemIntent = null;
                switch (dataImplicit.getIntentType()) {
                    case "Intent.ACTION_VIEW":
                        systemUri = Uri.parse(dataImplicit.getUri());
                        systemIntent = new Intent(Intent.ACTION_VIEW, systemUri);
                        context.startActivity(systemIntent);
                        break;
                    case "Intent.ACTION_DIAL":
                        systemUri = Uri.parse(dataImplicit.getUri());
                        systemIntent = new Intent(Intent.ACTION_DIAL, systemUri);
                        context.startActivity(systemIntent);
                    case "Intent.ACTION_CALL":
                        systemUri = Uri.parse(dataImplicit.getUri());
                        systemIntent = new Intent(Intent.ACTION_CALL, systemUri);//注意区别于上面4.3的aciton
                        context.startActivity(systemIntent);
                    case "Intent.ACTION_SENDTO":
                        systemUri = Uri.parse(dataImplicit.getUri());//指定接收者
                        systemIntent = new Intent(Intent.ACTION_SENDTO, systemUri);
                        systemIntent.putExtra("sms_body", dataImplicit.getContent());
                        context.startActivity(systemIntent);
                }
                break;
            case PortalRoutor.MODE_RESULT:
                if (context instanceof Activity) {
                    if (!request.getData().isEmpty()) {
                        Intent intent = new Intent(context, query(request.getUrl()));
                        for (String key : request.getData().keySet()) {
                            intentSet(key, request, intent);
                        }
                        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(context, query(request.getUrl()));
                        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                break;
        }

        return "portal success";
    }


    @SuppressWarnings("unchecked")
    private void intentSet(String key, PortalRouterRequest request, Intent intent) {
        Object object = request.getData().get(key);
        Bundle bundle = request.getBundle();
        if (object != null) {
            if (object instanceof String) {
                bundle.putString(key, (String) object);
                intent.putExtras(bundle);
            } else if (object instanceof Integer) {
                bundle.putInt(key, (Integer) object);
                intent.putExtras(bundle);
            } else if (object instanceof Float) {
                bundle.putFloat(key, (Float) object);
                intent.putExtras(bundle);
            } else if (object instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) object);
                intent.putExtras(bundle);
            } else if (object instanceof Double) {
                bundle.putDouble(key, (Double) object);
                intent.putExtras(bundle);
            } else if (object instanceof Bundle) {
                intent.putExtra(key, (Bundle) object);
            } else if (object instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) object);
                intent.putExtras(bundle);
            } else if (object instanceof ArrayList) {
                if (((ArrayList) object).get(0) instanceof Parcelable) {
                    bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) object);
                    intent.putExtras(bundle);
                } else if (((ArrayList) object).get(0) instanceof Integer) {
                    bundle.putIntegerArrayList(key, (ArrayList<Integer>) object);
                    intent.putExtras(bundle);
                } else if (((ArrayList) object).get(0) instanceof String) {
                    bundle.putStringArrayList(key, (ArrayList<String>) object);
                    intent.putExtras(bundle);
                }
            }
        }
        request.setBundle(bundle);
    }

    private Class query(String action) {

        if (classHashMap.containsKey(action)) {
            return classHashMap.get(action);
        } else {
            Log.i(TAG, "portal failed, please add the action first!");
            throw new NullPointerException();
        }
    }
}

