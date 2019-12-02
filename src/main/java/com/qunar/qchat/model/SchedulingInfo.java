package com.qunar.qchat.model;

public class SchedulingInfo {

    /**
     * scheduling_room_id : scheduling_room_id
     * scheduling_id : scheduling_id
     * mem_action : 1
     * create_time : create_time
     * end_time : end_time
     * scheduling_locale_id : scheduling_locale_id
     * begin_time : begin_time
     * action_remark : action_remark
     * scheduling_intr : scheduling_intr
     * scheduling_appointment : scheduling_appointment
     * remind_flag : 1
     * scheduling_remarks : scheduling_remarks
     * scheduling_room : scheduling_room
     * scheduling_name : scheduling_name
     * canceled : true
     * update_time : update_time
     * schedule_time : schedule_time
     * scheduling_date : scheduling_date
     * member : member
     * inviter : inviter
     * scheduling_locale : scheduling_locale
     */
    private Integer id;
    private String scheduling_room_id;
    private String scheduling_id;
    private String scheduling_type;
    private String mem_action;
    private String create_time;
    private String end_time;
    private String scheduling_locale_id;
    private String begin_time;
    private String action_remark;
    private String scheduling_intr;
    private String scheduling_appointment;
    private String remind_flag;
    private String scheduling_remarks;
    private String scheduling_room;
    private String scheduling_name;
    private boolean canceled;
    private String update_time;
    private String schedule_time;
    private String scheduling_date;
    private String member;
    private String inviter;
    private String scheduling_locale;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setScheduling_type(String scheduling_type) {
        this.scheduling_type = scheduling_type;
    }

    public void setScheduling_room_id(String scheduling_room_id) {
        this.scheduling_room_id = scheduling_room_id;
    }

    public void setScheduling_id(String scheduling_id) {
        this.scheduling_id = scheduling_id;
    }

    public void setMem_action(String mem_action) {
        this.mem_action = mem_action;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setScheduling_locale_id(String scheduling_locale_id) {
        this.scheduling_locale_id = scheduling_locale_id;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public void setAction_remark(String action_remark) {
        this.action_remark = action_remark;
    }

    public void setScheduling_intr(String scheduling_intr) {
        this.scheduling_intr = scheduling_intr;
    }

    public void setScheduling_appointment(String scheduling_appointment) {
        this.scheduling_appointment = scheduling_appointment;
    }

    public void setRemind_flag(String remind_flag) {
        this.remind_flag = remind_flag;
    }

    public void setScheduling_remarks(String scheduling_remarks) {
        this.scheduling_remarks = scheduling_remarks;
    }

    public void setScheduling_room(String scheduling_room) {
        this.scheduling_room = scheduling_room;
    }

    public void setScheduling_name(String scheduling_name) {
        this.scheduling_name = scheduling_name;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public void setScheduling_date(String scheduling_date) {
        this.scheduling_date = scheduling_date;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public void setScheduling_locale(String scheduling_locale) {
        this.scheduling_locale = scheduling_locale;
    }

    public String getScheduling_room_id() {
        return scheduling_room_id;
    }

    public String getScheduling_id() {
        return scheduling_id;
    }

    public String getMem_action() {
        return mem_action;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getScheduling_locale_id() {
        return scheduling_locale_id;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public String getAction_remark() {
        return action_remark;
    }

    public String getScheduling_intr() {
        return scheduling_intr;
    }

    public String getScheduling_appointment() {
        return scheduling_appointment;
    }

    public String getRemind_flag() {
        return remind_flag;
    }

    public String getScheduling_remarks() {
        return scheduling_remarks;
    }

    public String getScheduling_room() {
        return scheduling_room;
    }

    public String getScheduling_name() {
        return scheduling_name;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public String getScheduling_date() {
        return scheduling_date;
    }

    public String getMember() {
        return member;
    }

    public String getInviter() {
        return inviter;
    }

    public String getScheduling_type() {
        return scheduling_type;
    }

    public String getScheduling_locale() {
        return scheduling_locale;
    }

    public Integer getId() {
        return id;
    }
}
