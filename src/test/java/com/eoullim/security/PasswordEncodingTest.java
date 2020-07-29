package com.eoullim.security;

import com.eoullim.repository.EntityMappingTest;
import com.eoullim.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordEncodingTest {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private static Logger logger = LoggerFactory.getLogger(PasswordEncodingTest.class);

    @Test
    public void signUpTest() throws InterruptedException {
        String password = "qwerty123456";
        logger.info("before encode : "+ password);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        logger.info("after encode : "+ encodedPassword);

        Assertions.assertThat(bCryptPasswordEncoder.matches("qwerty123456", encodedPassword)).isEqualTo(true);

        logger.info(bCryptPasswordEncoder.encode("qwerty123456"));

        Thread.sleep(2000);

        logger.info(bCryptPasswordEncoder.encode("qwerty123456"));

        Assertions.assertThat(bCryptPasswordEncoder.matches("qwerty123456", encodedPassword)).isEqualTo(true);
    }
}
