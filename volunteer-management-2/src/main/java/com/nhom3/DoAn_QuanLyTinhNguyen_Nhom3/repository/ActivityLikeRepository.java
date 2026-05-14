package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.ActivityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityLikeRepository extends JpaRepository<ActivityLike, Long> {

    boolean existsByStudentIdAndActivityId(Long studentId, Long activityId);

    Optional<ActivityLike> findByStudentIdAndActivityId(Long studentId, Long activityId);

    long countByActivityId(Long activityId);
}
