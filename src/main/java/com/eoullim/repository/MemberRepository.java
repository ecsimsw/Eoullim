package com.eoullim.repository;

import com.eoullim.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member m){ em.persist(m); }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public Member findByLoginId(String loginId){
        List<Member> member = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        if(member != null){
            if(member.size()>0) return member.get(0); // loginId는 안겹치도록
            else return null;
        }

        else{ return null; }
    }
}
