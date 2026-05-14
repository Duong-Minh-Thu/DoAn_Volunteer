package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.BulkAttendanceRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.RegistrationRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.UpdateRegistrationStatusRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.RegistrationResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Activity;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Registration;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.TrainingPoint;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.NotificationType;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.BadRequestException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ForbiddenException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.RegistrationRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.TrainingPointRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final TrainingPointRepository trainingPointRepository;
    private final NotificationService notificationService;

    public RegistrationService(RegistrationRepository registrationRepository,
                               ActivityRepository activityRepository,
                               UserRepository userRepository,
                               TrainingPointRepository trainingPointRepository,
                               NotificationService notificationService) {
        this.registrationRepository = registrationRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.trainingPointRepository = trainingPointRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public RegistrationResponse register(String username, RegistrationRequest request) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chỉ sinh viên mới có thể đăng ký tham gia hoạt động");
        }

        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + request.getActivityId()));

        if (activity.getStatus() == ActivityStatus.COMPLETED || activity.getStatus() == ActivityStatus.CANCELLED) {
            throw new BadRequestException("Hoạt động này đã kết thúc hoặc bị hủy, không thể đăng ký");
        }

        if (registrationRepository.existsByStudentIdAndActivityId(student.getId(), activity.getId())) {
            throw new BadRequestException("Bạn đã đăng ký hoạt động này rồi");
        }

        if (activity.getMaxParticipants() != null) {
            long approved = registrationRepository.countByActivityIdAndStatus(activity.getId(), RegistrationStatus.APPROVED);
            if (approved >= activity.getMaxParticipants()) {
                throw new BadRequestException("Hoạt động đã đủ số lượng người tham gia");
            }
        }

        Registration registration = Registration.builder()
                .student(student)
                .activity(activity)
                .status(RegistrationStatus.PENDING)
                .build();

        return RegistrationResponse.from(registrationRepository.save(registration));
    }

    public Page<RegistrationResponse> getMyActivities(String username, Pageable pageable) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        return registrationRepository.findByStudentId(student.getId(), pageable)
                .map(RegistrationResponse::from);
    }

    public Page<RegistrationResponse> getStudentsByActivity(Long activityId, String username, Pageable pageable) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!activity.getOrganization().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền xem danh sách này");
        }

        return registrationRepository.findByActivityId(activityId, pageable)
                .map(RegistrationResponse::from);
    }

    @Transactional
    public RegistrationResponse updateStatus(Long registrationId, String username, UpdateRegistrationStatusRequest request) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký với ID: " + registrationId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!registration.getActivity().getOrganization().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền duyệt đăng ký này");
        }

        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new BadRequestException("Chỉ có thể duyệt/từ chối đăng ký ở trạng thái PENDING");
        }

        RegistrationStatus newStatus = request.getStatus();
        if (newStatus != RegistrationStatus.APPROVED && newStatus != RegistrationStatus.REJECTED) {
            throw new BadRequestException("Trạng thái không hợp lệ. Chỉ chấp nhận APPROVED hoặc REJECTED");
        }

        registration.setStatus(newStatus);
        RegistrationResponse result = RegistrationResponse.from(registrationRepository.save(registration));

        // Gửi thông báo đến sinh viên
        User student = registration.getStudent();
        Activity activity = registration.getActivity();
        if (newStatus == RegistrationStatus.APPROVED) {
            notificationService.send(
                    student,
                    NotificationType.REGISTRATION_APPROVED,
                    "Đăng ký được duyệt!",
                    "Đơn đăng ký hoạt động \"" + activity.getTitle() + "\" của bạn đã được chấp thuận.",
                    activity.getId()
            );
        } else {
            notificationService.send(
                    student,
                    NotificationType.REGISTRATION_REJECTED,
                    "Đăng ký bị từ chối",
                    "Đơn đăng ký hoạt động \"" + activity.getTitle() + "\" của bạn đã bị từ chối.",
                    activity.getId()
            );
        }

        return result;
    }

    @Transactional
    public int bulkAttendance(Long activityId, String username, BulkAttendanceRequest request) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!activity.getOrganization().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền chốt điểm danh cho hoạt động này");
        }

        List<Long> ids = request.getRegistrationIds();

        int updated = registrationRepository.bulkUpdateStatusByActivityAndIds(activityId, ids, RegistrationStatus.ATTENDED);

        if (activity.getPoints() > 0) {
            List<Registration> attended = registrationRepository.findByActivityIdAndStatus(activityId, RegistrationStatus.ATTENDED);
            String semester = deriveSemester(activity);

            for (Registration reg : attended) {
                if (ids.contains(reg.getId())) {
                    addTrainingPoint(reg.getStudent(), semester, activity.getPoints());
                    // Gửi thông báo điểm danh xác nhận cho từng sinh viên
                    notificationService.send(
                            reg.getStudent(),
                            NotificationType.ATTENDANCE_CONFIRMED,
                            "Xác nhận điểm danh!",
                            "Bạn đã được xác nhận tham dự hoạt động \"" + activity.getTitle()
                                    + "\" và nhận được " + activity.getPoints() + " điểm rèn luyện.",
                            activity.getId()
                    );
                }
            }
        }

        return updated;
    }

    private void addTrainingPoint(User student, String semester, int points) {
        Optional<TrainingPoint> existing = trainingPointRepository.findByStudentIdAndSemester(student.getId(), semester);
        if (existing.isPresent()) {
            TrainingPoint tp = existing.get();
            tp.setTotalPoints(tp.getTotalPoints() + points);
            trainingPointRepository.save(tp);
        } else {
            TrainingPoint tp = TrainingPoint.builder()
                    .student(student)
                    .semester(semester)
                    .totalPoints(points)
                    .build();
            trainingPointRepository.save(tp);
        }
    }

    private String deriveSemester(Activity activity) {
        int year = activity.getStartDate().getYear();
        int month = activity.getStartDate().getMonthValue();
        String hk = (month >= 1 && month <= 6) ? "1" : "2";
        return year + "_" + hk;
    }
}
