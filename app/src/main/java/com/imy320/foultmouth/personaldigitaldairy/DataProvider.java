package com.imy320.foultmouth.personaldigitaldairy;


import java.util.StringTokenizer;

public class DataProvider
{
    private String entryTitle;
    private String entryDate;
    private String entryTime;
    private String entryDetails;

    public DataProvider(String _entryTitle, String _entryDate, String _entryTime, String _entryDetails)
    {
        entryTitle = _entryTitle;
        entryDate = _entryDate;
        entryTime = _entryTime;
        entryDetails = _entryDetails;
    }

    public String getEntryTitle()
    {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle)
    {
        this.entryTitle = entryTitle;
    }

    public String getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(String entryDate)
    {
        this.entryDate = entryDate;
    }

    public String getEntryTime()
    {
        return entryTime;
    }

    public void setEntryTime(String entryTime)
    {
        this.entryTime = entryTime;
    }

    public String getEntryDetails()
    {
        return entryDetails;
    }

    public void setEntryDetails(String entryDetails)
    {
        this.entryDetails = entryDetails;
    }
}
