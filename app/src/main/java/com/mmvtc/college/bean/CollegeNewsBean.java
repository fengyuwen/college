package com.mmvtc.college.bean;

/**
 * Created by Administrator on 2018/10/21.
 */

public class CollegeNewsBean {
    private String title;
    private String titleValue;
    private String time;
    private String eyeMeasure;
    private String text;

    public String getEyeMeasure() {
        return eyeMeasure;
    }

    public void setEyeMeasure(String eyeMeasure) {
        this.eyeMeasure = eyeMeasure;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleValue() {
        return titleValue;
    }

    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String textValue;

}
