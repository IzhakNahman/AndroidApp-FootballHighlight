package com.nahman.footballhighlights.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Highlight implements Parcelable {

    private String competition;
    private String title;
    private String embed;
    private String team1;
    private String team2;
    private String competitionName;
    private String countryName;
    private String imageUrl;
    private String date;

    public Highlight(String title, String embed, String[] teams, String competition, String imageUrl, String date) {
        this.title = title;
        this.embed = embed;
        this.team1 = teams[0];
        this.team2 = teams[1];

        this.competition = competition;
        String [] competitionArray = competition.split(": ");
        this.competitionName = competitionArray[1];
        if (competitionArray[0].contains("EUROPA LEAGUE") || competitionArray[0].contains("CHAMPIONS LEAGUE")){
            String newName = competition.toLowerCase();

            this.competitionName =  newName.substring(0, 1).toUpperCase() + newName.substring(1);
        }
        this.countryName = competitionArray[0];
        this.imageUrl = imageUrl;
        this.date = convertDate(date);

    }

    public String getCompetition() {
        if (competitionName.toUpperCase().contains("EUROPA LEAGUE") || competitionName.toUpperCase().contains("CHAMPIONS LEAGUE")){
            return competitionName;
        }
            return this.countryName + " : " + competitionName;
    }

    private String convertDate(String date){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date d = null;
        try
        {
            d = input.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return date;
        }
        return output.format(d);
    }

    public String getTitle() {
        return title;
    }

    public String getEmbed() {
        return embed;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Highlight{" +
                "title='" + title + '\'' +
                ", embed='" + embed + '\'' +
                ", team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", competitionName='" + competitionName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.embed);
        dest.writeString(this.team1);
        dest.writeString(this.team2);
        dest.writeString(this.competitionName);
        dest.writeString(this.countryName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.date);
    }

    protected Highlight(Parcel in) {
        this.title = in.readString();
        this.embed = in.readString();
        this.team1 = in.readString();
        this.team2 = in.readString();
        this.competitionName = in.readString();
        this.countryName = in.readString();
        this.imageUrl = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Highlight> CREATOR = new Parcelable.Creator<Highlight>() {
        @Override
        public Highlight createFromParcel(Parcel source) {
            return new Highlight(source);
        }

        @Override
        public Highlight[] newArray(int size) {
            return new Highlight[size];
        }
    };


    public static class HighlightLeaguesComparator implements Comparator<Highlight> {

        @Override
        public int compare(Highlight o1, Highlight o2) {
            return o1.getCompetitionName().compareTo(o2.getCompetitionName());
        }
    }

    public static class HighlightTeamsComparator implements Comparator<Highlight> {

        @Override
        public int compare(Highlight o1, Highlight o2) {
            return o2.getTeam1().compareTo(o1.getTeam1());
        }
    }

    public static class HighlightDateComparator implements Comparator<Highlight> {

        @Override
        public int compare(Highlight o2, Highlight o1) {

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date date = format.parse(o1.getDate());
                Date date2 = format.parse(o2.getDate());
                return date.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return o1.getDate().compareTo(o2.getDate());

        }
    }
}
