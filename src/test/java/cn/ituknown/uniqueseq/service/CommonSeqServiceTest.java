package cn.ituknown.uniqueseq.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class CommonSeqServiceTest {

    @Autowired
    private CommonSeqService commonSeqService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonSeqServiceTest.class);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void exist() {
    }

    @Test
    void take() {
        LOGGER.info("==== test take");
        Long data = commonSeqService.take("commons");
        LOGGER.info("==== test take: {}", data);
    }

    @Test
    void takeN() {
        LOGGER.info("==== test takeN");
        List<Long> data = commonSeqService.take("commons", 10);
        LOGGER.info("==== test takeN: {}", data);
    }

    @Test
    void seqnameList() {
        LOGGER.info("==== test seqnameList");
        List<String> data = commonSeqService.seqnameList();
        LOGGER.info("==== test seqnameList: {}", data);
    }
}