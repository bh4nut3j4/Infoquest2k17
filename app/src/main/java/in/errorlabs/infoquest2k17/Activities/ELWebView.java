package in.errorlabs.infoquest2k17.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.steamcrafted.loadtoast.LoadToast;

import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;

public class ELWebView extends Activity {

    private WebView webView;
    LoadToast lt;
    String url;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elwebview);
        Bundle bundle = getIntent().getExtras();
        String name =bundle.getString("name");
        if (name.equals("errorlabs")){
             url = "http://errorlabs.in";
        }else if (name.equals("iqsite")){
             url = "http://infoquest2017.com";
        }else if (name.equals("ppt")){
            url = "https://forms.zohopublic.com/bhanutejar07/form/Infoquest17/formperma/4g4BdjBg_4ddm6Ca623g8530G";
        }
        webView = (WebView) findViewById(R.id.webview);
        lt=new LoadToast(this);
        Connection connection = new Connection(getApplicationContext());
        boolean checkinternet = connection.isInternet();
        if (checkinternet){
            lt.setText("Loading...");
            loadpage();

        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

    }

    private void loadpage() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Conneting...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                lt.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                lt.success();

            }

        });
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}

