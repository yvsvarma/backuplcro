package com.oracle.hpcm.restclient;

public class ProfitabilityRestResponse
{
    private String details;

    private String status;

    private String type;

    private String statusMessage;

    public String getDetails ()
    {
        return details;
    }

    public void setDetails (String details)
    {
        this.details = details;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

   
    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getStatusMessage ()
    {
        return statusMessage;
    }

    public void setStatusMessage (String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [details = "+details+", status = "+status+", type = "+type+", statusMessage = "+statusMessage+"]";
    }
}