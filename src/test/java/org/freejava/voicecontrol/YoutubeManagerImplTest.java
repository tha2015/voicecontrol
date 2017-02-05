package org.freejava.voicecontrol;

import java.io.IOException;

import org.freejava.voicecontrol.impl.YoutubeMediaSearchManagerImpl;
import org.junit.Assert;
import org.junit.Test;


public class YoutubeManagerImplTest {

    //@Test
    public void test() throws IOException {
        MediaSearchManager h = new YoutubeMediaSearchManagerImpl();
        Assert.assertNotNull(h.searchMediaItems("Happy Birthday"));
    }

}
