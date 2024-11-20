package myaong.popolog.inquiryservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;



public class InquiryRequest {

    @Getter
    public static class CreateInquiry {
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }
}
