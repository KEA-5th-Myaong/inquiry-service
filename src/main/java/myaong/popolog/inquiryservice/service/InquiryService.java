package myaong.popolog.inquiryservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.inquiryservice.common.exception.ApiCode;
import myaong.popolog.inquiryservice.common.exception.ApiException;
import myaong.popolog.inquiryservice.dto.request.InquiryRequest;
import myaong.popolog.inquiryservice.dto.response.InquiryResponse;
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
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryReplyRepository inquiryReplyRepository;

    // 문의 목록 조회
    @Transactional(readOnly = true)
    public InquiryResponse.PagedInquiries getInquiries(int page, Long memberId) {
        if (page <= 0) {
            throw new ApiException(ApiCode.INVALID_DATA, "page 값은 1 이상의 숫자여야 합니다.");
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageRequest);

        List<InquiryResponse.InquirySummary> inquiries = inquiryPage.getContent().stream()
                .map(inquiry -> new InquiryResponse.InquirySummary(
                        inquiry.getId(),
                        inquiry.getTitle(),
                        memberId != null && inquiry.getMemberId().equals(memberId), // memberId가 null일 경우 isMine = false
                        inquiry.getIsSecret(),
                        inquiry.getInquiryReply() != null,
                        inquiry.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        int nextPage = inquiryPage.hasNext() ? page + 1 : 0;

        return new InquiryResponse.PagedInquiries(
                inquiryPage.getSize(),
                nextPage,
                inquiries
        );
    }

    //문의 상세 조회
    @Transactional(readOnly = true)
    public InquiryResponse.InquiryDetail getInquiryDetail(Long inquiryId, Long memberId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(ApiCode.INQUIRY_NOT_FOUND));

        // 비공개 문의 접근 제한
        if (inquiry.getIsSecret() && (memberId == null || !inquiry.getMemberId().equals(memberId))) {
            throw new ApiException(ApiCode.INQUIRY_ACCESS_DENIED);
        }

        InquiryReply reply = inquiryReplyRepository.findById(inquiryId).orElse(null);

        return new InquiryResponse.InquiryDetail(
                inquiry.getId(),
                inquiry.getIsSecret(),
                new InquiryResponse.Inquiry(
                        inquiry.getTitle(),
                        inquiry.getContent(),
                        inquiry.getCreatedAt().toString()
                ),
                reply == null ? null : new InquiryResponse.InquiryReply(
                        reply.getContent(),
                        reply.getCreatedAt().toString()
                )
        );
    }

    // 문의 작성
    @Transactional
    public InquiryResponse.CreatedInquiry createInquiry(InquiryRequest.CreateInquiry request, Long memberId) {
        Inquiry inquiry = Inquiry.builder()
                .memberId(memberId)
                .title(request.getTitle())
                .content(request.getContent())
                .isSecret(false)
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        return new InquiryResponse.CreatedInquiry(savedInquiry.getId());
    }
}
