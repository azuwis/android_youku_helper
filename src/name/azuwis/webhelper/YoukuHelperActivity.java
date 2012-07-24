package name.azuwis.webhelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YoukuHelperActivity extends Activity {

    private static final String TAG = "YoukuHelperActivity";
    public static final String PREFS_VIDEOLIST = "VideoList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callFlvcd(getIntent().getData().toString());
    }

    private void callFlvcd(String url) {

        String flvUrl = "";
        try {
            flvUrl = "http://www.flvcd.com/parse.php?flag=one&format=super&kw=" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "err: " + e.getMessage());
        }

        new ParseFlvcd().execute(flvUrl);
    }

    private class ParseFlvcd extends AsyncTask<String, Object, List<String>> {

        private ProgressDialog dialog;

        @Override
        protected List<String> doInBackground(String... param) {
            List<String> linkList = new ArrayList<String>();
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
        protected void onPreExecute() {
            dialog = new ProgressDialog(YoukuHelperActivity.this);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<String> linkList) {
            dialog.dismiss();
            if(linkList.isEmpty()) {
                Toast toast = Toast.makeText(YoukuHelperActivity.this, "Error when getting video links, try again later", Toast.LENGTH_SHORT);
                toast.show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putStringArrayListExtra("linkList", (ArrayList<String>)linkList);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }
}
