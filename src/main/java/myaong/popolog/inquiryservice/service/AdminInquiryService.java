package myaong.popolog.inquiryservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.inquiryservice.common.exception.ApiCode;
import myaong.popolog.inquiryservice.common.exception.ApiException;
import myaong.popolog.inquiryservice.dto.request.AdminInquiryRequest;
import myaong.popolog.inquiryservice.dto.response.AdminInquiryResponse;
import myaong.popolog.inquiryservice.entity.Inquiry;
import myaong.popolog.inquiryservice.entity.InquiryReply;
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

    // 관리자용 문의 조회
    @Transactional(readOnly = true)
    public AdminInquiryResponse.PagedInquiries getInquiries(int page, Long memberId) {
        // 페이지 값이 유효하지 않은 경우 COMMON_4000 예외 발생
        if (page <= 0) {
            throw new ApiException(ApiCode.INVALID_DATA, "page 값은 1 이상의 숫자여야 합니다.");
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 3); // 페이지 크기: 3
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageRequest);

        // 조회된 문의 데이터 변환
        List<AdminInquiryResponse.InquirySummary> inquiries = inquiryPage.getContent().stream()
                .map(inquiry -> new AdminInquiryResponse.InquirySummary(
                        inquiry.getId(),
                        inquiry.getTitle(),
                        inquiry.getInquiryReply() != null, // 답변 여부
                        inquiry.getInquiryReply() != null ? inquiry.getInquiryReply().getMemberId().toString() : null, // 답변자 ID
                        inquiry.getCreatedAt().toString() // 타임스탬프
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
                .orElseThrow(() -> new ApiException(ApiCode.INQUIRY_NOT_FOUND, "존재하지 않는 문의입니다."));

        // 답변 데이터 생성 및 저장
        InquiryReply reply = InquiryReply.builder()
                .inquiry(inquiry)
                .memberId(memberId) // 관리자 ID 저장
                .content(request.getContent())
                .build();

        inquiryReplyRepository.save(reply);

        // 알림 발송 로직 미구현
    }
}
