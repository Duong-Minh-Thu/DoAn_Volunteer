package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Registration;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByStudentIdAndActivityId(Long studentId, Long activityId);

    Optional<Registration> findByStudentIdAndActivityId(Long studentId, Long activityId);

    // Lấy lịch sử của sinh viên (dùng EntityGraph để load Activity)
    @EntityGraph(attributePaths = {"activity", "activity.organization"})
    Page<Registration> findByStudentId(Long studentId, Pageable pageable);

    // Danh sách SV đăng ký 1 hoạt động (dùng EntityGraph để load Student)
    @EntityGraph(attributePaths = {"student"})
    Page<Registration> findByActivityId(Long activityId, Pageable pageable);

    // Lấy tất cả APPROVED registrations của 1 activity (cho bulk attendance)
    @Query("SELECT r FROM Registration r WHERE r.activity.id = :activityId AND r.status = :status")
    List<Registration> findByActivityIdAndStatus(@Param("activityId") Long activityId,
                                                  @Param("status") RegistrationStatus status);

    // Bulk update status (Batch Update để giảm tải DB)
    @Modifying
    @Query("UPDATE Registration r SET r.status = :newStatus WHERE r.activity.id = :activityId AND r.id IN :ids")
    int bulkUpdateStatusByActivityAndIds(@Param("activityId") Long activityId,
                                          @Param("ids") List<Long> ids,
                                          @Param("newStatus") RegistrationStatus newStatus);

    // Đếm số sinh viên ATTENDED của một activity
    long countByActivityIdAndStatus(Long activityId, RegistrationStatus status);

    // Đếm tổng số đăng ký theo status (dùng cho admin dashboard)
    long countByStatus(RegistrationStatus status);
}
