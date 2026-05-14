package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

public class LeaderboardEntryResponse {
    private int rank;
    private Long studentId;
    private String fullName;
    private String studentCode;
    private Integer totalPoints;
    private String semester;

    public LeaderboardEntryResponse() {}

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final LeaderboardEntryResponse r = new LeaderboardEntryResponse();

        public Builder rank(int val) { r.rank = val; return this; }
        public Builder studentId(Long val) { r.studentId = val; return this; }
        public Builder fullName(String val) { r.fullName = val; return this; }
        public Builder studentCode(String val) { r.studentCode = val; return this; }
        public Builder totalPoints(Integer val) { r.totalPoints = val; return this; }
        public Builder semester(String val) { r.semester = val; return this; }

        public LeaderboardEntryResponse build() { return r; }
    }
}
