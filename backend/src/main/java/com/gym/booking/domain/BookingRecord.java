package com.gym.booking.domain;

import java.time.LocalDateTime;

public class BookingRecord {
    private String id;
    private String courseId;
    private String memberId;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private LocalDateTime checkInTime;
    private boolean isWaitlist;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    public boolean isWaitlist() { return isWaitlist; }
    public void setWaitlist(boolean waitlist) { isWaitlist = waitlist; }
}
