package net.fiezhi.hybridbridgetest2.bridge;

import android.webkit.WebView;

import org.json.JSONObject;

/**
 * @Author Horizon
 * @ClasssName IbridgeImpl
 * @Description
 * @UpdateDate 2020/10/21 4:08 PM
 */
public class BridgeImpl implements IBridge {
    public static void setPicture(WebView webView, JSONObject param) {
        try {
            String name = (String) param.get("key1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
