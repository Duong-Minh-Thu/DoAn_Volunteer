package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.LeaderboardEntryResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.MyRankResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.TrainingPoint;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.TrainingPointRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    private final TrainingPointRepository trainingPointRepository;
    private final UserRepository userRepository;

    public RankingService(TrainingPointRepository trainingPointRepository, UserRepository userRepository) {
        this.trainingPointRepository = trainingPointRepository;
        this.userRepository = userRepository;
    }

    public List<LeaderboardEntryResponse> getLeaderboard(String semester, int limit) {
        List<LeaderboardEntryResponse> result = new ArrayList<>();

        if (semester != null && !semester.isBlank()) {
            List<TrainingPoint> top = trainingPointRepository
                    .findLeaderboardBySemester(semester, PageRequest.of(0, limit));

            for (int i = 0; i < top.size(); i++) {
                TrainingPoint tp = top.get(i);
                result.add(LeaderboardEntryResponse.builder()
                        .rank(i + 1)
                        .studentId(tp.getStudent().getId())
                        .fullName(tp.getStudent().getFullName())
                        .studentCode(tp.getStudent().getStudentCode())
                        .totalPoints(tp.getTotalPoints())
                        .semester(semester)
                        .build());
            }
        } else {
            List<Object[]> top = trainingPointRepository
                    .findAllTimeLeaderboard(PageRequest.of(0, limit));

            for (int i = 0; i < top.size(); i++) {
                Object[] row = top.get(i);
                result.add(LeaderboardEntryResponse.builder()
                        .rank(i + 1)
                        .studentId((Long) row[0])
                        .fullName((String) row[1])
                        .studentCode((String) row[2])
                        .totalPoints(((Number) row[3]).intValue())
                        .semester("all-time")
                        .build());
            }
        }

        return result;
    }

    public MyRankResponse getMyRank(String username, String semester) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (semester != null && !semester.isBlank()) {
            Integer points = trainingPointRepository
                    .findByStudentIdAndSemester(student.getId(), semester)
                    .map(TrainingPoint::getTotalPoints)
                    .orElse(0);

            Long rank = trainingPointRepository.findRankBySemesterAndStudent(semester, student.getId());

            return MyRankResponse.builder()
                    .studentId(student.getId())
                    .fullName(student.getFullName())
                    .studentCode(student.getStudentCode())
                    .totalPoints(points)
                    .rank(rank)
                    .semester(semester)
                    .build();
        } else {
            Integer totalPoints = trainingPointRepository.sumTotalPointsByStudent(student.getId());
            return MyRankResponse.builder()
                    .studentId(student.getId())
                    .fullName(student.getFullName())
                    .studentCode(student.getStudentCode())
                    .totalPoints(totalPoints != null ? totalPoints : 0)
                    .rank(null)
                    .semester("all-time")
                    .build();
        }
    }
}
