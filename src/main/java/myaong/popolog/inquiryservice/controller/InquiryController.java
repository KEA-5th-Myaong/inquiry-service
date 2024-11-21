package myaong.popolog.inquiryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.inquiryservice.common.exception.ApiResponse;
import myaong.popolog.inquiryservice.dto.request.InquiryRequest;
import myaong.popolog.inquiryservice.dto.response.InquiryResponse;
import myaong.popolog.inquiryservice.service.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "API 명세서 v0.5 line 89", description = "문의 목록 조회 (페이징)")
    @GetMapping
    public ResponseEntity<ApiResponse<InquiryResponse.PagedInquiries>> getInquiries(
            @RequestParam(defaultValue = "1") int page,
            @RequestHeader(name = "memberId", required = false) Long memberId) {

        InquiryResponse.PagedInquiries inquiries = inquiryService.getInquiries(page, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(inquiries));
    }

    @Operation(summary = "API 명세서 v0.5 line 90", description = "문의 상세 조회")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<InquiryResponse.InquiryDetail>> getInquiryDetail(
            @PathVariable Long inquiryId,
            @RequestHeader(name = "memberId", required = false) Long memberId) {

        InquiryResponse.InquiryDetail detail = inquiryService.getInquiryDetail(inquiryId, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(detail));
    }

    @Operation(summary = "API 명세서 v0.5 line 91", description = "문의 작성")
    @PostMapping
    public ResponseEntity<ApiResponse<InquiryResponse.CreatedInquiry>> createInquiry(
            @Valid @RequestBody InquiryRequest.CreateInquiry request,
            @RequestHeader(name = "memberId") Long memberId) {

        InquiryResponse.CreatedInquiry created = inquiryService.createInquiry(request, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(created));
    }
}