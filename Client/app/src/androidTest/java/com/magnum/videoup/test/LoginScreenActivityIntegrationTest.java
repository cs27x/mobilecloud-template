package com.magnum.videoup.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import org.magnum.mobilecloud.video.TestUtils;
import org.magnum.videoup.client.R;

import org.magnum.videoup.client.LoginScreenActivity;
import org.magnum.videoup.client.VideoListActivity;

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
public class LoginScreenActivityIntegrationTest extends ActivityInstrumentationTestCase2 {

    private Button loginButton_;

    private EditText serverEditText_;

    private String localHost_;

    public LoginScreenActivityIntegrationTest() {
        super(LoginScreenActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        localHost_ = TestUtils.findTheRealLocalhostAddress();

        Activity activity = getActivity();
        loginButton_ = (Button)activity.findViewById(R.id.loginButton);
        serverEditText_ = (EditText)activity.findViewById(R.id.server);
    }

    public void testLogin() {

        // An ActivityMonitor is used to check what other Activities are launched.
        // Later, we use this ActivityMonitor to make sure that the LoginScreenActivity
        // launches the VideoListActivity.
        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(VideoListActivity.class.getName(),
                null, false);

        // We can't manipulate UI views from background threads that are running
        // tests. The code in the run() method below will be invoked on the main
        // thread and can manipulate the UI.
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                serverEditText_.setText("http://" + localHost_ + ":8080");
            }
        });

        // Fake a click on the login button
        TouchUtils.clickView(this, loginButton_);

        // Now, we ensure that the LoginScreenActivity sent an Intent
        // and launched the VideoListActivity
        VideoListActivity receiverActivity = (VideoListActivity)
                am.waitForActivityWithTimeout(5000);
        assertNotNull(receiverActivity);
        assertEquals(1, am.getHits());

        // Clean up our mess...
        getInstrumentation().removeMonitor(am);
    }
}
