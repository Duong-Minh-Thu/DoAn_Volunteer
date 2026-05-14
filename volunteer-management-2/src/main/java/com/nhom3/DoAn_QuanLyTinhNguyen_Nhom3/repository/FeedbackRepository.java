package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByStudentIdAndActivityId(Long studentId, Long activityId);

    @EntityGraph(attributePaths = {"student"})
    Page<Feedback> findByActivityId(Long activityId, Pageable pageable);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.activity.id = :activityId")
    Double findAverageRatingByActivity(@Param("activityId") Long activityId);
}
