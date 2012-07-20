package name.azuwis.webhelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YoukuHelperActivity extends Activity {

    private static final String TAG = "YoukuHelperActivity";

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private WebView webview;
    private Boolean play;
    private ArrayList<String> linkList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = new WebView(this);
        play = false;
        linkList = new ArrayList<String>();
        mainListView = (ListView) findViewById( R.id.mainListView );
        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row);
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                play = true;
                //Object listItem = mainListView.getItemAtPosition(position);
                String link = ((TextView)view).getText().toString();
                webview.loadUrl(link);
                Log.d(TAG, "after webview");
                //Uri uri = Uri.parse(link);
                //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                //intent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
                //intent.setData(uri);
                //webIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                //webIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                //PackageManager packageManager = getPackageManager();
                //List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                //boolean isIntentSafe = activities.size() > 0;
                //if(isIntentSafe) startActivity(intent);
            }
            });

        Intent intent = getIntent();
        Uri webpage = intent.getData();

        String url = "";

        try {
            url = "http://www.flvcd.com/parse.php?flag=one&format=super&kw=" + URLEncoder.encode(webpage.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "err: " + e.getMessage());
        }

        new ParseFlvcd().execute(url);
        //finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        play = false;
        Log.d(TAG, "onStart play: " + play);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int flags = getIntent().getFlags();
        Log.d(TAG, "onResume play: " + play);
        Log.d(TAG, "onResume flags: " + flags);
    }

    private class ParseFlvcd extends AsyncTask<String, Object, Object> {

        @Override
        protected Object doInBackground(String... param) {
            //ArrayList<String> linkList = new ArrayList<String>();
            try {
                Document doc = Jsoup.connect(param[0]).get();
                Elements links = doc.select("td a[onclick]");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    Log.d(TAG, "link: " + linkHref);

                    linkList.add(linkHref);
                }
            } catch (IOException e) {
                Log.d(TAG, "err: " + e.getMessage());
            }
			return null;
        }

        @Override
        protected void onPostExecute(Object obj) {
            for (String link : linkList) {
                listAdapter.add(link);
            }
            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //Uri videoUri = Uri.parse("http://host:port/playlist.m3u8");
            //intent.setDataAndType( videoUri, "application/x-mpegURL" );
            //Uri[] uris = {Uri.parse("http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300020200500114277A0D031EA7C7510169B2-429A-23F6-1903-43B3B3EDF4FE?K=8eeaaa9a98a9c83e261c74e0,k2:13fec670333072ccd"), Uri.parse("http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300020201500114277A0D031EA7C7510169B2-429A-23F6-1903-43B3B3EDF4FE?K=39dea8b30170aea7261c74e0,k2:181cbd13a6bb7c00f")};
            //intent.setPackage( "com.mxtech.videoplayer.ad" );
            //intent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
            //intent.setData(Uri.parse("http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300010B004FFF189A3F6B058B92021D3118DA-A0F1-2F6D-56BB-E1E838D3592E?K=8202f626bce0ab022827d6e4,k2:1fe9898c4f4eee3fb"));
            //intent.putExtra("video_list", uris);
            //startActivity( intent );
        }
    }
}
