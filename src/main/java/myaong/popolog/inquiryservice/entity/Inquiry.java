package myaong.popolog.inquiryservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`inquiry`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inquiry_id")
	private Long id;

	// 문의 작성자
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	// 비밀글 여부
	@Column(name = "is_secret", nullable = false)
	private Boolean isSecret;

	@Setter
	@OneToOne(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
	private InquiryReply inquiryReply;

	@Builder
	public Inquiry(Long memberId, String title, String content, Boolean isSecret) {
		this.memberId = memberId;
		this.title = title;
		this.content = content;
		this.isSecret = isSecret;
	}
}
