package com.example.myapplication;

public class ModelData {
    String Date,Audio_name,Duration;

    public String getAudio_name() {
        return Audio_name;
    }

    public void setAudio_name(String audio_name) {
        Audio_name = audio_name;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public ModelData(String date, String audio_name, String duration) {
        Date = date;
        Audio_name = audio_name;
        Duration = duration;
    }

    public ModelData() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
