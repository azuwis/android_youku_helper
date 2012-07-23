package name.azuwis.webhelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YoukuHelperActivity extends Activity {

    private static final String TAG = "YoukuHelperActivity";

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private WebView webview;
    private Context appContext;
    private int listPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = new WebView(this);
        appContext = getApplicationContext();
        mainListView = (ListView) findViewById( R.id.mainListView );
        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row);
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPosition = position + 1;
                Object link = mainListView.getItemAtPosition(position);
                //String link = ((TextView)view).getText().toString();
                webview.loadUrl((String)link);
                Log.d(TAG, "position: "+ position);
            }
            });
        callFlvcd(getIntent());
    }

    @Override
	protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        callFlvcd(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listPosition > 0 && listPosition < mainListView.getCount()) {
          Object item = mainListView.getItemAtPosition(listPosition);
          listPosition++;
          webview.loadUrl((String)item);
        }
    }

    private void callFlvcd(Intent intent) {
        listPosition = 0;
        Uri webpage = intent.getData();

        String url = "";

        try {
            url = "http://www.flvcd.com/parse.php?flag=one&format=super&kw=" + URLEncoder.encode(webpage.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "err: " + e.getMessage());
        }

        new ParseFlvcd().execute(url);
    }

    private class ParseFlvcd extends AsyncTask<String, Object, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... param) {
            ArrayList<String> linkList = new ArrayList<String>();
            String url = param[0];
            Log.d(TAG,"flv url: " + url);
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("td a[onclick]");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    Log.d(TAG, "video link: " + linkHref);

                    linkList.add(linkHref);
                }
            } catch (IOException e) {
                Log.d(TAG, "err: " + e.getMessage());
            }
			return linkList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> linkList) {
            listAdapter.clear();
            if(linkList.isEmpty()) {
                Toast toast = Toast.makeText(appContext, "Error when getting video links, try again later", Toast.LENGTH_SHORT);
                toast.show();
            }
            for (String link : linkList) {
                listAdapter.add(link);
            }
        }
    }
}
