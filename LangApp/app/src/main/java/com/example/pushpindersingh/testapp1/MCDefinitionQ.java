package com.example.pushpindersingh.testapp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pushpindersingh.testapp1.models.JWord;
import com.example.pushpindersingh.testapp1.models.Question;
import com.example.pushpindersingh.testapp1.models.StaticDBInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MCDefinitionQ extends AppCompatActivity {
    public static final String TAG = MCDefinitionQ.class.getSimpleName();

    @BindView(R.id.progressBar2)
    ProgressBar bar;

    @BindView(R.id.hiragana)
    TextView hiragana;

    @BindView(R.id.romaji)
    TextView romanji;

    @BindView(R.id.japanese)
    TextView japanese;

    @BindView(R.id.c1)
    Button c1;
    @OnClick(R.id.c1)
    public void C1Pressed(){
        ButtonPressed(0);
    }
    @BindView(R.id.c2)
    Button c2;
    @OnClick(R.id.c2)
    public void C2Pressed(){
        ButtonPressed(1);
    }
    @BindView(R.id.c3)
    Button c3;
    @OnClick(R.id.c3)
    public void C3Pressed(){
        ButtonPressed(2);
    }
    @BindView(R.id.c4)
    Button c4;
    @OnClick(R.id.c4)
    public void C4Pressed(){
        ButtonPressed(3);
    }

    Button[] btnChoices = new Button[4];

    private int correctAnswerIndex;
    Question question;

    JWord[] choices = new JWord[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcdefinition_q);

        ButterKnife.bind(this);

        question = StaticDBInfo.singleton.question;
        if(question.questionType != Question.QType.WORDDEFQUESTION)
        {
            Log.e(TAG,"Loaded mc definition Q activity for wrong type of question, Q type: " + question.questionType.toString());
        }
        correctAnswerIndex = (int)(Math.random() * ((4)));
        btnChoices[0] = c1;
        btnChoices[1] = c2;
        btnChoices[2] = c3;
        btnChoices[3] = c4;
        SetupChoices();
    }


    protected boolean[] buttonSelected = new boolean[4];
    protected void ButtonPressed(int index)
    {
        for(int i = 0; i < 4; i++)
        {
            //btnChoices[i].setAlpha(0.4f);
            //btnChoices[i].setBackgroundColor(0x);
        }
        if(buttonSelected[correctAnswerIndex])
        {
            if(index == correctAnswerIndex)
            {
                Intent intent = StaticDBInfo.singleton.GetNextQuestionPage(this);
                startActivity(intent);
            }
        }else {
            if (index == correctAnswerIndex) {
                int wrongAnswers = 0;
                for(int o = 0; o < 4; o++)
                {
                    if(buttonSelected[o])
                    {
                        wrongAnswers++;
                    }
                }
                btnChoices[index].setBackgroundColor(Color.GREEN);
                choices[correctAnswerIndex].selectedAsCorrectAnswer(wrongAnswers);
                buttonSelected[index] = true;
            } else {
                if(!buttonSelected[index]) {
                    btnChoices[index].setBackgroundColor(Color.RED);
                    choices[index].selectedAsWrongAnswer();
                    buttonSelected[index] = true;
                }
            }
        }
    }

    protected void SetupChoices()
    {
        int c = 0;
        for(int i = 0; i < 4; i++)
        {
            if(correctAnswerIndex == i)
            {
                choices[i] = question.releventWord;
            }else{
                choices[i] = question.wrongWords[c];
                c++;
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hiragana.setText(question.releventWord.hiragana);
        romanji.setText(question.releventWord.romaji);
        japanese.setText(question.releventWord.japanese);//Float.toString((float)choices[correctAnswerIndex].getKnown()));
        for(int i = 0; i < 4; i++)
        {
            btnChoices[i].setText(choices[i].definition);
        }
        DatabaseHelper.singleton.openUserDataBase();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // hide/show hirignana/romaji text beased on settings
        String showRomajiStr = sharedPref.getString("showRomaji","True");
        boolean showRomaji = showRomajiStr.equals("True");
        String showHiraganaStr = sharedPref.getString("showHiragana","False");
        boolean showHiragana = showHiraganaStr.equals("True");
        if(showRomaji)
        {
            romanji.setVisibility(View.VISIBLE);
        }else
        {
            romanji.setVisibility(View.GONE);
        }
        if(showHiragana)
        {
            hiragana.setVisibility(View.VISIBLE);
        }else
        {
            hiragana.setVisibility(View.GONE);
        }
        bar.setProgress(40);
        bar.setSecondaryProgress(80);
        bar.setScaleY(10f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper.singleton.closeUserDatabase();
    }
}
