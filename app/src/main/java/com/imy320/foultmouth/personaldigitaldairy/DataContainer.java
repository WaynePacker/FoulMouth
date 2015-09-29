package com.imy320.foultmouth.personaldigitaldairy;

public class DataContainer
{
    public int day, month, year, hour, minute;
    public String title, bodyText, date, time;

    public DataContainer(int _day, int _month, int _year, int _hour, int _minute, String _title, String _bodyText)
    {
        this.day = _day;
        this.month = _month;
        this.year = _year;

        this.hour = _hour;
        this.minute = _minute;

        this.title = _title;
        this.bodyText = _bodyText;
    }

    public String dateToString()
    {
        return day+"-"+month+"-"+year;
    }

    public String timeToString()
    {
        if(minute < 10)
          return hour + ":0"+minute;

        return hour+":"+minute;
    }
}
