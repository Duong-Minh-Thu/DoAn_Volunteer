package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.ActivityRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ActivityResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Activity;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.NotificationType;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.BadRequestException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ForbiddenException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ActivityService(ActivityRepository activityRepository,
                           UserRepository userRepository,
                           NotificationService notificationService) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Page<ActivityResponse> getAllActivities(ActivityStatus status, String orgName, String location, Pageable pageable) {
        // Nếu có bất kỳ filter nào (orgName hoặc location) → dùng search query linh hoạt
        boolean hasFilter = (orgName != null && !orgName.isBlank()) || (location != null && !location.isBlank());
        if (hasFilter || status != null) {
            String orgNameParam = (orgName != null && !orgName.isBlank()) ? orgName.trim() : null;
            String locationParam = (location != null && !location.isBlank()) ? location.trim() : null;
            return activityRepository.search(status, orgNameParam, locationParam, pageable)
                    .map(ActivityResponse::from);
        }
        return activityRepository.findAll(pageable).map(ActivityResponse::from);
    }

    public ActivityResponse getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + id));
        return ActivityResponse.from(activity);
    }

    @Transactional
    public ActivityResponse createActivity(String username, ActivityRequest request) {
        User org = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (org.getRole() != Role.ORG && org.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Chỉ tổ chức hoặc admin mới có thể tạo hoạt động");
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        Activity activity = Activity.builder()
                .organization(org)
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation())
                .points(request.getPoints() != null ? request.getPoints() : 0)
                .maxParticipants(request.getMaxParticipants())
                .status(ActivityStatus.UPCOMING)
                .build();

        ActivityResponse response = ActivityResponse.from(activityRepository.save(activity));

        // Gửi thông báo hoạt động mới đến tất cả STUDENT
        List<User> students = userRepository.findByRole(Role.STUDENT);
        notificationService.sendToAll(
                students,
                NotificationType.NEW_ACTIVITY,
                "Hoạt động mới: " + activity.getTitle(),
                "Tổ chức " + org.getFullName() + " vừa đăng hoạt động mới tại " + activity.getLocation(),
                response.getId()
        );

        return response;
    }

    @Transactional
    public ActivityResponse updateActivity(Long id, String username, ActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!activity.getOrganization().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền cập nhật hoạt động này");
        }

        if (request.getTitle() != null) activity.setTitle(request.getTitle());
        if (request.getDescription() != null) activity.setDescription(request.getDescription());
        if (request.getStartDate() != null) activity.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) activity.setEndDate(request.getEndDate());
        if (request.getLocation() != null) activity.setLocation(request.getLocation());
        if (request.getPoints() != null) activity.setPoints(request.getPoints());
        if (request.getMaxParticipants() != null) activity.setMaxParticipants(request.getMaxParticipants());
        if (request.getStatus() != null) activity.setStatus(request.getStatus());

        return ActivityResponse.from(activityRepository.save(activity));
    }

    @Transactional
    public void deleteActivity(Long id, String username) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (!activity.getOrganization().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền xóa hoạt động này");
        }

        String title = activity.getTitle();
        Long activityId = activity.getId();
        activityRepository.delete(activity);

        // Gửi thông báo hủy đến tất cả STUDENT
        List<User> students = userRepository.findByRole(Role.STUDENT);
        notificationService.sendToAll(
                students,
                NotificationType.ACTIVITY_CANCELLED,
                "Hoạt động bị hủy: " + title,
                "Hoạt động \"" + title + "\" đã bị hủy bởi ban tổ chức.",
                activityId
        );
    }
}
