package com.gym.booking.service;

import com.gym.booking.domain.*;
import com.gym.booking.repository.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private InMemoryStorage storage;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MemberService memberService;

    public BookingRecord lockSeat(String courseId, String memberId) {
        if (memberService.isBanned(memberId)) {
            throw new RuntimeException("您因爽约次数过多已被限制预约");
        }

        Course course = courseService.getCourse(courseId);
        if (course.getStatus() != CourseStatus.PUBLISHED && course.getStatus() != CourseStatus.FULL) {
            throw new RuntimeException("课程不可预约");
        }

        if (course.getBookedMemberIds().contains(memberId) || course.getWaitlistMemberIds().contains(memberId)) {
            throw new RuntimeException("您已预约或已在候补队列中");
        }

        boolean hasAvailableSeat = course.getBookedCount() < course.getTotalCapacity();

        if (hasAvailableSeat) {
            course.setBookedCount(course.getBookedCount() + 1);
            course.getBookedMemberIds().add(memberId);
            if (course.getBookedCount() >= course.getTotalCapacity()) {
                course.setStatus(CourseStatus.FULL);
            }
        } else {
            course.setWaitlistCount(course.getWaitlistCount() + 1);
            course.getWaitlistMemberIds().add(memberId);
        }

        Member member = memberService.getMember(memberId);
        member.getLockedCourseIds().add(courseId);

        BookingRecord record = new BookingRecord();
        record.setId(UUID.randomUUID().toString());
        record.setCourseId(courseId);
        record.setMemberId(memberId);
        record.setStatus(hasAvailableSeat ? BookingStatus.LOCKED : BookingStatus.WAITLIST);
        record.setBookingTime(LocalDateTime.now());
        record.setWaitlist(!hasAvailableSeat);
        storage.getBookingRecords().put(record.getId(), record);

        return record;
    }

    public BookingRecord confirmBooking(String bookingId) {
        BookingRecord record = getBookingRecord(bookingId);
        if (record.getStatus() != BookingStatus.LOCKED) {
            throw new RuntimeException("只有锁定状态的预约可以确认");
        }

        record.setStatus(BookingStatus.CONFIRMED);
        Member member = memberService.getMember(record.getMemberId());
        member.getLockedCourseIds().remove(record.getCourseId());
        member.getBookedCourseIds().add(record.getCourseId());

        return record;
    }

    public void cancelBooking(String bookingId) {
        BookingRecord record = getBookingRecord(bookingId);
        Course course = courseService.getCourse(record.getCourseId());
        Member member = memberService.getMember(record.getMemberId());

        if (record.isWaitlist()) {
            course.getWaitlistMemberIds().remove(member.getId());
            course.setWaitlistCount(course.getWaitlistCount() - 1);
        } else {
            course.getBookedMemberIds().remove(member.getId());
            course.setBookedCount(course.getBookedCount() - 1);
            member.getBookedCourseIds().remove(course.getId());

            if (course.getStatus() == CourseStatus.FULL) {
                course.setStatus(CourseStatus.PUBLISHED);
            }

            notifyWaitlistMember(course);
        }

        member.getLockedCourseIds().remove(course.getId());
        record.setStatus(BookingStatus.CANCELLED);
    }

    private void notifyWaitlistMember(Course course) {
        if (!course.getWaitlistMemberIds().isEmpty()) {
            String nextMemberId = course.getWaitlistMemberIds().poll();
            course.setWaitlistCount(course.getWaitlistCount() - 1);
            course.setBookedCount(course.getBookedCount() + 1);
            course.getBookedMemberIds().add(nextMemberId);

            Member member = memberService.getMember(nextMemberId);
            member.getLockedCourseIds().remove(course.getId());
            member.getBookedCourseIds().add(course.getId());

            for (BookingRecord record : storage.getBookingRecords().values()) {
                if (record.getCourseId().equals(course.getId()) &&
                    record.getMemberId().equals(nextMemberId) &&
                    record.getStatus() == BookingStatus.WAITLIST) {
                    record.setStatus(BookingStatus.CONFIRMED);
                    record.setWaitlist(false);
                    break;
                }
            }
        }
    }

    public void checkIn(String courseId, String memberId) {
        Course course = courseService.getCourse(courseId);
        if (!course.getBookedMemberIds().contains(memberId)) {
            throw new RuntimeException("该会员未预约此课程");
        }

        if (course.getCheckedInMemberIds().contains(memberId)) {
            throw new RuntimeException("该会员已签到");
        }

        course.getCheckedInMemberIds().add(memberId);

        for (BookingRecord record : storage.getBookingRecords().values()) {
            if (record.getCourseId().equals(courseId) &&
                record.getMemberId().equals(memberId) &&
                record.getStatus() == BookingStatus.CONFIRMED) {
                record.setStatus(BookingStatus.CHECKED_IN);
                record.setCheckInTime(LocalDateTime.now());
                break;
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processNoShows() {
        courseService.updateCourseStatus();
        LocalDateTime now = LocalDateTime.now();
        for (Course course : storage.getCourses().values()) {
            if ((course.getStatus() == CourseStatus.IN_PROGRESS || course.getStatus() == CourseStatus.COMPLETED)) {
                for (String memberId : course.getBookedMemberIds()) {
                    if (!course.getCheckedInMemberIds().contains(memberId) &&
                        !course.getNoShowMemberIds().contains(memberId)) {
                        course.getNoShowMemberIds().add(memberId);
                        memberService.recordNoShow(memberId);

                        for (BookingRecord record : storage.getBookingRecords().values()) {
                            if (record.getCourseId().equals(course.getId()) &&
                                record.getMemberId().equals(memberId) &&
                                record.getStatus() == BookingStatus.CONFIRMED) {
                                record.setStatus(BookingStatus.NO_SHOW);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public BookingRecord getBookingRecord(String bookingId) {
        BookingRecord record = storage.getBookingRecords().get(bookingId);
        if (record == null) {
            throw new RuntimeException("预约记录不存在");
        }
        return record;
    }

    public List<BookingRecord> getAllBookingRecords() {
        return new ArrayList<>(storage.getBookingRecords().values());
    }

    public List<BookingRecord> getMemberBookings(String memberId) {
        List<BookingRecord> result = new ArrayList<>();
        for (BookingRecord record : storage.getBookingRecords().values()) {
            if (record.getMemberId().equals(memberId)) {
                result.add(record);
            }
        }
        return result;
    }

    public List<BookingRecord> getCourseBookings(String courseId) {
        List<BookingRecord> result = new ArrayList<>();
        for (BookingRecord record : storage.getBookingRecords().values()) {
            if (record.getCourseId().equals(courseId)) {
                result.add(record);
            }
        }
        return result;
    }
}
