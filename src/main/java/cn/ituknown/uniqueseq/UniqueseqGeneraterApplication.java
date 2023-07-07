package cn.ituknown.uniqueseq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.ituknown.uniqueseq.mapper")
public class UniqueseqGeneraterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniqueseqGeneraterApplication.class, args);
    }

}
