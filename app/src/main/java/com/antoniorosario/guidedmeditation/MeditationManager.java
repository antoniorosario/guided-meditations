package com.antoniorosario.guidedmeditation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.antoniorosario.guidedmeditation.model.Meditation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MeditationManager {

    private static final String TAG = "MeditationManager";
    private static final String MEDITATIONS_FOLDER = "guided_meditations";

    private AssetManager assets;
    private List<Meditation> meditations = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                release();
            }
        }
    };
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            release();
        }
    };

    public MeditationManager(Context context) {
        assets = context.getAssets();
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        loadMeditations();
    }

    public void play(Meditation meditation) {

        int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            try {
                AssetFileDescriptor afd = assets.openFd(meditation.getAssetPath());
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(completionListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    private void loadMeditations() {
        String[] meditationNames;
        try {
            meditationNames = assets.list(MEDITATIONS_FOLDER);
            Log.i(TAG, "Found " + meditationNames.length + " meditations");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }
        for (String fileName : meditationNames) {
            String assetPath = MEDITATIONS_FOLDER + "/" + fileName;
            Meditation meditation = new Meditation(assetPath);
            meditations.add(meditation);
        }
    }

    public List<Meditation> getMeditations() {
        return meditations;
    }
}
