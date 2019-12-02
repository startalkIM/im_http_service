package com.qunar.qchat.model;

import java.util.List;

public class SchedulingJson {

    /**
     * tripIntr : 我想开个会
     * tripRemark : 可以写点自己对此会的备注
     * scheduleTime : 2018-8-3 10:00:00
     * operateType : 2
     * updateTime : 111
     * appointment : 预约会面
     * tripId : 12345
     * tripRoom : 酸梅汤
     * tripLocaleNumber : 1
     * tripLocale : 维亚大厦
     * tripType : 1
     * tripInviter : hubin.hu@ejabhost1
     * memberList : [{"memberName":"刘帆","memberId":"liufan.liu@ejabhost1"}]
     * tripRoomNumber : 1
     * tripDate : 2018-8-3
     * tripName : RN大神邀请会
     * beginTime : 2018-8-3 14:00:00
     * endTime : 2018-8-3 15:00:00
     */
    private String tripIntr;
    private String tripRemark;
    private String scheduleTime;
    private String operateType;
    private String updateTime;
    private String appointment;
    private String tripId;
    private String tripRoom;
    private String tripLocaleNumber;
    private String tripLocale;
    private String tripType;
    private String tripInviter;
    private List<MemberListEntity> memberList;
    private String tripRoomNumber;
    private String tripDate;
    private String tripName;
    private String beginTime;
    private String endTime;
    private boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setTripIntr(String tripIntr) {
        this.tripIntr = tripIntr;
    }

    public void setTripRemark(String tripRemark) {
        this.tripRemark = tripRemark;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setTripRoom(String tripRoom) {
        this.tripRoom = tripRoom;
    }

    public void setTripLocaleNumber(String tripLocaleNumber) {
        this.tripLocaleNumber = tripLocaleNumber;
    }

    public void setTripLocale(String tripLocale) {
        this.tripLocale = tripLocale;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public void setTripInviter(String tripInviter) {
        this.tripInviter = tripInviter;
    }

    public void setMemberList(List<MemberListEntity> memberList) {
        this.memberList = memberList;
    }

    public void setTripRoomNumber(String tripRoomNumber) {
        this.tripRoomNumber = tripRoomNumber;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTripIntr() {
        return tripIntr;
    }

    public String getTripRemark() {
        return tripRemark;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public String getOperateType() {
        return operateType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getAppointment() {
        return appointment;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTripRoom() {
        return tripRoom;
    }

    public String getTripLocaleNumber() {
        return tripLocaleNumber;
    }

    public String getTripLocale() {
        return tripLocale;
    }

    public String getTripType() {
        return tripType;
    }

    public String getTripInviter() {
        return tripInviter;
    }

    public List<MemberListEntity> getMemberList() {
        return memberList;
    }

    public String getTripRoomNumber() {
        return tripRoomNumber;
    }

    public String getTripDate() {
        return tripDate;
    }

    public String getTripName() {
        return tripName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public static class MemberListEntity {
        /**
         * memberId : liufan.liu@ejabhost1
         * memberState: 1
         * memberStateDescribe: xxxxx
         */
        private String memberId;
        private String memberState;
        private String memberStateDescribe;

        public String getMemberState() {
            return memberState;
        }

        public void setMemberState(String memberState) {
            this.memberState = memberState;
        }

        public String getMemberStateDescribe() {
            return memberStateDescribe;
        }

        public void setMemberStateDescribe(String memberStateDescribe) {
            this.memberStateDescribe = memberStateDescribe;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberId() {
            return memberId;
        }
    }
}
