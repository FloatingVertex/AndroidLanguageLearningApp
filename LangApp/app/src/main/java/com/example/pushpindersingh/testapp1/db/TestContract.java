package com.example.pushpindersingh.testapp1.db;

import android.provider.BaseColumns;

/**
 * Created by Pushpinder Singh on 7/9/2017.
 */

public final class TestContract {
    private TestContract(){}

    public static class JWordGroupEntry implements BaseColumns
    {

    }

    public static class JWordRelationEntry implements BaseColumns
    {

    }

    public static class JWordEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "recipe_step";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DEFINITION = "definition";
    }
}
