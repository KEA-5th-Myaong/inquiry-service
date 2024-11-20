package myaong.popolog.inquiryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class AdminInquiryResponse {

    @Getter
    @AllArgsConstructor
    public static class PagedInquiries {
        private int pageSize;
        private int nextPage;
        private List<InquirySummary> inquiries;
    }

    @Getter
    @AllArgsConstructor
    public static class InquirySummary {
        private Long inquiryId;
        private String title;
        private boolean isReplied;
        private String responder; // nullable
        private String timestamp;
    }
}
