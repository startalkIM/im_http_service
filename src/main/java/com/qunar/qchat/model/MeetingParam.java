package com.qunar.qchat.model;

public class MeetingParam {

    /**
     * date : 2018-08-15
     * bookerQtalk : xing.zhou
     * conferenceName : 测试
     * meetingUsers : mingliang.gao,lee.guo,
     * reserveUser : mingliang.gao
     * description : 测试
     * startTime : 16:30
     * endTime : 17:00
     * type : I
     * roomId : 148
     * entryId : 115435
     */
    private String date;
    private String bookerQtalk;
    private String conferenceName;
    private String meetingUsers;
    private String reserveUser;
    private String description;
    private String startTime;
    private String endTime;
    private String type;
    private int roomId;
    private int entryId;

    public void setDate(String date) {
        this.date = date;
    }

    public void setBookerQtalk(String bookerQtalk) {
        this.bookerQtalk = bookerQtalk;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public void setMeetingUsers(String meetingUsers) {
        this.meetingUsers = meetingUsers;
    }

    public void setReserveUser(String reserveUser) {
        this.reserveUser = reserveUser;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getDate() {
        return date;
    }

    public String getBookerQtalk() {
        return bookerQtalk;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public String getMeetingUsers() {
        return meetingUsers;
    }

    public String getReserveUser() {
        return reserveUser;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getType() {
        return type;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getEntryId() {
        return entryId;
    }
}
