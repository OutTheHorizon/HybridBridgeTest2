package net.fiezhi.hybridbridgetest2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import net.fiezhi.hybridbridgetest2.bridge.BridgeImpl;
import net.fiezhi.hybridbridgetest2.bridge.BridgeProcess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createInterface();
        useWebView();
        BridgeProcess.register("Picture", BridgeImpl.class);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void createInterface(){
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(ll);

        mWebView = new WebView(this);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.getSettings().setJavaScriptEnabled(true);
        ll.addView(mWebView);
    }
    private void useWebView(){
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                //判断信息在message里，url里是html路径。defaultValue是第二个参数。
                Log.e(TAG, "onJsPrompt: url:"+url+" message:"+ message+" defaultValue:" + defaultValue );

                if(!TextUtils.isEmpty(message) && message.startsWith("Android")){
                    String[]s =message.split("\\.");
                    Log.e(TAG, "onJsPrompt: Android" );
                    //结果放到该字符串里
                    if(s.length==4) {
                        result.confirm(BridgeProcess.callJava(view, s[1], s[2], s[3]));
                    }

                }

//                if(!TextUtils.isEmpty(defaultValue)){
//                    view.loadUrl(defaultValue);
//                }
                return true;
            }
        });
        mWebView.loadUrl("file:///android_asset/index.html");
    }


    final String JSBridgeName = "Android";
    private String callJava(String message){
        Uri uri = Uri.parse(message);
        String className = uri.getHost();
        String methodName = uri.getPath();
        String param = uri.getQuery();

        if (!TextUtils.isEmpty(methodName)) {
            methodName = methodName.replace("/", "");
        }

        if(message.startsWith(JSBridgeName)){

        }
        return null;
    }
    private void uriTest(String message){
        //通过解析message获得一定格式的数据流调用数据
        if(!TextUtils.isEmpty(message) && message.startsWith("Android")) {
            Log.e(TAG, "onJsPrompt: It's in Android" );
            String testUri = "protocol :// hostname:12/path?query=df&&dfjksa=djslf#{fragment:dfads}\n";
            Uri uri = Uri.parse(testUri);
            Log.e(TAG, "uri: "+uri.toString());
            String s = uri.getHost();
            Log.e(TAG, "host: "+s );
            s=uri.getPath();
            Log.e(TAG, "path: "+s );
            s=uri.getScheme();
            Log.e(TAG, "scheme: "+s );
            s=uri.getQuery();
            Log.e(TAG, "query: "+s );
            s=String.valueOf( uri.getPort());
            Log.e(TAG, "port: "+s );
            s=uri.getFragment();
            Log.e(TAG, "fragment: "+s );
            List<String >l = uri.getPathSegments();
            for (int i = 0; i < l.size(); i++) {
                s=l.get(i);
                Log.e(TAG, "fragment: "+i+s );
            }
        }
    }
}