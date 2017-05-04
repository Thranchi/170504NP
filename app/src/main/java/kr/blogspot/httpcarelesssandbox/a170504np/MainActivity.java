package kr.blogspot.httpcarelesssandbox.a170504np;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et;
    WebView webView;
    Animation animTop;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        et = (EditText) findViewById(R.id.et);

        webView = (WebView) findViewById(R.id.webview);
        linear = (LinearLayout) findViewById(R.id.linear);

        WebSettings webSettings = webView.getSettings();
        //자바스크립트 사용
        webSettings.setJavaScriptEnabled(true);
        //webView 내장 줌 사용
        webSettings.setBuiltInZoomControls(true);
        //확대,축소 기능을 사용할 수 있도록 설정
        webSettings.setSupportZoom(true);
        // 캐쉬 사용 방법을 정의(default : LOAD_DEFAULT)
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.addJavascriptInterface(new JavaScriptMethods(), "MyApp");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                et.setText(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });

        final ProgressDialog dialog;
        dialog = new ProgressDialog(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("Loading.......");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) dialog.dismiss();
            }
        });


        webView.loadUrl("file:///android_asset/index.html");

        animTop= AnimationUtils.loadAnimation(this, R.anim.translate);
        animTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn2) {
            webView.loadUrl("javascript:changeImage()");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"즐겨찾기");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==1)
        {
            webView.loadUrl("file:///android_asset/index.html");
            linear.setAnimation(animTop);
            animTop.start();
        }
        return super.onOptionsItemSelected(item);
    }

    Handler myhandler=new Handler();
    class JavaScriptMethods{

        //이걸해줘야 자바스크립트 웹과 연동되어 호출이 가능해진다
        @JavascriptInterface
        public void displayToast(){
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("그림변경")
                            .setMessage("그림변경할꺼?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    webView.loadUrl("javascript:changeImage()");
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();
                }
            });
        }
    }
}

