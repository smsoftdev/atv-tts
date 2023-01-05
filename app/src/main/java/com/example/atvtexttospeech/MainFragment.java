package com.example.atvtexttospeech;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MainFragment extends Fragment {

    String TAG = "TTS Fragment";
    TextView textEditToSpeech;
    Button speechButton;

    // 텍스트 to 스피치 개체 참조
    TextToSpeech textToSpeech;

    public MainFragment() {
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // onCreate에서 TextToSpeech 개체를 생성한다.
            // 개체 초기화가 성공하면 TextToSpeech.OnInitListener인터페이스의 onInit(int status) 가 호출 된다.
            textToSpeech = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    // status 는 실패와 성공 2가지를 전달 된다.
                    switch (status) {
                        case TextToSpeech.ERROR:
                            // 실패시 처리
                            Log.i(TAG, "onInit: error");
                            speechButton.setEnabled(false);
                            speechButton.setText("준비 실패");
                            break;
                        case TextToSpeech.SUCCESS:
                            // 성공시 처리
                            // 성공시에는 한국어 지원을 하도록 하고, 한국어 지원이 되지 않을 시 오류 처리를 한다.
                            Log.i(TAG, "success");
                            int ttsLang = textToSpeech.setLanguage(Locale.KOREA);
                            if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e(TAG, "onInit: The language not supported!");
                                speechButton.setEnabled(false);
                                speechButton.setText("한국어 지원하지 않음");
                            }
                            else {
                                Log.i(TAG, "onInit: Language supported");
                                speechButton.setEnabled(true);
                                speechButton.setText("말하기");
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textEditToSpeech = (TextView)view.findViewById(R.id.text_edit_to_speech);
        speechButton = (Button)view.findViewById(R.id.speech_button);
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "speakNow [" + textEditToSpeech.getText().toString() + "]");
                // 발화를 한다.
                textToSpeech.speak(textEditToSpeech.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "abc");
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

}