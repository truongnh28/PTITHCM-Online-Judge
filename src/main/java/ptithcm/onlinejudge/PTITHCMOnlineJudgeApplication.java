package ptithcm.onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class PTITHCMOnlineJudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PTITHCMOnlineJudgeApplication.class, args);
    }
}
