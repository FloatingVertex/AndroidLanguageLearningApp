package com.example.pushpindersingh.testapp1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.pushpindersingh.testapp1.models.StaticDBInfo;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.dbtest)
    TextView testText;
    @BindView(R.id.showRomaji)
    CheckBox showRomaji;
    @BindView(R.id.showHiragana)
    CheckBox showHiragana;

    @OnClick(R.id.btn_questions)
    public void onButtonClicked()
    {
        Intent intent = StaticDBInfo.singleton.GetNextQuestionPage(this);
        startActivity(intent);
    }

    @OnClick(R.id.wordslist)
    public void onBtnWordsListClicked()
    {
        Intent intent = new Intent(this,ExtraInfo.class);
        startActivity(intent);
    }

    @OnClick(R.id.resetdata)
    public void onBtnResetData()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        StaticDBInfo.singleton.ResetStats();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @OnCheckedChanged(R.id.showRomaji)
    public void onShowRomajiChecked()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(showRomaji.isChecked()) {
            editor.putString("showRomaji", "True");
        }else{
            editor.putString("showRomaji", "False");
        }
        editor.apply();
    }

    @OnCheckedChanged(R.id.showHiragana)
    public void onShowHiraganChecked()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(showHiragana.isChecked()) {
            editor.putString("showHiragana", "True");
        }else{
            editor.putString("showHiragana", "False");
        }
        editor.apply();
        if(showHiragana.isChecked())
        {
            testText.setText("Hiragana Checked");
        }else{
            testText.setText("Hiragana Unchecked");
        }
    }

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        db = new DatabaseHelper(this);
        db.openDataBase();
        db.SetupStaticDBInfo();
        db.close();
        db.openUserDataBase();
        db.LoadUserDatabase();
        //db.closeUserDatabase();
        StaticDBInfo.singleton.FillLearningGroup();
    }

    @Override
    protected  void onResume()
    {
        super.onResume();

        testText.setText(StaticDBInfo.singleton.GetRandomWord().romaji);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String showRomajiStr = sharedPref.getString("showRomaji","True");
        showRomaji.setChecked(showRomajiStr.equals("True"));
        String showHiraganaStr = sharedPref.getString("showHiragana","True");
        showHiragana.setChecked(showHiraganaStr.equals("True"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
