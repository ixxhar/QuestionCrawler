package izharhussain.questioncrawler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class ItemTwo extends Fragment implements View.OnClickListener {

    private View v;

    private EditText etSearchQuery;
    private Button bQuery;
    private WebView wvQueryResult;

    public static ItemTwo newInstance() {
        ItemTwo fragment = new ItemTwo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.item_two, container, false);
        bindingViews();
        return v;
    }

    private void bindingViews() {
        etSearchQuery = (EditText) v.findViewById(R.id.editTextSearchQuery);
        bQuery = (Button) v.findViewById(R.id.buttonSearchButton);
        wvQueryResult = (WebView) v.findViewById(R.id.webviewSearchResult);
        bQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //wvQueryResult.loadUrl("http://www.example.com");

        String s = String.valueOf(etSearchQuery.getText());
        //s.replaceAll("\\s", "_").toLowerCase();   //not working for some reason...
        String query = "";

        String s1[] = s.split("\\s");
        for (int i = 0; i < s1.length; i++) {
            query += s1[i] + "+";
        }

        loadWebViewLoad(wvQueryResult, "https://stackoverflow.com/search?q=" + query);
    }


    private void loadWebViewLoad(WebView webview, String query) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(query);
    }
}