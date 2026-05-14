package com.gym.booking.service;

import com.gym.booking.domain.Member;
import com.gym.booking.dto.CreateMemberRequest;
import com.gym.booking.repository.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    @Autowired
    private InMemoryStorage storage;

    @Value("${gym.booking.max-no-show:3}")
    private int maxNoShow;

    @Value("${gym.booking.ban-duration-days:7}")
    private int banDurationDays;

    public Member createMember(CreateMemberRequest request) {
        Member member = new Member();
        member.setId(UUID.randomUUID().toString());
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setNoShowCount(0);
        storage.getMembers().put(member.getId(), member);
        return member;
    }

    public Member getMember(String memberId) {
        Member member = storage.getMembers().get(memberId);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }
        return member;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(storage.getMembers().values());
    }

    public boolean isBanned(String memberId) {
        Member member = getMember(memberId);
        return member.getBannedUntil() != null && member.getBannedUntil().isAfter(LocalDateTime.now());
    }

    public void recordNoShow(String memberId) {
        Member member = getMember(memberId);
        member.setNoShowCount(member.getNoShowCount() + 1);
        if (member.getNoShowCount() >= maxNoShow) {
            member.setBannedUntil(LocalDateTime.now().plusDays(banDurationDays));
            member.setNoShowCount(0);
        }
    }

    public void resetBan(String memberId) {
        Member member = getMember(memberId);
        member.setBannedUntil(null);
        member.setNoShowCount(0);
    }
}
