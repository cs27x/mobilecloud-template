package org.magnum.mobilecloud.video.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * @author jules
 * 
 */
@Entity
public class Like {



    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long videoId = -1;


	public Like() {
	}

	public Like(long videoId) {
		super();
		this.videoId = videoId;
	}

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long id) {
        videoId = id;
    }


}
