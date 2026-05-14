package com.gym.booking.dto;

import java.time.LocalDateTime;

public class CreateCourseRequest {
    private String name;
    private String coach;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalCapacity;

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
}
