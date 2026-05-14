package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Activity;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Dùng @EntityGraph để tránh N+1 khi load organization
    @EntityGraph(attributePaths = {"organization"})
    Page<Activity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"organization"})
    Page<Activity> findByStatus(ActivityStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT a FROM Activity a WHERE a.status = :status AND a.startDate >= :from AND a.endDate <= :to")
    Page<Activity> findByStatusAndDateRange(@Param("status") ActivityStatus status,
                                            @Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to,
                                            Pageable pageable);

    /**
     * Tìm kiếm hoạt động linh hoạt: có thể lọc theo status, tên tổ chức, địa điểm.
     * Các param null = bỏ qua điều kiện đó (tìm tất cả).
     */
    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT a FROM Activity a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:orgName IS NULL OR LOWER(a.organization.fullName) LIKE LOWER(CONCAT('%', :orgName, '%'))) AND " +
           "(:location IS NULL OR LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Activity> search(@Param("status") ActivityStatus status,
                          @Param("orgName") String orgName,
                          @Param("location") String location,
                          Pageable pageable);

    long count();
}

