package com.infjay.mice.artifacts;

/**
 * Created by KimJS on 2015-05-25.
 */
public class BusinessCardInfo {
    private String name;
    private String company;

    public BusinessCardInfo()
    {
        name = "";
        company = "";
    }

    public void setName(String str)
    {
        name = str;
    }

    public void setCompany(String str)
    {
        company = str;
    }

    public String getName()
    {
        return name;
    }

    public String getCompany()
    {
        return company;
    }
}