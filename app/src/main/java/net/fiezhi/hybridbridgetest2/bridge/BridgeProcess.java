package net.fiezhi.hybridbridgetest2.bridge;

import android.webkit.WebView;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Horizon
 * @ClasssName BridgeProcess
 * @Description
 * @UpdateDate 2020/10/21 4:15 PM
 */
public class BridgeProcess {
    private static Map<String, HashMap<String, Method>> exposedMethods = new HashMap<>();

    /**
     * 注册JavaScript需要使用的类
     *
     * @param exposedName
     * @param clazz
     */
    public static void register(String exposedName, Class<? extends IBridge> clazz) {
        if (!exposedMethods.containsKey(exposedName)) {
            try {
                exposedMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得需要的方法并储存到Map里
     */
    private static HashMap<String, Method> getAllMethod(Class injectedCls) throws Exception {
        HashMap<String, Method> mMethodsMap = new HashMap<>();
        Method[] methods = injectedCls.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            //方法属性参数不对退出
            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC)) {
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if (parameters.length == 2) {
                if (parameters[0] == WebView.class && parameters[1] == JSONObject.class) {
                    mMethodsMap.put(name, method);
                }
            }
        }
        return mMethodsMap;
    }

    /**
     * 调用java
     *
     * @param webView
     * @param className
     * @param methodName
     * @param param
     * @return
     */
    public static String callJava(WebView webView, String className, String methodName, String param) {
        if (exposedMethods.containsKey(className)) {
            HashMap<String, Method> methodHashMap = exposedMethods.get(className);

            if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                Method method = methodHashMap.get(methodName);
                if (method != null) {
                    try {
                        method.invoke(null, webView, new JSONObject(param));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
