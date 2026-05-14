package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.TrainingPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPointRepository extends JpaRepository<TrainingPoint, Long> {

       Optional<TrainingPoint> findByStudentIdAndSemester(Long studentId, String semester);

       // Leaderboard theo học kỳ (dùng JOIN FETCH để tránh N+1)
       @Query("SELECT tp FROM TrainingPoint tp JOIN FETCH tp.student WHERE tp.semester = :semester ORDER BY tp.totalPoints DESC")
       List<TrainingPoint> findLeaderboardBySemester(@Param("semester") String semester, Pageable pageable);

       // Leaderboard all-time (tổng điểm tất cả học kỳ)
       @Query("SELECT tp.student.id, tp.student.fullName, tp.student.studentCode, SUM(tp.totalPoints) as totalPts " +
                     "FROM TrainingPoint tp GROUP BY tp.student.id, tp.student.fullName, tp.student.studentCode " +
                     "ORDER BY totalPts DESC")
       List<Object[]> findAllTimeLeaderboard(Pageable pageable);

       // Xếp hạng của 1 sinh viên trong 1 học kỳ
       @Query("SELECT COUNT(tp) + 1 FROM TrainingPoint tp WHERE tp.semester = :semester AND tp.totalPoints > " +
                     "(SELECT COALESCE(tp2.totalPoints, 0) FROM TrainingPoint tp2 WHERE tp2.student.id = :studentId AND tp2.semester = :semester)")
       Long findRankBySemesterAndStudent(@Param("semester") String semester, @Param("studentId") Long studentId);

       // Tổng điểm tất cả học kỳ của 1 sinh viên
       @Query("SELECT COALESCE(SUM(tp.totalPoints), 0) FROM TrainingPoint tp WHERE tp.student.id = :studentId")
       Integer sumTotalPointsByStudent(@Param("studentId") Long studentId);
}
