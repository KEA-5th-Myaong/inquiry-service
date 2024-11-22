package myaong.popolog.inquiryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class InquiryResponse {

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
        private boolean isMine;
        private boolean isSecret;
        private boolean isReplied;
        private String timestamp;
    }

    @Getter
    @AllArgsConstructor
    public static class InquiryDetail {
        private Long inquiryId;
        private boolean isSecret;
        private Inquiry inquiry;
        private InquiryReply inquiryReply;
    }

    @Getter
    @AllArgsConstructor
    public static class CreatedInquiry {
        private Long inquiryId;
    }

    @Getter
    @AllArgsConstructor
    public static class Inquiry {
        private String title;
        private String content;
        private String timestamp;
    }

    @Getter
    @AllArgsConstructor
    public static class InquiryReply {
        private String content;
        private String timestamp;
    }
}
