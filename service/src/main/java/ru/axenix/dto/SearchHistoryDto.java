package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchHistoryDto {
    private String from;
    private String to;
    private String fromTitle;
    private String toTitle;
    private Boolean isFavorite;
}
