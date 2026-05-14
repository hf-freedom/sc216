package com.gym.booking.controller;

import com.gym.booking.domain.Member;
import com.gym.booking.dto.CreateMemberRequest;
import com.gym.booking.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable String id) {
        return ResponseEntity.ok(memberService.getMember(id));
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}/banned")
    public ResponseEntity<Boolean> isBanned(@PathVariable String id) {
        return ResponseEntity.ok(memberService.isBanned(id));
    }

    @PostMapping("/{id}/reset-ban")
    public ResponseEntity<Void> resetBan(@PathVariable String id) {
        memberService.resetBan(id);
        return ResponseEntity.ok().build();
    }
}
