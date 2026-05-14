package com.gym.booking.domain;

public class CourseStatistics {
    private String courseId;
    private String courseName;
    private double bookingRate;
    private double checkInRate;
    private double noShowRate;
    private int totalBooked;
    private int totalCheckedIn;
    private int totalNoShow;

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public double getBookingRate() { return bookingRate; }
    public void setBookingRate(double bookingRate) { this.bookingRate = bookingRate; }
    public double getCheckInRate() { return checkInRate; }
    public void setCheckInRate(double checkInRate) { this.checkInRate = checkInRate; }
    public double getNoShowRate() { return noShowRate; }
    public void setNoShowRate(double noShowRate) { this.noShowRate = noShowRate; }
    public int getTotalBooked() { return totalBooked; }
    public void setTotalBooked(int totalBooked) { this.totalBooked = totalBooked; }
    public int getTotalCheckedIn() { return totalCheckedIn; }
    public void setTotalCheckedIn(int totalCheckedIn) { this.totalCheckedIn = totalCheckedIn; }
    public int getTotalNoShow() { return totalNoShow; }
    public void setTotalNoShow(int totalNoShow) { this.totalNoShow = totalNoShow; }
}
