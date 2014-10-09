package org.magnum.mobilecloud.video.repository;

import javax.persistence.Entity;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * @author brendan
 * 
 */
@Entity
public class Like {
	
	private long videoId = -1;
	
	public Like() {
	}

	public long getVideoId() {
		return videoId;
	}

	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}

}
