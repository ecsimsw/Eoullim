package com.eoullim.repository;

import com.eoullim.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member m){
        em.persist(m);
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
