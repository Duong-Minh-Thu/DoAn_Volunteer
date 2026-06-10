package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.NotificationResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Notification;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.NotificationType;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ForbiddenException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.NotificationRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.RegistrationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               RegistrationRepository registrationRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    // ==================== Gửi thông báo (nội bộ) ====================

    /**
     * Gửi thông báo đến một user.
     */
    public void send(User recipient, NotificationType type, String title, String message, Long referenceId) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(type)
                .title(title)
                .message(message)
                .referenceId(referenceId)
                .build();
        notificationRepository.save(notification);
    }

    /**
     * Gửi thông báo đến nhiều users (ví dụ: toàn bộ sinh viên).
     */
    @Transactional
    public void sendToAll(List<User> recipients, NotificationType type, String title, String message, Long referenceId) {
        List<Notification> notifications = recipients.stream()
                .map(u -> Notification.builder()
                        .recipient(u)
                        .type(type)
                        .title(title)
                        .message(message)
                        .referenceId(referenceId)
                        .build())
                .toList();
        notificationRepository.saveAll(notifications);
    }

    // ==================== API cho user ====================

    /**
     * Lấy danh sách thông báo của user đang đăng nhập.
     */
    public Page<NotificationResponse> getMyNotifications(String username, Pageable pageable) {
        User user = findUser(username);
        return notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(NotificationResponse::from);
    }

    /**
     * Đếm số thông báo chưa đọc.
     */
    public Map<String, Long> countUnread(String username) {
        User user = findUser(username);
        long count = notificationRepository.countByRecipientIdAndIsReadFalse(user.getId());
        return Map.of("unreadCount", count);
    }

    /**
     * Đánh dấu một thông báo đã đọc.
     */
    @Transactional
    public NotificationResponse markAsRead(Long notificationId, String username) {
        User user = findUser(username);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo với ID: " + notificationId));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new ForbiddenException("Bạn không có quyền đọc thông báo này");
        }

        notification.setRead(true);
        return NotificationResponse.from(notificationRepository.save(notification));
    }

    /**
     * Đánh dấu tất cả thông báo đã đọc.
     */
    @Transactional
    public Map<String, Integer> markAllAsRead(String username) {
        User user = findUser(username);
        int updated = notificationRepository.markAllAsRead(user.getId());
        return Map.of("markedAsRead", updated);
    }

    /**
     * Xóa một thông báo.
     */
    @Transactional
    public void deleteNotification(Long notificationId, String username) {
        User user = findUser(username);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo với ID: " + notificationId));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new ForbiddenException("Bạn không có quyền xóa thông báo này");
        }

        notificationRepository.delete(notification);
    }

    /**
     * Gửi thông báo tùy chỉnh dựa trên vai trò của người gửi.
     */
    @Transactional
    public void sendCustomNotification(String senderUsername, com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.SendNotificationRequest req) {
        User sender = findUser(senderUsername);
        
        List<User> recipients;
        if (sender.getRole() == Role.ORG) {
            if (!"REGISTERED_STUDENTS".equalsIgnoreCase(req.getTargetType())) {
                throw new ForbiddenException("Tài khoản Ban tổ chức chỉ được phép thông báo cho sinh viên đã đăng ký hoạt động!");
            }
            recipients = registrationRepository.findStudentsByOrgId(sender.getId());
        } else if (sender.getRole() == Role.ADMIN) {
            if ("STUDENTS".equalsIgnoreCase(req.getTargetType())) {
                recipients = userRepository.findByRole(Role.STUDENT);
            } else if ("ORGS".equalsIgnoreCase(req.getTargetType())) {
                recipients = userRepository.findByRole(Role.ORG);
            } else if ("ALL".equalsIgnoreCase(req.getTargetType())) {
                recipients = userRepository.findAll().stream()
                        .filter(u -> u.getRole() == Role.STUDENT || u.getRole() == Role.ORG)
                        .toList();
            } else {
                throw new IllegalArgumentException("Đối tượng nhận thông báo không hợp lệ!");
            }
        } else {
            throw new ForbiddenException("Bạn không có quyền gửi thông báo!");
        }

        if (recipients.isEmpty()) {
            return;
        }

        sendToAll(recipients, NotificationType.SYSTEM_ALERT, req.getTitle(), req.getMessage(), null);
    }

    // ==================== Helper ====================

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));
    }
}
