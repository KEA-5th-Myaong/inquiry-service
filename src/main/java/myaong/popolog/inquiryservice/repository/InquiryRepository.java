package myaong.popolog.inquiryservice.repository;

import myaong.popolog.inquiryservice.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
