package org.magnum.mobilecloud.video.repository.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.magnum.mobilecloud.video.repository.Video;

public class VideoTest {

	private final Video video1_ = new Video("a","http://a", 1000);
	private final Video video2_ = new Video("a","http://a", 1000);
	
	@Test
	public void testEquals() {
		assertEquals(video1_,video2_);
	}
	
	@Test
	public void testHashcode() {
		assertEquals(video1_.hashCode(),video2_.hashCode());
	}
	
}
