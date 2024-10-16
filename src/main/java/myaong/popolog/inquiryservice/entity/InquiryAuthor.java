package myaong.popolog.inquiryservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`inquiry_author`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryAuthor extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	// 글 조회 시 로그인 아이디 표시
	@Column(name = "username", nullable = false)
	private String username;

	@Builder
	public InquiryAuthor(String username) {
		this.username = username;
	}
}
