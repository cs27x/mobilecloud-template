package org.magnum.videoup.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;

public class AddVideo extends Activity {
    EditText title;
    EditText duration;
    EditText url;
    final VideoSvcApi svc = VideoSvc.getOrShowLogin(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onPost(View view){

        title = (EditText)findViewById(R.id.title);
        url = (EditText)findViewById(R.id.url);
        duration = (EditText)findViewById(R.id.duration);

        long myDuration;
        try {
            myDuration = Long.parseLong(duration.getText().toString());
        }
        catch (NumberFormatException e){
            System.out.println(duration.toString());
            myDuration = 20;
        }

        Video vid = new Video(title.getText().toString(), url.getText().toString(), myDuration);
        new PostToServer().execute(svc, vid);

        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
