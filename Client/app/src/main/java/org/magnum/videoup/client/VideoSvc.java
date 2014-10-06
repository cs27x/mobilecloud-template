/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.videoup.client;

import org.magnum.mobilecloud.video.client.VideoSvcApi;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import android.content.Context;
import android.content.Intent;

public class VideoSvc {

	private static VideoSvcApi videoSvc_;

	public static synchronized VideoSvcApi getOrShowLogin(Context ctx) {
		if (videoSvc_ != null) {
			return videoSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized VideoSvcApi init(String server, String user,
			String pass) {

		videoSvc_ =
                new RestAdapter.Builder()
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(VideoSvcApi.class);

		return videoSvc_;
	}
}
