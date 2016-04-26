package com.ccjeng.iwish.controller;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by andycheng on 2016/3/26.
 */
public class Speaker implements TextToSpeech.OnInitListener {

    private static final String TAG = Speaker.class.getSimpleName();

    private TextToSpeech tts;
    private boolean ready = false;

    private boolean allowed = false;
    private Context context;

    public Speaker(Context context){
        tts = new TextToSpeech(context, this);
        this.context = context;
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            // Change this to match your
            // locale
            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                ready = false;
                Log.e(TAG, "This Language is not supported");
            } else {
                ready = true;
                Log.d(TAG, "TextToSpeech.SUCCESS");
            }

        }else{
            ready = false;
            Log.d(TAG, "TextToSpeech.False");
        }
    }

    public void speak(String text) {

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if (ready && allowed) {
            HashMap<String, String> hash = new HashMap<String, String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void pause(int duration){
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
        //tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
    }

    // Free up resources
    public void destroy(){
        tts.shutdown();
    }
}
