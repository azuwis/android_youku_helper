package name.azuwis.webhelper;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Uri videoUri = Uri.parse("http://host:port/playlist.m3u8");
        //intent.setDataAndType( videoUri, "application/x-mpegURL" );
        //Uri[] uris = {Uri.parse("http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300020200500114277A0D031EA7C7510169B2-429A-23F6-1903-43B3B3EDF4FE?K=8eeaaa9a98a9c83e261c74e0,k2:13fec670333072ccd"), Uri.parse("http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300020201500114277A0D031EA7C7510169B2-429A-23F6-1903-43B3B3EDF4FE?K=39dea8b30170aea7261c74e0,k2:181cbd13a6bb7c00f")};
        //intent.setPackage( "com.mxtech.videoplayer.ad" );
        //intent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
        //intent.setData(Uri.parse("http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300010B004FFF189A3F6B058B92021D3118DA-A0F1-2F6D-56BB-E1E838D3592E?K=8202f626bce0ab022827d6e4,k2:1fe9898c4f4eee3fb"));
        //intent.putExtra("video_list", uris);
        //startActivity( intent );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
