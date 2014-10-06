package com.magnum.videoup.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.magnum.mobilecloud.video.TestUtils;
import org.magnum.videoup.client.R;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.videoup.client.VideoListActivity;
import org.magnum.videoup.client.VideoSvc;

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
public class VideoListActivityTest extends ActivityInstrumentationTestCase2<VideoListActivity> {

    private VideoSvcApi videoSvc_;

    private VideoListActivity activity_;

    private ListView videoList_;

    private final Video video_ = TestUtils.randomVideo();

    public VideoListActivityTest() {
        super(VideoListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        String localhost = TestUtils.findTheRealLocalhostAddress();
        assertNotNull(localhost);

        videoSvc_ = VideoSvc.init("http://"+localhost+":8080", "", "");
        assertNotNull(videoSvc_);

        activity_ = getActivity();
        assertNotNull(activity_);

        videoList_ = (ListView)activity_.findViewById(R.id.videoList);
        assertNotNull(videoList_);
    }

    public void testVideoListIsPopulatedCorrectly() throws Exception {
        videoSvc_.addVideo(video_);

        getInstrumentation().callActivityOnResume(activity_);

        // We have to wait to ensure that the network request
        // completes before we check to see if the list is updated.
        //
        // Really...not ideal, indicates that there is a better design out
        // there. However, testing is much better than not testing even
        // with a hack!
        Thread.currentThread().sleep(1000);

        ListAdapter videoAdapter = videoList_.getAdapter();

        boolean foundExpectedVideo = false;
        for(int i = 0; i < videoAdapter.getCount(); i++){
            assertTrue(videoAdapter.getItem(i) instanceof String);

            String name = (String)videoAdapter.getItem(i);
            foundExpectedVideo = foundExpectedVideo || video_.getName().equals(name);
        }

        assertTrue(foundExpectedVideo);

        Collection<Video> videos = videoSvc_.getVideoList();
        for(Video v : videos){
            videoSvc_.deleteVideo(v.getId());
        }

        getInstrumentation().callActivityOnResume(activity_);

        // Repeat timing hack
        Thread.currentThread().sleep(1000);

        videoAdapter = videoList_.getAdapter();
        assertEquals(0, videoAdapter.getCount());
    }
}
