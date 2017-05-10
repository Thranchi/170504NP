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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText et;
    Button go;
    WebView webView;
    Animation animTop;
    LinearLayout linear;
    ListView listView;
    ArrayList<data> databox = new ArrayList<data>();
    ArrayList<String> list=new ArrayList<String>();
    adapter adapter;
    String tempname="",tempurl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        et = (EditText) findViewById(R.id.et);
        listView=(ListView)findViewById(R.id.listlay);
        adapter=new adapter(databox);
        listView.setAdapter(adapter);
        webView = (WebView) findViewById(R.id.webview);
        linear = (LinearLayout) findViewById(R.id.linear);
        go = (Button)findViewById(R.id.btn1) ;
        WebSettings webSettings = webView.getSettings();
        //자바스크립트 사용
        webSettings.setJavaScriptEnabled(true);
        //webView 내장 줌 사용
        webSettings.setBuiltInZoomControls(true);
        //확대,축소 기능을 사용할 수 있도록 설정
        webSettings.setSupportZoom(true);
        // 캐쉬 사용 방법을 정의(default : LOAD_DEFAULT)
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl("file:///android_asset/index.html");
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("http://"+et.getText());

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
                linear.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                webView.loadUrl("http://"+databox.get(position).getUrl());

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {  //롱클릭으로 삭제
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제확인")
                        .setMessage("선택한 주소를 정말 삭제하시겠습니까?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteitem(position);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });


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
                dialog.setMessage("Loading......");
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





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"즐겨찾기 추가");
        menu.add(0,2,0,"즐겨찾기 목록");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==1)
        {
            linear.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

            webView.loadUrl("file:///android_asset/urladd.html");
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
            linear.setAnimation(animTop);
            animTop.start();
        }
        if(item.getItemId()==2)
        {
            //webView.loadUrl("file:///android_asset/index.html");

            linear.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    final Handler myhandler=new Handler();
    class JavaScriptMethods{

        //이걸해줘야 자바스크립트 웹과 연동되어 호출이 가능해진다
        @JavascriptInterface
        public void displayToast(){
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    linear.setVisibility(View.VISIBLE);
                    AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("그림변경")
                            .setMessage("그림을 변경하시겠습니까?")
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
        @JavascriptInterface
        public void getinformation(final String tempname, final String tempurl){
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    if(adapter.getCount()==0){
                        adapter.addItem(tempname,tempurl);
                        Toast.makeText(getApplicationContext(), "목록이 추가되었습니다"  , Toast.LENGTH_SHORT).show();
                        webView.loadUrl("javascript: blank()");
                    }
                    else {
                        if(!adapter.findItem(tempurl)){
                            adapter.addItem(tempname,tempurl);
                            Toast.makeText(getApplicationContext(), "목록이 추가되었습니다" , Toast.LENGTH_SHORT).show();
                            webView.loadUrl("javascript: blank()");
                        }
                        else{
                            webView.loadUrl("javascript: displayMsg()");
                        }

                    }
                }
            });
        }
    }

}

