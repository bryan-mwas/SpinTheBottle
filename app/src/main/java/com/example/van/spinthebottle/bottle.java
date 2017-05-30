package com.example.van.spinthebottle;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Random;

public class bottle extends AppCompatActivity {
    //spin the bottle code
    ImageView ic_bottle;
    Button spinb;
    Random random;
    int angle;
    boolean restart = false;

    //audio code
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);

        //SPIN AND STOP AT A RANDOM POSITION
        random = new Random();

        ic_bottle = (ImageView) findViewById(R.id.ic_bottle);

        spinb = (Button) findViewById(R.id.spinb);
        spinb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BOTTLE SPIN
                if (restart) {
                    int temp = angle % 360;
                    RotateAnimation rotateAnimation = new RotateAnimation(temp, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setDuration(1000);
                    rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                    ic_bottle.startAnimation(rotateAnimation);
                    spinb.setText("SPIN");

                    restart = false;
                } else {
                    angle = random.nextInt(3400) + 360;
                    RotateAnimation rotateAnimation = new RotateAnimation(0, angle, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setDuration(1000);
                    rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                    ic_bottle.startAnimation(rotateAnimation);

                    restart = true;
                    spinb.setText("RESET");
                }
            }
        });

        handler = new Handler();
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lovesick);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mEDIAp) {
                seekBar.setMax(mediaPlayer.getDuration());
                playCycle();
                mediaPlayer.start();
            }
        });
        /**
         * Seekbar
         * */
        //seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){});
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void playCycle() {//tracks the current point the song is at
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    protected void onResume() {//mediaplayer starts playing
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {//mediaplayer stops playing
        super.onPause();
        //mediaPlayer.stop();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        handler.removeCallbacks(runnable);
    }
}
