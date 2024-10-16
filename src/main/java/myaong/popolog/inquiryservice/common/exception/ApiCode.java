package myaong.popolog.inquiryservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiCode {

	OK(HttpStatus.OK, "COMMON_2000", "OK"),
	INVALID_DATA(HttpStatus.BAD_REQUEST, "COMMON_4000", "Request data missing or invalid"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_4050", "Method not allowed"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_5000", "Internal Server Error"),
	DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_5001", "DB Error"),

	INQUIRY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "INQUIRY", "비공개 문의입니다."),
	INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "INQUIRY_4040", "존재하지 않는 문의입니다."),
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
