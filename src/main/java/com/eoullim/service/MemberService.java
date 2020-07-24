package com.eoullim.service;

import com.eoullim.domain.Member;
import com.eoullim.form.JoinForm;
import com.eoullim.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member loginCheck(String loginId, String loginPw){
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) return null; // 존재하지 않는 사용자 id, 이것도 enum으로 화면에 보내주자

        if(member.getLoginPw().equals(loginPw)){ return member; }
        else{ return null; } // 비밀번호가 틀림
    }

    @Transactional
    public Member join(JoinForm joinForm){
        Member isAlready = memberRepository.findByLoginId(joinForm.getLoginId());
        if(isAlready != null) return null; // 이미 존재하는 사용자 id, validation 처리할 것.

        Member newMember = new Member();
        newMember.setName(joinForm.getName());
        newMember.setLoginId(joinForm.getLoginId());
        newMember.setLoginPw(joinForm.getLoginPw());
        newMember.setEmail(joinForm.getEmail());

        memberRepository.save(newMember);
        return newMember;
    }
}
