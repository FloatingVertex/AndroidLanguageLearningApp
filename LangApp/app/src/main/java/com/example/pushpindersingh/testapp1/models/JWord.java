package com.example.pushpindersingh.testapp1.models;

import android.util.Log;

import com.example.pushpindersingh.testapp1.DatabaseHelper;

/**
 * Created by Pushpinder Singh on 7/9/2017.
 */

public class JWord {
    public static final String TAG = JWord.class.getSimpleName();

    public static final double showDefCuttoff = -0.1;
    public static final double finishLearningCutoff = 1.0;
    public static final double learnedCuttoff = 2.0;

    public static KnownLevel GetKnownLevel(double known)
    {
        if(known < showDefCuttoff)
        {
            return KnownLevel.UNKNOWN;
        }else if(known < finishLearningCutoff)
        {
            return KnownLevel.LEARNING;
        }else if(known < learnedCuttoff)
        {
            return KnownLevel.REMINDERNEEDED;
        }else{
            return KnownLevel.LEARNED;
        }
    }

    //values goten from database, should not be changed once set
    public long id;
    public long priority;
    public String definition;
    public String romaji;
    public String hiragana = "";
    public String japanese = "";
    public WordType type = WordType.NULL;
    public ConjigationType conjugation = ConjigationType.DICTIONARY;
    public double priorityValue;

    private double known;//ho well this word is understood
    public double getKnown(){return known;}
    public boolean setKnown(double newValue)
    {
        if(newValue < 0.0)
        {
            newValue = 0.0;
        }
        double oldVal = known;
        known = newValue;
        if(GetKnownLevel(oldVal) != GetKnownLevel(newValue)) {
            StaticDBInfo.singleton.WordChangedKnown(oldVal, this);
            return true;
        }else{
            return false;
        }
    }

    public void setKnownPermanent(double newValue)
    {
        setKnownP(newValue);
    }

    private boolean setKnownP(double newValue)
    {
        boolean r = setKnown(newValue);
        DatabaseHelper.singleton.SetWordKnown(this);
        return r;
    }
    private long lastOccurrence;


    public JWord()
    {

    }

    //return true if the status of word changed due to change
    public boolean selectedAsWrongAnswer()
    {
        return setKnownP(known - 0.4);
    }

    //return true if the status of word changed due to change
    public boolean selectedAsCorrectAnswer(int wrongAnswers)
    {
        if(wrongAnswers == 0)
        {
            return setKnownP(known + .5);
        }else if(wrongAnswers == 1){
            return setKnownP(known - 0.8);
        }else{
            return setKnownP(known - 0.5);
        }
    }

    public double GetTotalPriority()
    {
        return (priorityValue - known);
    }

    public void SetVariable(String name, String value)
    {
        if(name.equals("_id"))
        {
            id = Integer.parseInt(value);
        }else if(name.equals("priority")){
            priority = Integer.parseInt(value);
            priorityValue = Math.log10(priority);
        }else if(name.equals("definition")){
            definition = value;
        }else if(name.equals("romaji")){
            romaji = value;
        }else if(name.equals("hiragana")){
            hiragana = value;
        }else if(name.equals("japanese")){
            japanese = value;
        }else if(name.equals("type")){
            if(value.equals("")){
                type = WordType.NULL;
            }else {
                try {
                    type = WordType.valueOf(value.toUpperCase());
                }catch (Exception ex){
                    Log.e(TAG,"Failed to find word type in enum, : " + value.toUpperCase());
                }
            }
        }else if(name.equals("conjugation")){
            if(value.equals("")){
                conjugation = ConjigationType.DICTIONARY;
            }else {
                try {
                    conjugation = ConjigationType.valueOf(value);
                }catch (Exception ex){
                    Log.e(TAG,"Failed to find conjegation type, : " + value.toUpperCase());
                }
            }
        }else{
            Log.e(TAG,"Failed to match varible name to a varible in the JWord class, Var name: " + name +" varible value:" + value);
        }
    }

    public enum WordType{
        NULL,
        VERB,
        ADJECTIVE,
        NOUN,
        PRONOUN,
        ADVERB,
        INTERROGATIVE
    }

    public enum ConjigationType{
        PRESENT,
        DICTIONARY,
    }

    public enum KnownLevel{
        UNKNOWN,
        LEARNING,
        REMINDERNEEDED,
        LEARNED
    }
}
