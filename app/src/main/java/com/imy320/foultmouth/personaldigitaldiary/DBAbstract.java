package com.imy320.foultmouth.personaldigitaldiary;

public class DBAbstract
{
    public static abstract class DB_Diary
    {
        //Database Table Name
        public static final String TABLE_NAME = "T_Diary";
        //Database Variables, where XXX is the variable name and ="xxx" is the column name.
        public static final String ENTRY_TITLE = "entry_title";
        public static final String ENTRY_DATE = "entry_date";
        public static final String ENTRY_TIME = "entry_time";
        public static final String ENTRY_DETAILS = "entry_details";
    }
}
