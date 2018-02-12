package com.example.pushpindersingh.testapp1.models;

/**
 * Created by Pushpinder Singh on 7/12/2017.
 */

public class Question {

    public QType questionType;
    public JWord releventWord;
    public JWord[] wrongWords = new JWord[3];

    public Question()
    {

    }


    public enum QType{
        NULL,
        WORDDEF,
        WORDDEFQUESTION,

    }
}
