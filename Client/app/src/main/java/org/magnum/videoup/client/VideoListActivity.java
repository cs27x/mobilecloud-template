package org.magnum.videoup.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Dialog;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class VideoListActivity extends Activity {

    Dialog custom;
    Button savebtn;
    Button canbtn;
    EditText editText_name;
    EditText editText_url;
    EditText editText_duration;


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

    @OnClick(R.id.btn_addNewMovie)
    public void addNewMovie() {
        custom = new Dialog(VideoListActivity.this);
        custom.setContentView(R.layout.dialog_add_movie);

        custom.setTitle("Add New Movie");
        savebtn = (Button)custom.findViewById(R.id.savebtn);
        canbtn = (Button)custom.findViewById(R.id.canbtn);

        editText_name = (EditText)custom.findViewById(R.id.mName);
        editText_url = (EditText)custom.findViewById(R.id.mUrl);
        editText_duration = (EditText)custom.findViewById(R.id.mDuration);

        savebtn.setOnClickListener(new View.OnClickListener() {

            String movieName,movieUrl, movieDuration;

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                movieName = editText_name.getText().toString();
                movieUrl = editText_url.getText().toString();
                movieDuration = editText_duration.getText().toString();

                final Video v = new Video(movieName, movieUrl, Long.parseLong(movieDuration));
                final VideoSvcApi svc = VideoSvc.getOrShowLogin(VideoListActivity.this);
                if (svc != null) {
                    CallableTask.invoke(new Callable<Collection<Video>>() {

                        @Override
                        public Collection<Video> call() throws Exception {
                            svc.addVideo(v);

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

                custom.dismiss();
            }

        });
        canbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                custom.dismiss();

            }
        });
        custom.show();
    }

}
