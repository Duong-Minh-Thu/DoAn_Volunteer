package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.DashboardStatsResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.RegistrationRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public StatsService(ActivityRepository activityRepository,
                        UserRepository userRepository,
                        RegistrationRepository registrationRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    public DashboardStatsResponse getDashboardStats() {
        long totalActivities = activityRepository.count();
        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long totalOrgs = userRepository.countByRole(Role.ORG);
        long totalRegistrations = registrationRepository.count();
        long totalAttended = registrationRepository.countByStatus(RegistrationStatus.ATTENDED);

        return DashboardStatsResponse.builder()
                .totalActivities(totalActivities)
                .totalStudents(totalStudents)
                .totalOrganizations(totalOrgs)
                .totalRegistrations(totalRegistrations)
                .totalAttended(totalAttended)
                .build();
    }
}
