package myaong.popolog.inquiryservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AdminInquiryRequest {

    @Getter
    public static class RespondInquiry {
        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }
}
