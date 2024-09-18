package com.example.textsegmenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextBlock {
    private String text;
    private float x;
    private float y;
    private float width;
    private float height;
}