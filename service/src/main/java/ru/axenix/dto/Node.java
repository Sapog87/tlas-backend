package ru.axenix.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Node {
    private String id;
    private String label;
    private String title;
}
