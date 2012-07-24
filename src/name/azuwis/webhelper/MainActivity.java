package name.azuwis.webhelper;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {


    private static final String TAG = "MainActivity";

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private WebView webview;
    private int listPosition = 0;
    private Boolean play = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = new WebView(this);
        mainListView = (ListView) findViewById( R.id.mainListView );
        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row);
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                play = true;
                playVideo(position);
                listPosition = position + 1;
                //String link = ((TextView)view).getText().toString();
            }
        });
        ArrayList<String> linkList = getIntent().getStringArrayListExtra("linkList");
        if (linkList == null) {
            //TODO get linkList from data storage
        }
        updateView(linkList);
    }

    @Override
	protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        play = false;
        ArrayList<String> linkList = intent.getStringArrayListExtra("linkList");
        updateView(linkList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (play && listPosition > 0 && listPosition < mainListView.getCount()) {
            playVideo(listPosition);
            listPosition++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void updateView(List<String> linkList) {
        if (linkList != null) {
            listAdapter.clear();
            listPosition = 0;
            for (String link : linkList) {
                listAdapter.add(link);
            }
        }
    }

    private void playVideo(int position) {
        mainListView.getChildAt(position).setBackgroundColor(Color.GRAY);
        Object item = mainListView.getItemAtPosition(position);
        webview.loadUrl((String)item);
    }
}
