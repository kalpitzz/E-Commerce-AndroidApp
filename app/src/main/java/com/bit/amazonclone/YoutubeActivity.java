package com.bit.amazonclone;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public  class YoutubeActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubeView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        youTubeView = findViewById(R.id.playerView);



        mOnInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                    youTubePlayer.loadPlaylist("PLMRKdK25AuPVu7vuwjDm2yobPuZS8hiiO"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };






    }

    @Override
    protected void onStart() {
        super.onStart();
        youTubeView.initialize("AIzaSyDR5xTBn6_YsMFPqp6XQhPBMUVG7z942zE",mOnInitializedListener);

    }
}