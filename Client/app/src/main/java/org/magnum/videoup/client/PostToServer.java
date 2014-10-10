package org.magnum.videoup.client;

import android.os.AsyncTask;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;

/**
 * Created by walton on 10/10/14.
 */
public class PostToServer extends AsyncTask<Object, Void, Integer> {
    protected Integer doInBackground(Object... params){
        try {
            VideoSvcApi svc = (VideoSvcApi) params[0];
            Video vid = (Video) params[1];

            svc.addVideo(vid);
            return 1;
        }
        catch (Exception e){
            return null;
        }
    }



}
