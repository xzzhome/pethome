package com.xzz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= PetHomeApp.class)
public class RedisTest {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Test
    public void test() throws Exception{
        System.out.println(template);
    }
}