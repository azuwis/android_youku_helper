package name.azuwis.webhelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YoukuHelperActivity extends Activity {

    private static final String TAG = "YoukuHelperActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Uri webpage = intent.getData();

        String url = "";

        try {
            url = "http://www.flvcd.com/parse.php?flag=one&format=super&kw=" + URLEncoder.encode(webpage.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "err: " + e.getMessage());
        }

        new ParseFlvcd().execute(url);
    }

    private class ParseFlvcd extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {
            try {
                Document doc = Jsoup.connect((String)param[0]).get();
                Elements links = doc.select("td a[onclick]");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    Log.d(TAG, "link: " + linkHref);

                    Uri webpage = Uri.parse(linkHref);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    PackageManager packageManager = getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
                    boolean isIntentSafe = activities.size() > 0;
                    if(isIntentSafe) startActivity(webIntent);
                }
            } catch (IOException e) {
                Log.d(TAG, "err: " + e.getMessage());
            }
            return null;
        }
    }
}
