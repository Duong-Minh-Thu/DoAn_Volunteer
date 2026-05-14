package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.FeedbackRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.FeedbackResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Activity;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Feedback;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.BadRequestException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ForbiddenException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.FeedbackRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.RegistrationRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           ActivityRepository activityRepository,
                           UserRepository userRepository,
                           RegistrationRepository registrationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public FeedbackResponse createFeedback(Long activityId, String username, FeedbackRequest request) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chỉ sinh viên mới có thể đánh giá");
        }

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId));

        boolean hasAttended = registrationRepository
                .findByStudentIdAndActivityId(student.getId(), activityId)
                .map(reg -> reg.getStatus() == RegistrationStatus.ATTENDED)
                .orElse(false);

        if (!hasAttended) {
            throw new BadRequestException("Bạn cần hoàn thành hoạt động mới có thể đánh giá");
        }

        if (feedbackRepository.existsByStudentIdAndActivityId(student.getId(), activityId)) {
            throw new BadRequestException("Bạn đã đánh giá hoạt động này rồi");
        }

        Feedback feedback = Feedback.builder()
                .activity(activity)
                .student(student)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return FeedbackResponse.from(feedbackRepository.save(feedback));
    }

    public Page<FeedbackResponse> getFeedbacksByActivity(Long activityId, Pageable pageable) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId);
        }
        return feedbackRepository.findByActivityId(activityId, pageable).map(FeedbackResponse::from);
    }

    @Transactional
    public void deleteFeedback(Long activityId, Long feedbackId, String username) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId);
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy feedback với ID: " + feedbackId));

        if (!feedback.getStudent().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền xóa đánh giá này");
        }

        feedbackRepository.delete(feedback);
    }
}
