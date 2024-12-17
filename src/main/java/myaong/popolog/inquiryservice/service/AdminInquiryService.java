package myaong.popolog.inquiryservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.inquiryservice.common.exception.ApiCode;
import myaong.popolog.inquiryservice.common.exception.ApiException;
import myaong.popolog.inquiryservice.dto.request.AdminInquiryRequest;
import myaong.popolog.inquiryservice.dto.response.AdminInquiryResponse;
import myaong.popolog.inquiryservice.entity.Inquiry;
import myaong.popolog.inquiryservice.entity.InquiryReply;
import myaong.popolog.inquiryservice.feign.constant.NotificationType;
import myaong.popolog.inquiryservice.feign.service.NotificationFeignService;
import myaong.popolog.inquiryservice.repository.InquiryRepository;
import myaong.popolog.inquiryservice.repository.InquiryReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryReplyRepository inquiryReplyRepository;
    private final NotificationFeignService notificationFeignService;

    // 관리자용 문의 조회
    @Transactional(readOnly = true)
    public AdminInquiryResponse.PagedInquiries getInquiries(int page, Long memberId) {
        // 페이지 값이 유효하지 않은 경우 COMMON_4000 예외 발생
        if (page <= 0) {
            throw new ApiException(ApiCode.INVALID_DATA, "page 값은 1 이상의 숫자여야 합니다.");
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageRequest);

        // 조회된 문의 데이터 변환
        List<AdminInquiryResponse.InquirySummary> inquiries = inquiryPage.getContent().stream()
                .map(inquiry -> new AdminInquiryResponse.InquirySummary(
                        inquiry.getId(),
                        inquiry.getTitle(),
                        inquiry.getInquiryReply() != null,
                        inquiry.getInquiryReply() != null ? inquiry.getInquiryReply().getMemberId().toString() : null,
                        inquiry.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        // 다음 페이지 계산
        int nextPage = inquiryPage.hasNext() ? page + 1 : 0;

        return new AdminInquiryResponse.PagedInquiries(
                inquiryPage.getSize(),
                nextPage,
                inquiries
        );
    }

    // 문의 답변 작성
    @Transactional
    public void respondToInquiry(Long inquiryId, AdminInquiryRequest.RespondInquiry request, Long memberId) {
        // 문의 존재 여부 확인 INQUIRY_4040 예외 발생
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(ApiCode.INQUIRY_NOT_FOUND));

        // 답변 데이터 생성 및 저장
        InquiryReply reply = InquiryReply.builder()
                .inquiry(inquiry)
                .memberId(memberId)
                .content(request.getContent())
                .build();

        inquiryReplyRepository.save(reply);

        // 알림 발송 로직 추가
        sendNotificationForInquiryReply(inquiry, memberId, request.getContent());
    }

    // 알림 발송 메서드
    private void sendNotificationForInquiryReply(Inquiry inquiry, Long responderId, String replyContent) {

        String title = "문의에 대한 답변이 등록되었습니다.";
        String url = "/inquiries/" + inquiry.getId();

        // 알림 전송
        notificationFeignService.sendNotification(inquiry.getMemberId(), title, replyContent, url, NotificationType.INQUIRY_REPLY, responderId);
    }
}
