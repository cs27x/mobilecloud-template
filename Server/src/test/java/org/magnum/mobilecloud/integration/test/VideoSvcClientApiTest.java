package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.magnum.mobilecloud.video.TestUtils;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

/**
 * 
 * This integration test sends HTTP calls to the server using the Retrofit library.
 * The server must be running before you launch this test. 
 * 
 * @author jules
 *
 */
public class VideoSvcClientApiTest {

	private final String TEST_URL = "http://localhost:8080";

	private VideoSvcApi videoService = new RestAdapter.Builder()
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);

	private Video video = TestUtils.randomVideo();
	
	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoCrud() throws Exception {
		
		// Add the video
		videoService.addVideo(video);
		
		// We should get back the video that we added above
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.size() > 0);
		assertTrue(videos.contains(video));
		
		for(Video v : videos){
			videoService.deleteVideo(v.getId());
		}
		
		videos = videoService.getVideoList();
		assertEquals(0, videos.size());
	}

	
	@Test
	public void testVideoFindByTitle() throws Exception {
		
		// Add the video
		videoService.addVideo(video);
		
		Collection<Video> videos = videoService.findByTitle(video.getName());
		assertTrue(videos.size() > 0);
		for(Video v : videos){
			assertEquals(video.getName(), v.getName());
		}
		
		for(Video v : videos){
			videoService.deleteVideo(v.getId());
		}
	
		assertTrue(videoService.findByTitle(video.getName()).size() == 0);
	}
	
	@Test
	public void testVideoFindByDurationLessThan() throws Exception {
		
		// Add the video
		videoService.addVideo(video);
		
		Collection<Video> videos = videoService.findByDurationLessThan(video.getDuration() + 1);
		assertTrue(videos.size() > 0);
		
		long target = video.getDuration() - 1;
		videos = videoService.findByDurationLessThan(target);
		for(Video v : videos){
			assertTrue(v.getDuration() < target);
		}
		
		videos = videoService.findByDurationLessThan(video.getDuration() + 1);
		for(Video v : videos){
			videoService.deleteVideo(v.getId());
		}
	
		videos = videoService.findByDurationLessThan(video.getDuration() + 1);
		assertTrue(videos.size() == 0);
	}
}
