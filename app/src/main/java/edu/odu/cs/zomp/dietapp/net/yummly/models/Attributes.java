package edu.odu.cs.zomp.dietapp.net.yummly.models;

import java.util.List;

public class Attributes {

    private List<String> course = null;
    private List<String> holiday = null;

    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    public List<String> getHoliday() {
        return holiday;
    }

    public void setHoliday(List<String> holiday) {
        this.holiday = holiday;
    }
}