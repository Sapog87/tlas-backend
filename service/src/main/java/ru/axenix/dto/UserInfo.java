package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
public class UserInfo {
    private String username;
    private String name;
    private Timestamp registrationDate;
}
