package myaong.popolog.inquiryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.inquiryservice.common.exception.ApiResponse;
import myaong.popolog.inquiryservice.dto.request.AdminInquiryRequest;
import myaong.popolog.inquiryservice.dto.response.AdminInquiryResponse;
import myaong.popolog.inquiryservice.service.AdminInquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @Operation(summary = "API 명세서 v0.4 line 108", description = "관리자용 문의 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<AdminInquiryResponse.PagedInquiries>> getAdminInquiries(
            @RequestParam(defaultValue = "1") int page,
            @RequestHeader(name = "memberId") Long memberId) {

        AdminInquiryResponse.PagedInquiries inquiries = adminInquiryService.getInquiries(page, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(inquiries));
    }

    @Operation(summary = "API 명세서 v0.4 line 109", description = "문의에 대한 답변 작성 및 알림 발송")
    @PutMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<Void>> respondToInquiry(
            @PathVariable Long inquiryId,
            @Valid @RequestBody AdminInquiryRequest.RespondInquiry request,
            @RequestHeader(name = "memberId") Long memberId) {

        adminInquiryService.respondToInquiry(inquiryId, request, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}

