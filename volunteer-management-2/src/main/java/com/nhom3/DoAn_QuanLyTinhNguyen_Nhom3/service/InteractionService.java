package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.CommentRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.CommentResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.LikeResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Activity;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.ActivityComment;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.ActivityLike;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ForbiddenException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.ResourceNotFoundException;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityCommentRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityLikeRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.ActivityRepository;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InteractionService {

    private final ActivityLikeRepository likeRepository;
    private final ActivityCommentRepository commentRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public InteractionService(ActivityLikeRepository likeRepository,
                              ActivityCommentRepository commentRepository,
                              ActivityRepository activityRepository,
                              UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    // ==================== LIKE ====================

    /**
     * Toggle thả tim / bỏ tim cho hoạt động (chỉ STUDENT).
     * Nếu chưa thích → thích; nếu đã thích → bỏ thích.
     */
    @Transactional
    public LikeResponse toggleLike(Long activityId, String username) {
        User student = findUser(username);
        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chỉ sinh viên mới có thể thả tim hoạt động");
        }
        Activity activity = findActivity(activityId);

        Optional<ActivityLike> existing = likeRepository.findByStudentIdAndActivityId(student.getId(), activityId);
        boolean likedByMe;

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            likedByMe = false;
        } else {
            ActivityLike like = ActivityLike.builder()
                    .student(student)
                    .activity(activity)
                    .build();
            likeRepository.save(like);
            likedByMe = true;
        }

        long count = likeRepository.countByActivityId(activityId);
        return new LikeResponse(activityId, count, likedByMe);
    }

    /**
     * Xem lượt thích của một hoạt động (công khai, nhưng trả về likedByMe nếu đã đăng nhập).
     */
    public LikeResponse getLikeStatus(Long activityId, String username) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId);
        }
        long count = likeRepository.countByActivityId(activityId);
        boolean likedByMe = false;
        if (username != null) {
            User u = userRepository.findByUsername(username).orElse(null);
            if (u != null) {
                likedByMe = likeRepository.existsByStudentIdAndActivityId(u.getId(), activityId);
            }
        }
        return new LikeResponse(activityId, count, likedByMe);
    }

    // ==================== COMMENT ====================

    /**
     * Sinh viên đăng bình luận.
     */
    @Transactional
    public CommentResponse createComment(Long activityId, String username, CommentRequest request) {
        User student = findUser(username);
        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chỉ sinh viên mới có thể bình luận");
        }
        Activity activity = findActivity(activityId);

        ActivityComment comment = ActivityComment.builder()
                .activity(activity)
                .student(student)
                .content(request.getContent())
                .build();

        return CommentResponse.from(commentRepository.save(comment));
    }

    /**
     * Lấy danh sách bình luận gốc của một hoạt động (không bao gồm replies).
     */
    public Page<CommentResponse> getComments(Long activityId, Pageable pageable) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId);
        }
        return commentRepository
                .findByActivityIdAndParentCommentIsNullOrderByCreatedAtDesc(activityId, pageable)
                .map(c -> CommentResponse.from(c, commentRepository.countByParentCommentId(c.getId())));
    }

    /**
     * Xóa bình luận (chủ bình luận hoặc ADMIN).
     */
    @Transactional
    public void deleteComment(Long activityId, Long commentId, String username) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId);
        }
        User user = findUser(username);
        ActivityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận với ID: " + commentId));

        if (!comment.getStudent().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền xóa bình luận này");
        }

        commentRepository.delete(comment);
    }

    /**
     * Sinh viên trả lời một bình luận (chỉ STUDENT).
     */
    @Transactional
    public CommentResponse createReply(Long activityId, Long commentId, String username, CommentRequest request) {
        User student = findUser(username);
        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chỉ sinh viên mới có thể trả lời bình luận");
        }
        Activity activity = findActivity(activityId);

        ActivityComment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận với ID: " + commentId));

        // Chỉ cho phép reply bình luận gốc, không cho reply lồng nhiều cấp
        if (parent.getParentComment() != null) {
            throw new com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.exception.BadRequestException(
                "Không thể trả lời một reply, chỉ có thể trả lời bình luận gốc");
        }

        ActivityComment reply = ActivityComment.builder()
                .activity(activity)
                .student(student)
                .content(request.getContent())
                .parentComment(parent)
                .build();

        return CommentResponse.from(commentRepository.save(reply));
    }

    /**
     * Lấy danh sách replies của một bình luận (công khai).
     */
    public Page<CommentResponse> getReplies(Long activityId, Long commentId, Pageable pageable) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId);
        }
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Không tìm thấy bình luận với ID: " + commentId);
        }
        return commentRepository.findByParentCommentIdOrderByCreatedAtAsc(commentId, pageable)
                .map(CommentResponse::from);
    }

    // ==================== Helpers ====================

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));
    }

    private Activity findActivity(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hoạt động với ID: " + activityId));
    }
}
