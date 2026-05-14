package com.gym.booking.controller;

import com.gym.booking.domain.BookingRecord;
import com.gym.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/lock")
    public ResponseEntity<BookingRecord> lockSeat(@RequestBody Map<String, String> request) {
        String courseId = request.get("courseId");
        String memberId = request.get("memberId");
        return ResponseEntity.ok(bookingService.lockSeat(courseId, memberId));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<BookingRecord> confirmBooking(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable String id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkin")
    public ResponseEntity<Void> checkIn(@RequestBody Map<String, String> request) {
        String courseId = request.get("courseId");
        String memberId = request.get("memberId");
        bookingService.checkIn(courseId, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingRecord> getBookingRecord(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingRecord(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingRecord>> getAllBookingRecords() {
        return ResponseEntity.ok(bookingService.getAllBookingRecords());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BookingRecord>> getMemberBookings(@PathVariable String memberId) {
        return ResponseEntity.ok(bookingService.getMemberBookings(memberId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<BookingRecord>> getCourseBookings(@PathVariable String courseId) {
        return ResponseEntity.ok(bookingService.getCourseBookings(courseId));
    }
}
