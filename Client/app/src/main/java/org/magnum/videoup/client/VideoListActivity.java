package org.magnum.videoup.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VideoListActivity extends Activity {

    final static int REQUEST = 1;

	@InjectView(R.id.videoList)
	protected ListView videoList_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);

		ButterKnife.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshVideos();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.video_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, AddVideo.class);
            startActivityForResult(intent, REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void refreshVideos() {
		final VideoSvcApi svc = VideoSvc.getOrShowLogin(this);

		if (svc != null) {
			CallableTask.invoke(new Callable<Collection<Video>>() {

				@Override
				public Collection<Video> call() throws Exception {
					return svc.getVideoList();
				}
			}, new TaskCallback<Collection<Video>>() {

				@Override
				public void success(Collection<Video> result) {
					List<String> names = new ArrayList<String>();
					for (Video v : result) {
						names.add(v.getName());
					}
					videoList_.setAdapter(new ArrayAdapter<String>(
							VideoListActivity.this,
							android.R.layout.simple_list_item_1, names));
				}

				@Override
				public void error(Exception e) {
					Toast.makeText(
							VideoListActivity.this,
							"Unable to fetch the video list, please login again.",
							Toast.LENGTH_SHORT).show();

					startActivity(new Intent(VideoListActivity.this,
							LoginScreenActivity.class));
				}
			});
		}
	}


}
