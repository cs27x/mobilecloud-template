package com.magnum.videoup.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.TextureView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.magnum.mobilecloud.video.TestUtils;
import org.magnum.videoup.client.R;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.videoup.client.VideoListActivity;
import org.magnum.videoup.client.VideoSvc;
import org.w3c.dom.Text;

import java.util.Collection;
import java.util.List;

/**
 * This test assumes that you are running integration testing against
 * a local version of the server and using the emulator. The server
 * must already be running before you launch this test. Since different
 * emulators have different addresses to represent the "real" localhost,
 * the test uses a helper method to derive the true localhost address.
 *
 *
 * Created by jules on 10/6/14.
 */
public class AddVideoFormTest extends ActivityInstrumentationTestCase2<VideoListActivity> {

    private VideoSvcApi videoSvc_;

    private AddVideoActivity activity_;

    private TextView videoName_;
    private TextView videoURL_;
    private TextView videoDuration_;
    private Button addVideo_;

    private final Video video_ = TestUtils.randomVideo();

    private int numberVideos_;

    public AddVideoFormTest() {
        super(AddVidoActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        String localhost = TestUtils.findTheRealLocalhostAddress();
        assertNotNull(localhost);

        videoSvc_ = VideoSvc.init("http://"+localhost+":8080", "", "");
        assertNotNull(videoSvc_);

        numberVideos_ = videoSvc_.getVideoList().size();

        activity_ = getActivity();
        assertNotNull(activity_);

        videoName_ = (TextView) activity_.findViewById(R.id.videoName);
        videoURL_ = (TextView) activity_.findViewById(R.id.videoURL);
        videoDuration_ = (TextView) activity_.findViewById(R.id.duration);

        addVideo_ = (Button) activity_.findViewById(R.id.add);
        assertNotNull(videoName_);
        assertNotNull(videoURL_);
        assertNotNull(videoDuration_);

    }

    public void testVideoAddsCorrectly() throws Exception {
        //videoSvc_.addVideo(video_);

        getInstrumentation().callActivityOnResume(activity_);

        // We have to wait to ensure that the network request
        // completes before we check to see if the list is updated.
        //
        // Really...not ideal, indicates that there is a better design out
        // there. However, testing is much better than not testing even
        // with a hack!
        Thread.currentThread().sleep(1000);

        Collection<Video> videos = videoSvc_.getVideoList();
        for(Video v : videos){
            videoSvc_.deleteVideo(v.getId());
        }

        videoName_.setText(video_.getName());
        videoURL_.setText(video_.getUrl());
        videoDuration_.setText(String.valueOf(video_.getDuration()));

        addVideo_.callOnClick();

        getInstrumentation().callActivityOnResume(activity_);

        // Repeat timing hack
        Thread.currentThread().sleep(1000);

        int newCount = videoSvc_.getVideoList().size();

        Collection<Video> newVideo = videoSvc_.findByTitle(video_.getName());

        Thread.currentThread().sleep(1000);

        assertEquals(numberVideos_ + 1, newCount);

        boolean foundVideo = false;
        for (Video v : newVideo) {
            if (v.equals(video_)) {
                foundVideo = true;
            }
        }

        assertTrue(foundVideo);

        videoSvc_.deleteVideo(video_.getId());


    }
}
