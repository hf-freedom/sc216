package com.gym.booking.repository;

import com.gym.booking.domain.BookingRecord;
import com.gym.booking.domain.Course;
import com.gym.booking.domain.Member;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStorage {
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final Map<String, Member> members = new ConcurrentHashMap<>();
    private final Map<String, BookingRecord> bookingRecords = new ConcurrentHashMap<>();

    public Map<String, Course> getCourses() {
        return courses;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    public Map<String, BookingRecord> getBookingRecords() {
        return bookingRecords;
    }
}
