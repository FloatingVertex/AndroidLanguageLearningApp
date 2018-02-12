package com.example.pushpindersingh.testapp1.models;

import android.support.annotation.IntegerRes;
import android.util.Log;

/**
 * Created by Pushpinder Singh on 7/9/2017.
 *
 * represents a collection of words that should be learned together
 */

public class JWordGroup {
    public static final String TAG = JWordGroup.class.getSimpleName();

    public long id;
    public String name;
    public float relation_strength;//0-1 indicating how strongly related the elements of his group are

    public JWordGroup()
    {

    }

    public void SetVariable(String name,String value )
    {
        if(name == "_id")
        {
            id = Integer.parseInt(value);
        }else if(name == "name"){
            this.name = value;
        }else if(name == "relation_strength"){
            relation_strength = ((float)Integer.parseInt(value))/100.0f;
        }else{
            Log.e(TAG,"Failed to match varible name to a varible in the JWord class, Var name: " + name +" varible value:" + value);
        }
    }
}
