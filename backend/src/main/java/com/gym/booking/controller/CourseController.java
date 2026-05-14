package com.gym.booking.controller;

import com.gym.booking.domain.Course;
import com.gym.booking.domain.CourseStatistics;
import com.gym.booking.dto.CreateCourseRequest;
import com.gym.booking.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Course> publishCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.publishCourse(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Course> cancelCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.cancelCourse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<CourseStatistics>> getStatistics() {
        return ResponseEntity.ok(courseService.getStatistics());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Course>> getPopularCourses() {
        return ResponseEntity.ok(courseService.getPopularCourses());
    }
}
