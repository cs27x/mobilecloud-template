package org.magnum.videoup.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.client.VideoSvcApi;

import retrofit.RestAdapter;


public class AddVideoActivity extends Activity {
    EditText vidName;
    EditText vidURL;
    EditText vidDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        vidName = (EditText)this.findViewById(R.id.videoName);
        vidURL = (EditText)this.findViewById(R.id.videoURL);
        vidDuration = (EditText)this.findViewById(R.id.videoDuration);
    }

    public void onAddVideoCompleteClick(View v) {
        Video vid = new Video(vidName.getText().toString(), vidURL.getText().toString(),
                Long.parseLong(vidDuration.getText().toString()));
        AddVideoAsyncTask task = new AddVideoAsyncTask();
        task.execute(vid);
    }

    private class AddVideoAsyncTask extends AsyncTask<Video, Void, Void> {
        @Override
        protected Void doInBackground(Video... videos) {
            Video vid = videos[0];
            VideoSvcApi api = new RestAdapter.Builder()
                    .setEndpoint("http://192.168.56.1:8080").setLogLevel(RestAdapter.LogLevel.FULL).build()
                    .create(VideoSvcApi.class);
            api.addVideo(vid);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            finish();
        }
    }
}
