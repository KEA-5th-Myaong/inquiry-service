package myaong.popolog.inquiryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InquiryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InquiryServiceApplication.class, args);
    }

}
