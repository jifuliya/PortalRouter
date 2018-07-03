package jifuliya.lyl.portalrouter;

public class DataImplicit {

    private String mode;

    private String action;

    private String category;

    private String scheme;

    private String host;

    private String port;

    private String pathPattern;

    private String mimeType;

    private String uri;

    private String intentType;

    private String content;

    public DataImplicit() {
        this.mode = "explicit";
        this.action = "";
        this.category = "";
        this.scheme = "";
        this.host = "";
        this.port = "";
        this.pathPattern = "";
        this.mimeType = "";
        this.uri = "";
        this.intentType = "";
        this.content = "";
    }

    public void clear(){
        this.mode = "explicit";
        this.action = "";
        this.category = "";
        this.scheme = "";
        this.host = "";
        this.port = "";
        this.pathPattern = "";
        this.mimeType = "";
        this.uri = "";
        this.intentType = "";
        this.content = "";
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIntentType() {
        return intentType;
    }

    public void setIntentType(String intentType) {
        this.intentType = intentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
