package com.ccjeng.iwish.controller;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by andycheng on 2016/3/26.
 */
public class Speaker implements TextToSpeech.OnInitListener {

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
            int result = tts.setLanguage(Locale.TRADITIONAL_CHINESE);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                ready = false;
                //Toast.makeText(context, "This Language is not supported", Toast.LENGTH_SHORT).show();
                Log.e("Speaker", "This Language is not supported");
            } else {
                ready = true;
                //Toast.makeText(context, "TextToSpeech.SUCCESS", Toast.LENGTH_SHORT).show();
                Log.d("Speaker", "TextToSpeech.SUCCESS");
            }

        }else{
            ready = false;
            Log.d("Speaker", "TextToSpeech.False");
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
