package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

public class LikeResponse {
    private Long activityId;
    private long likeCount;
    private boolean likedByMe; // user hiện tại đã thích chưa

    public LikeResponse() {}

    public LikeResponse(Long activityId, long likeCount, boolean likedByMe) {
        this.activityId = activityId;
        this.likeCount = likeCount;
        this.likedByMe = likedByMe;
    }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public long getLikeCount() { return likeCount; }
    public void setLikeCount(long likeCount) { this.likeCount = likeCount; }

    public boolean isLikedByMe() { return likedByMe; }
    public void setLikedByMe(boolean likedByMe) { this.likedByMe = likedByMe; }
}
