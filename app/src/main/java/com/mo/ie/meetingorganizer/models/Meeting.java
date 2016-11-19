package com.mo.ie.meetingorganizer.models;

import org.joda.time.MutableDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Meeting implements Serializable {


    public long id = -1;
    public MutableDateTime startDateTime, endDateTime; //Joda time  // Java8 java.time android alternative
    public Set<String> contacts = new HashSet<>();
    public String note, title, location;
    public int notification = 0;

    public Meeting() {}

//    public Meeting(long id, MutableDateTime startDateTime, MutableDateTime endDateTime, Set<String> contacts, String note, String title, String location, int notification) {
//        this.id = id;
//        this.startDateTime = startDateTime;
//        this.endDateTime = endDateTime;
//        this.contacts = contacts;
//        this.note = note;
//        this.title = title;
//        this.location = location;
//        this.notification = notification;
//    }


    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", contacts=" + contacts +
                ", note='" + note + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", notification=" + notification +
                '}';
    }
}
