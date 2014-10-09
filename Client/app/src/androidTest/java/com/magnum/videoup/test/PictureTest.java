package com.magnum.videoup.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import org.magnum.videoup.client.LoginScreenActivity;
import org.magnum.videoup.client.R;

import java.io.IOException;
import java.net.URL;

/**
 * Created by pauljs on 10/9/2014.
 */
public class PictureTest extends ActivityInstrumentationTestCase2 {

    public PictureTest() {
        super(LoginScreenActivity.class);
    }

    public void testLogo() throws Exception {
        try {
            getActivity().getResources().getDrawable(R.drawable.penguin);
        } catch(Exception e) {
            fail("No file found");
        }
    }
}
