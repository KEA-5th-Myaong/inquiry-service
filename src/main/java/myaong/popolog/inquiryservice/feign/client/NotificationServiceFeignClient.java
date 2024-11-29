package myaong.popolog.inquiryservice.feign.client;

import myaong.popolog.inquiryservice.feign.dto.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-service", url = "localhost:9085")
public interface NotificationServiceFeignClient {

    @PostMapping("/notifications/{type}")
    void sendNotification(@RequestBody NotificationRequest request, @PathVariable("type") String type, @RequestHeader("memberId") Long memberId);

}