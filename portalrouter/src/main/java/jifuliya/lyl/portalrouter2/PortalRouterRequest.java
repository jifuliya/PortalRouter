package jifuliya.lyl.portalrouter2;

import java.util.HashMap;

public class PortalRouterRequest {

    private static volatile PortalRouterRequest request;

    private String url;
    private String action;
    private HashMap<String, Object> data;

    private PortalRouterRequest() {
        this.data = new HashMap<>();
    }

    public static PortalRouterRequest initialize() {
        if (request == null) {
            synchronized (PortalRouterRequest.class) {
                if (request == null) {
                    request = new PortalRouterRequest();
                }
            }
        }
        return request;
    }

    public static PortalRouterRequest get() {
        return request;
    }

    public PortalRouterRequest data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public PortalRouterRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public String getUrl() {
        return this.url;
    }

    public void clear() {
        if (url != null || data != null) {
            url = null;
            action = null;
            data = null;
        }
    }

}