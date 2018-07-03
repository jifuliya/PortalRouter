# PortalRouter

![](https://img.shields.io/badge/language-java-brightgreen.svg) ![](https://img.shields.io/badge/android-router-orange.svg) 

*Read this in other languages: [中文](README.ch.md), [English](README.md)* 

A lightweight, simple, smart and powerful Android routing library.


## Getting started

### Setting up the dependency

Add to your project build.gradle file:

```groovy
dependencies {
	implementation 'lyl.jifuliya:portalrouter:x.y.z'
	annotationProcessor 'lyl.jifuliya:potalrouter-compiler:x.y.z'
}
```

(Please replace `x` and `y` and `z` with the latest version numbers)

### Flow

First you should init the PortalRouter by the method below:

```java
pulic class app extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Portal.initialize();
    }
}
```
Portal is created by processor, so you only need to do `Portal.initialize()`

add the `@PortalRouter` annotation to the activity we need to route:

```java
@PortalRouter(url = "yourself define url")
public class YourActivity extends AppCompatActivity {
    ...
}
```

If you want to define a action which you can use it to go routting, you should define a class with @PortalAction
,and this class shoud implements the class AbsAct which you should achieve the method:

```java
@PortalAction(action = "yourself define action")
class Action implements AbsAct{
@Override
    public Object portal(Context context, PortalRouterRequest request, HashMap<String, Class<?>> classHashMap) {
    ...
        return null;
    }
}
```

Okay, all things get well, you can start it like this:

```java
  PortalRoutor.get()
                .url("xxx")
                .route(this);
```

### Post Data
if you want to post data to other activity, it is easy for PortalRouter:

```java
    PortalRoutor.get()
                .mode(PortalRoutor.MODE_EXPLICIT)
                .url("test")
                .data("string", "str")
                .data("int", 1)
                .data("boolean", true)
                .data("float", 1.1)
                .route(this);
```
as you see, you can set the mode which you want, and use `data()`to post, PortalRouter has supported the type such as String, 
Integer, Boolean, Float, Double, Parcelable and so on.

###Pay Attention:
1.if you don't choose the mode , PortalRouter default use `MODE_EXPLICIT`
2.if you don't set `@PortalAction`, PortalRouter have default `PortalAct` to provide you the route function.

### Action

PortalRouter also provider the action way to route, such as:

```Xml
<activity android:name=".ActionActivity">
    <intent-filter>
        <action android:name="xxx.action" />
        <category android:name="xxx.category" />
    </intent-filter>
</activity>
```

```java
  PortalRoutor.get()
                .mode(PortalRoutor.MODE_IMPLICIT_ACTION, "xxx.action")
                .url("test")
                .route(this);
```

```java
  PortalRoutor.get()
                .mode(PortalRoutor.MODE_IMPLICIT_CATEGORY, "xxx.category")
                .url("test")
                .route(this);
```

### System

if you want to use system action, it's okey. for example, you want to use system message, you should only do this:

```java
    PortalRoutor.get()
                .mode(PortalRoutor.MODE_IMPLICIT_SYSTEM,
                        "smsto:xxx",
                        "Intent.ACTION_SENDTO",
                        "example")
                .route(this);
```

### Whats new on next version:

PortalRouter will provider intercepter to let you do something easy before you route to other page.
