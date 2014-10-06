package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Created by jules on 10/6/14.
 */
public class TestUtils {

    private static final String[] POSSIBLE_LOCALHOSTS = {
            "10.0.2.2", // Android Emulator
            "192.168.56.1" // Genymotion
    };

    public static String findTheRealLocalhostAddress() {
        String realLocalHost = null;

        for(String localhost : POSSIBLE_LOCALHOSTS) {
            try {
                URL url = new URL("http://"+localhost+":8080");
                URLConnection con = url.openConnection();
                con.setConnectTimeout(500);
                con.setReadTimeout(500);
                InputStream in = con.getInputStream();
                if (in != null){
                    realLocalHost = localhost;
                    in.close();
                    break;
                }
            } catch (Exception e) {}
        }

        return realLocalHost;
    }

    public static long randomVideoDuration(){
        return (long)Math.rint(Math.random() * 1000);
    }

    public static Video randomVideo() {
        return new Video(
                randomVideoName(),
                "http://" + randomVideoName(),
                randomVideoDuration());
    }

    public static String randomVideoName() {
        return UUID.randomUUID().toString();
    }

}

