package name.azuwis.webhelper;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
                listPosition = position + 1;
                Object link = mainListView.getItemAtPosition(position);
                //String link = ((TextView)view).getText().toString();
                webview.loadUrl((String)link);
                Log.d(TAG, "position: "+ position);
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
        ArrayList<String> linkList = intent.getStringArrayListExtra("linkList");
        updateView(linkList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void updateView(List<String> linkList) {
        listPosition = 0;
        if (linkList != null) {
            listAdapter.clear();
            for (String link : linkList) {
                listAdapter.add(link);
            }
        }
    }
}
