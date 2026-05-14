package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.UpdateProfileRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.UpdateUserRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.UserResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.BadRequestException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.TrainingPointRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TrainingPointRepository trainingPointRepository;

    public UserService(UserRepository userRepository, TrainingPointRepository trainingPointRepository) {
        this.userRepository = userRepository;
        this.trainingPointRepository = trainingPointRepository;
    }

    // ==================== Profile của user đang đăng nhập ====================

    /**
     * Lấy profile của user đang đăng nhập, kèm tổng điểm rèn luyện.
     */
    public UserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));
        Integer totalPoints = trainingPointRepository.sumTotalPointsByStudent(user.getId());
        return UserResponse.from(user, totalPoints);
    }

    /**
     * Cập nhật thông tin cá nhân (user tự cập nhật).
     */
    @Transactional
    public UserResponse updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (userRepository.existsByEmail(request.getEmail()) && !request.getEmail().equals(user.getEmail())) {
                throw new BadRequestException("Email đã được sử dụng bởi tài khoản khác");
            }
            user.setEmail(request.getEmail());
        }

        Integer totalPoints = trainingPointRepository.sumTotalPointsByStudent(user.getId());
        return UserResponse.from(userRepository.save(user), totalPoints);
    }

    // ==================== Admin: Quản lý người dùng ====================

    /**
     * Lấy danh sách tất cả users (ADMIN).
     */
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> {
            Integer totalPoints = trainingPointRepository.sumTotalPointsByStudent(user.getId());
            return UserResponse.from(user, totalPoints);
        });
    }

    /**
     * Lấy chi tiết một user theo ID (ADMIN).
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với ID: " + id));
        Integer totalPoints = trainingPointRepository.sumTotalPointsByStudent(user.getId());
        return UserResponse.from(user, totalPoints);
    }

    /**
     * Admin cập nhật thông tin user.
     */
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với ID: " + id));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (userRepository.existsByEmail(request.getEmail()) && !request.getEmail().equals(user.getEmail())) {
                throw new BadRequestException("Email đã được sử dụng bởi tài khoản khác");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        Integer totalPoints = trainingPointRepository.sumTotalPointsByStudent(user.getId());
        return UserResponse.from(userRepository.save(user), totalPoints);
    }

    /**
     * Admin xóa user theo ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với ID: " + id));
        userRepository.delete(user);
    }
}
