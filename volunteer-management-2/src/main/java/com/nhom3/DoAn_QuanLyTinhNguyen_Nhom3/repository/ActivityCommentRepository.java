package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.ActivityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityCommentRepository extends JpaRepository<ActivityComment, Long> {

    // Lấy bình luận gốc (không phải reply)
    @EntityGraph(attributePaths = {"student"})
    Page<ActivityComment> findByActivityIdAndParentCommentIsNullOrderByCreatedAtDesc(Long activityId, Pageable pageable);

    // Lấy tất cả replies của một bình luận
    @EntityGraph(attributePaths = {"student"})
    Page<ActivityComment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId, Pageable pageable);

    long countByActivityId(Long activityId);

    long countByParentCommentId(Long parentCommentId);

    Optional<ActivityComment> findByIdAndStudentId(Long id, Long studentId);
}
