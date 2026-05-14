package com.gym.booking.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Course {
    private String id;
    private String name;
    private String coach;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalCapacity;
    private int bookedCount;
    private int waitlistCount;
    private CourseStatus status;
    private List<String> bookedMemberIds = new ArrayList<>();
    private Queue<String> waitlistMemberIds = new LinkedList<>();
    private List<String> checkedInMemberIds = new ArrayList<>();
    private List<String> noShowMemberIds = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCoach() { return coach; }
    public void setCoach(String coach) { this.coach = coach; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }
    public int getBookedCount() { return bookedCount; }
    public void setBookedCount(int bookedCount) { this.bookedCount = bookedCount; }
    public int getWaitlistCount() { return waitlistCount; }
    public void setWaitlistCount(int waitlistCount) { this.waitlistCount = waitlistCount; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    public List<String> getBookedMemberIds() { return bookedMemberIds; }
    public void setBookedMemberIds(List<String> bookedMemberIds) { this.bookedMemberIds = bookedMemberIds; }
    public Queue<String> getWaitlistMemberIds() { return waitlistMemberIds; }
    public void setWaitlistMemberIds(Queue<String> waitlistMemberIds) { this.waitlistMemberIds = waitlistMemberIds; }
    public List<String> getCheckedInMemberIds() { return checkedInMemberIds; }
    public void setCheckedInMemberIds(List<String> checkedInMemberIds) { this.checkedInMemberIds = checkedInMemberIds; }
    public List<String> getNoShowMemberIds() { return noShowMemberIds; }
    public void setNoShowMemberIds(List<String> noShowMemberIds) { this.noShowMemberIds = noShowMemberIds; }
}
