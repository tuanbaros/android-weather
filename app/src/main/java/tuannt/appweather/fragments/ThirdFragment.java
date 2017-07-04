package tuannt.appweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import tuannt.appweather.R;
import tuannt.appweather.models.User;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 22/02/2016.
 */
public class ThirdFragment extends Fragment{

    private YouTubePlayer youTubePlayer;

    private boolean isFullScreen;

    public boolean isFullScreen() {
        return isFullScreen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slide_page3, container, false);

        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(Variables.API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    User user = User.getInstance();
//                    player.loadVideo(user.getWeatherVideos()[0].getVideoId());
//                    player.play();
                    youTubePlayer.cueVideo(user.getWeatherVideos()[0].getVideoId());
                    youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                    //Tell the player how to control the change
                    youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            isFullScreen = arg0;
                        }
                    });

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }


        });


        return rootView;
    }

    public void collapsingVideo(){
        youTubePlayer.setFullscreen(false);
    }

    public void onPause() {
        super.onPause();
        if (youTubePlayer != null && youTubePlayer.isPlaying()) youTubePlayer.pause();
    }



}
