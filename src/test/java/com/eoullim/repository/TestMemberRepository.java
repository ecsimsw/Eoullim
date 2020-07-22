package com.eoullim.repository;

import com.eoullim.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMemberRepository {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void saveAndFind(){
        Member m = new Member();
        m.setName("jinhwan");
        memberRepository.save(m);
        Member find = memberRepository.find(m.getId());
        Assertions.assertThat(find.getId()).isEqualTo(m.getId());
    }
}
