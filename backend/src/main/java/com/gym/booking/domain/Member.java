package com.gym.booking.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private String id;
    private String name;
    private String phone;
    private int noShowCount;
    private LocalDateTime bannedUntil;
    private List<String> lockedCourseIds = new ArrayList<>();
    private List<String> bookedCourseIds = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getNoShowCount() { return noShowCount; }
    public void setNoShowCount(int noShowCount) { this.noShowCount = noShowCount; }
    public LocalDateTime getBannedUntil() { return bannedUntil; }
    public void setBannedUntil(LocalDateTime bannedUntil) { this.bannedUntil = bannedUntil; }
    public List<String> getLockedCourseIds() { return lockedCourseIds; }
    public void setLockedCourseIds(List<String> lockedCourseIds) { this.lockedCourseIds = lockedCourseIds; }
    public List<String> getBookedCourseIds() { return bookedCourseIds; }
    public void setBookedCourseIds(List<String> bookedCourseIds) { this.bookedCourseIds = bookedCourseIds; }
}
