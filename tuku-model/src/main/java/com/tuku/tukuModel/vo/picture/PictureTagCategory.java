package com.tuku.tukuModel.vo.picture;

import lombok.Data;

import java.util.List;

@Data
public class PictureTagCategory {
    List<String> tagList;

    List<String> categoryList;
}
