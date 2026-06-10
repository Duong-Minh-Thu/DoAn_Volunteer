package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @Size(max = 1000, message = "Bình luận tối đa 1000 ký tự")
    private String content;

    private String image;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
