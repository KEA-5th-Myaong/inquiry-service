package myaong.popolog.inquiryservice.feign.dto.request;

import lombok.Builder;
import lombok.Getter;
import myaong.popolog.inquiryservice.feign.constant.NotificationType;

@Getter
@Builder
public class NotificationRequest {
    private Long memberId;
    private String title;
    private String content;
    private String url;
    private NotificationType type;
}