package myaong.popolog.inquiryservice.feign.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.inquiryservice.feign.client.NotificationServiceFeignClient;
import myaong.popolog.inquiryservice.feign.constant.NotificationType;
import myaong.popolog.inquiryservice.feign.dto.request.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationFeignService {

    private final NotificationServiceFeignClient notificationServiceFeignClient;

    public void sendNotification(Long targetMemberId, String title, String content, String url, NotificationType type, Long senderId) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .memberId(targetMemberId)
                .title(title)
                .content(content)
                .url(url)
                .type(type)
                .build();

        notificationServiceFeignClient.sendNotification(notificationRequest, type.name(), senderId);
    }
}
