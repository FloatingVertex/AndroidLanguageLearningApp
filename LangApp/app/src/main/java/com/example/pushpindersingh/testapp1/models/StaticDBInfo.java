package com.example.pushpindersingh.testapp1.models;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.pushpindersingh.testapp1.MCDefinitionQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Pushpinder Singh on 7/10/2017.
 */

public class StaticDBInfo {
    public static final String TAG = StaticDBInfo.class.getSimpleName();
    public static final int numberOfLearningElements = 10;
    public static StaticDBInfo singleton;
    Random rand = new Random();

    Map<String,JWord> wordMap = new HashMap<>();
    public JWord getWord(String romaji){return wordMap.get(romaji);}
    public JWord[] words;
    public JWordGroup[] wordGroups;

    public List<JWord> wordsNeedingReminder = new ArrayList<JWord>();
    public List<JWord> wordsInLearningGroup = new ArrayList<JWord>();

    public Question question = new Question();
    public Question lastQuestion = new Question();
    public long questionIndex;

    public StaticDBInfo(Cursor wordsQuary){
        singleton = this;
        SetWords(wordsQuary);
        //FillLearningGroup();
    }

    public void WordChangedKnown(double oldKnown, JWord word)
    {
        JWord.KnownLevel oldLvl = JWord.GetKnownLevel(oldKnown);
        JWord.KnownLevel newLvl = JWord.GetKnownLevel(word.getKnown());
        if(oldLvl == JWord.KnownLevel.LEARNING && newLvl == JWord.KnownLevel.REMINDERNEEDED)
        {
            wordsInLearningGroup.remove(word);
            FillLearningGroup();
        }else if(oldLvl == JWord.KnownLevel.REMINDERNEEDED)
        {
            wordsNeedingReminder.remove(word);
        }

        if(newLvl == JWord.KnownLevel.LEARNING)
        {
            wordsInLearningGroup.add(word);
        }else if(newLvl == JWord.KnownLevel.REMINDERNEEDED){
            wordsNeedingReminder.add(word);
        }
    }

    public Intent GetNextQuestionPage(Context context)
    {
        int len = wordsInLearningGroup.size();
        lastQuestion = question;
        question = new Question();
        do {
            int randomNum = (int)(rand.nextInt(len));
            question.releventWord = wordsInLearningGroup.get(randomNum);
        }while(question.releventWord == lastQuestion.releventWord);
        Intent intent;
        if(question.releventWord.getKnown() < JWord.showDefCuttoff)
        {
            question.questionType = Question.QType.WORDDEF;
            return null;
            //intent = new Intent(context,)
        }else{
            question.questionType = Question.QType.WORDDEFQUESTION;
            SetWrongAnswers(question);
            intent = new Intent(context, MCDefinitionQ.class);
        }
        return intent;
    }

    protected void SetWrongAnswers(Question question)
    {
        for(int i = 0; i < 3; i++){
            boolean wordSet = false;
            while(!wordSet){
                wordSet = true;

                double rand = Math.random();
                if(rand < Math.min(0.5,((double)wordsNeedingReminder.size())/10.0))
                {
                    question.wrongWords[i] = GetRandomWordFromReminder();
                }else if(rand < 0.8){
                    question.wrongWords[i] = GetRandomWordFromLearning();
                }else{
                    question.wrongWords[i] = GetRandomWord();
                }
                for(int o =0; o < i; o++)
                {
                    if(question.wrongWords[i] == question.wrongWords[o])
                    {
                        wordSet = false;
                    }
                }
                if(question.wrongWords[i] == question.releventWord)
                {
                    wordSet = false;
                }
            }
        }
    }

    protected JWord GetRandomWord2()
    {
        int randomNum = (int)(Math.random() * ((words.length)));
        return words[randomNum];
    }

    public JWord GetRandomWord()
    {
        int len = words.length;
        int randomNum = (int)(Math.random() * ((len)));
        return words[randomNum];
    }

    protected JWord GetRandomWordFromLearning()
    {
        int randomNum = (int)(Math.random() * ((wordsInLearningGroup.size())));
        return wordsInLearningGroup.get(randomNum);
    }

    protected JWord GetRandomWordFromReminder()
    {
        int randomNum = (int)(Math.random() * ((wordsNeedingReminder.size())));
        return wordsNeedingReminder.get(randomNum);
    }

    protected JWord GetRandomKnownWord()
    {
        Log.e(TAG,"not implimented");
        return null;
    }

    public void FillLearningGroup()
    {
        while(wordsInLearningGroup.size() < numberOfLearningElements)
        {
            AddWordToLearning(0.0);
        }
    }

    public void AddWordToLearning(double startingValue)
    {
        for(int i = 0; i < words.length; i++)
        {
            if(words[i].GetTotalPriority() > startingValue)
            {
                boolean isInList = false;
                for(int u = 0; u < wordsInLearningGroup.size(); u++)
                {
                    if(wordsInLearningGroup.get(u) == words[i])
                    {
                        isInList = true;
                    }
                }
                if(!isInList) {
                    wordsInLearningGroup.add(words[i]);
                    return;
                }
            }
        }
        AddWordToLearning(startingValue-.1);
    }

    public void SetWords(Cursor cursor)
    {
        try{
            int count = 0;
            while(cursor.moveToNext())
            {
                count++;
            }
            cursor.moveToFirst();
            words = new JWord[count];

            int index = 0;
            do {
                //create word and store in appropriate index
                words[index] = new JWord();
                //setup variable names
                int colCount = cursor.getColumnCount();
                for(int i = 0; i < colCount; i++)
                {
                    String colName = cursor.getColumnName(i);
                    words[index].SetVariable(colName,cursor.getString(i));
                }
                wordMap.put(words[index].romaji,words[index]);//<Map.Entry<String,JWord>>(new Map.<String,JWord>(words[index].romaji,words[index]));
                index ++;
            }while(cursor.moveToNext());
        }finally {
            if(cursor != null && !cursor.isClosed())
            {
                cursor.close();
            }
        }
    }

    public void SetGroups(Cursor cursor)
    {
        try{
            int count = 0;
            while(cursor.moveToNext())
            {
                count++;
            }
            cursor.moveToFirst();
            wordGroups = new JWordGroup[count];

            int index = 0;
            do {
                //create wordgroups and store in appropriate index
                wordGroups[index] = new JWordGroup();
                //setup the variables
                int colCount = cursor.getColumnCount();
                for(int i = 0; i < colCount; i++)
                {
                    String colName = cursor.getColumnName(i);
                    wordGroups[index].SetVariable(colName,cursor.getString(i));
                }

                index ++;
            }while(cursor.moveToNext());
        }finally {
            if(cursor != null && !cursor.isClosed())
            {
                cursor.close();
            }
        }
    }

    public void ResetStats()
    {
        for(int i = 0; i < words.length; i++)
        {
            words[i].setKnownPermanent(0.0);
        }
    }
}
