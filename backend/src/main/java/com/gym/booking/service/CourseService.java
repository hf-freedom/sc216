package com.gym.booking.service;

import com.gym.booking.domain.*;
import com.gym.booking.dto.CreateCourseRequest;
import com.gym.booking.repository.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private InMemoryStorage storage;

    @Autowired
    private MemberService memberService;

    public Course createCourse(CreateCourseRequest request) {
        Course course = new Course();
        course.setId(UUID.randomUUID().toString());
        course.setName(request.getName());
        course.setCoach(request.getCoach());
        course.setStartTime(request.getStartTime());
        course.setEndTime(request.getEndTime());
        course.setTotalCapacity(request.getTotalCapacity());
        course.setBookedCount(0);
        course.setWaitlistCount(0);
        course.setStatus(CourseStatus.DRAFT);
        storage.getCourses().put(course.getId(), course);
        return course;
    }

    public Course publishCourse(String courseId) {
        Course course = getCourse(courseId);
        if (course.getStatus() != CourseStatus.DRAFT) {
            throw new RuntimeException("只有草稿状态的课程可以发布");
        }
        course.setStatus(CourseStatus.PUBLISHED);
        return course;
    }

    public Course cancelCourse(String courseId) {
        Course course = getCourse(courseId);
        if (course.getStatus() == CourseStatus.COMPLETED || course.getStatus() == CourseStatus.CANCELLED) {
            throw new RuntimeException("课程已完成或已取消");
        }
        course.setStatus(CourseStatus.CANCELLED);
        for (String memberId : course.getBookedMemberIds()) {
            Member member = memberService.getMember(memberId);
            member.getBookedCourseIds().remove(courseId);
        }
        for (String memberId : course.getWaitlistMemberIds()) {
            Member member = memberService.getMember(memberId);
            member.getLockedCourseIds().remove(courseId);
        }
        course.getBookedMemberIds().clear();
        course.getWaitlistMemberIds().clear();
        course.setBookedCount(0);
        course.setWaitlistCount(0);
        return course;
    }

    public Course getCourse(String courseId) {
        Course course = storage.getCourses().get(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        return course;
    }

    public List<Course> getAllCourses() {
        updateCourseStatus();
        return new ArrayList<>(storage.getCourses().values());
    }

    public void updateCourseStatus() {
        LocalDateTime now = LocalDateTime.now();
        for (Course course : storage.getCourses().values()) {
            if (course.getStatus() == CourseStatus.PUBLISHED || course.getStatus() == CourseStatus.FULL) {
                if (now.isAfter(course.getStartTime()) && now.isBefore(course.getEndTime())) {
                    course.setStatus(CourseStatus.IN_PROGRESS);
                } else if (now.isAfter(course.getEndTime())) {
                    course.setStatus(CourseStatus.COMPLETED);
                }
            }
        }
    }

    public List<CourseStatistics> getStatistics() {
        updateCourseStatus();
        List<CourseStatistics> statistics = new ArrayList<>();
        for (Course course : storage.getCourses().values()) {
            if (course.getBookedCount() > 0) {
                CourseStatistics stat = new CourseStatistics();
                stat.setCourseId(course.getId());
                stat.setCourseName(course.getName());
                stat.setTotalBooked(course.getBookedCount());
                stat.setTotalCheckedIn(course.getCheckedInMemberIds().size());
                stat.setTotalNoShow(course.getNoShowMemberIds().size());
                if (course.getTotalCapacity() > 0) {
                    stat.setBookingRate((double) course.getBookedCount() / course.getTotalCapacity() * 100);
                }
                if (course.getBookedCount() > 0) {
                    stat.setCheckInRate((double) course.getCheckedInMemberIds().size() / course.getBookedCount() * 100);
                    stat.setNoShowRate((double) course.getNoShowMemberIds().size() / course.getBookedCount() * 100);
                }
                statistics.add(stat);
            }
        }
        return statistics.stream()
                .sorted((a, b) -> Double.compare(b.getBookingRate(), a.getBookingRate()))
                .collect(Collectors.toList());
    }

    public List<Course> getPopularCourses() {
        updateCourseStatus();
        return storage.getCourses().values().stream()
                .filter(c -> c.getBookedCount() > 0)
                .sorted((a, b) -> Integer.compare(b.getBookedCount(), a.getBookedCount()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
