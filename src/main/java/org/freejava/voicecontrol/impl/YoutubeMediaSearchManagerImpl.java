package org.freejava.voicecontrol.impl;

import java.io.IOException;
import java.util.Arrays;

import org.freejava.voicecontrol.MediaSearchManager;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

public class YoutubeMediaSearchManagerImpl implements MediaSearchManager {

    private YouTube youtubev3;

    public YoutubeMediaSearchManagerImpl() throws IOException {

        YouTube.Builder builder = new YouTube.Builder(new NetHttpTransport.Builder().build(), new GsonFactory(),
                GoogleCredential.getApplicationDefault().createScoped(Arrays.asList("https://www.googleapis.com/auth/youtube")));

        this.youtubev3 = builder.build();

    }


    /* (non-Javadoc)
     * @see org.freejava.voicecontrol.IYoutubeManager#searchMediaItems(java.lang.String)
     */
    @Override
    public String searchMediaItems(String keyword) throws IOException {

        YouTube.Search search = youtubev3.search();
        YouTube.Search.List list = search.list("id,snippet").setQ(keyword).setType("video").setMaxResults(1L);
        SearchListResponse response = list.execute();

        return response.getItems().isEmpty() ? null :  response.getItems().get(0).getId().getVideoId();
    }

}
