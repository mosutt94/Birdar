package com.example.birdfinder;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class BirdDataModel implements Parcelable {

    private String name;
    private String date;
    private String amount;
    private String location;
    private String lat;
    private String lon;
    private String year;
    private String month;
    private String day;
    private String time;
    private boolean hasAmount = false;

    protected BirdDataModel(Parcel in) {
        name = in.readString();
        date = in.readString();
        amount = in.readString();
        location = in.readString();
        lat = in.readString();
        lon = in.readString();
        year = in.readString();
        month = in.readString();
        day = in.readString();
        time = in.readString();


        hasAmount = in.readByte() != 0;
    }

    public static final Creator<BirdDataModel> CREATOR = new Creator<BirdDataModel>() {
        @Override
        public BirdDataModel createFromParcel(Parcel in) {
            return new BirdDataModel(in);
        }

        @Override
        public BirdDataModel[] newArray(int size) {
            return new BirdDataModel[size];
        }
    };

    public BirdDataModel() {

    }

    public static BirdDataModel fromJSON(JSONObject jsonObject){
        BirdDataModel birdData = new BirdDataModel();
        try {
            birdData.name = jsonObject.getString("comName");
            birdData.date = jsonObject.getString("obsDt");
            birdData.location = jsonObject.getString("locName");
            birdData.lat = String.valueOf(jsonObject.getDouble("lat"));
            birdData.lon = String.valueOf(jsonObject.getDouble("lng"));
            birdData.year = findYear(birdData.date);
            birdData.month = findMonth(birdData.date);
            birdData.day = findDay(birdData.date);
            birdData.time = findTime(birdData.date);


            for(int i = 0; i < jsonObject.names().length(); i++){
                //Log.d("Heem", "fromJSON: name is " + jsonObject.names().get(i));
                if(jsonObject.names().get(i).equals("howMany")){

                    birdData.hasAmount = true;
                    break;
                }
                jsonObject.keys().next();
            }
            if(birdData.hasAmount == true) {
                birdData.amount = String.valueOf(jsonObject.getInt("howMany"));
            }
            else {
                birdData.amount = "X";
            }

        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return birdData;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getLocation() {
        return location;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getYear() { return year; }

    public String getMonth() { return month; }

    public String getDay() { return day; }

    public String getTime() { return time; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(amount);
        dest.writeString(location);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(day);
        dest.writeString(time);
        dest.writeByte((byte) (hasAmount ? 1 : 0));
    }
    //the find methods for date and time takes substrings of the date String that eBird gives us
    private static String findYear(String date) {
//        int start = 0;
//        int end = 0;
//        for(int i = 0; i < date.length(); i++){
//            if(date.charAt(i) == '-'){
//                end = i;
//                break;
//            }
//        }
        String year = date.substring(0,4);
        return year;
    }

    private static String findMonth(String date) {
        String month = date.substring(5,7);
        return month;
    }

    private static String findDay(String date) {
        String day = date.substring(8,10);
        return day;
    }
    private static String findTime(String date) {
        Log.d("Yankees", "date length " + date.length());
        Log.d("Yankees", "date and time " + date);

        if(date.length() > 10) {
            String time = date.substring(11, date.length());

            int hour = Integer.parseInt(time.substring(0, 2));
            String colonAndMinutes = time.substring(2, time.length());
            if (hour > 12) {
                hour = hour - 12;
                String hourString = String.valueOf(hour);
                return hourString + colonAndMinutes + "pm";
            } else {
                String hourString = String.valueOf(hour);
                return hourString + colonAndMinutes + "am";
            }
        }
        else {
            return null;
        }
    }
}
