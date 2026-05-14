package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Feedback;

import java.time.LocalDateTime;

public class FeedbackResponse {
    private Long id;
    private Long activityId;
    private Long studentId;
    private String studentName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public FeedbackResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static FeedbackResponse from(Feedback feedback) {
        FeedbackResponse r = new FeedbackResponse();
        r.id = feedback.getId();
        r.activityId = feedback.getActivity().getId();
        r.studentId = feedback.getStudent().getId();
        r.studentName = feedback.getStudent().getFullName();
        r.rating = feedback.getRating();
        r.comment = feedback.getComment();
        r.createdAt = feedback.getCreatedAt();
        return r;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final FeedbackResponse r = new FeedbackResponse();

        public Builder id(Long val) { r.id = val; return this; }
        public Builder activityId(Long val) { r.activityId = val; return this; }
        public Builder studentId(Long val) { r.studentId = val; return this; }
        public Builder studentName(String val) { r.studentName = val; return this; }
        public Builder rating(Integer val) { r.rating = val; return this; }
        public Builder comment(String val) { r.comment = val; return this; }
        public Builder createdAt(LocalDateTime val) { r.createdAt = val; return this; }

        public FeedbackResponse build() { return r; }
    }
}
