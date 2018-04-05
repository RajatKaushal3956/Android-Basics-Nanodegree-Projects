package com.example.god.habitapp.data;


import android.provider.BaseColumns;

public class HabitContract {
    public HabitContract(){}
    public static final class HabitEntry implements BaseColumns {
        public final static String TABLE_NAME="Habits";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_NUMBER_OF_TIMES="NUMBER_OF_TIMES";

    }
}
