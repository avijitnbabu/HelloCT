package com.w3xplorers.helloct;

/**
 * Created by Avijit on 10/9/2017.
 */

public class ReportClass {
    private String crimeReport;
    private String crime_comments;
    private String replied_by;
    private String replied_date;

    public String getCrimeReport() {
        return crimeReport;
    }

    public void setCrimeReport(String crimeReport) {
        this.crimeReport = crimeReport;
    }

    public String getCrime_comments() {
        return crime_comments;
    }

    public void setCrime_comments(String crime_comments) {
        this.crime_comments = crime_comments;
    }

    public String getReplied_by() {
        return replied_by;
    }

    public void setReplied_by(String replied_by) {
        this.replied_by = replied_by;
    }

    public String getReplied_date() {
        return replied_date;
    }

    public void setReplied_date(String replied_date) {
        this.replied_date = replied_date;
    }
}
