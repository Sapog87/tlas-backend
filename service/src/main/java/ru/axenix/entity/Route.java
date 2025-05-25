package ru.axenix.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "route")
public class Route {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "value")
    private RouteJson value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    @Data
    public static class RouteJson {
        private int transfers;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private OffsetDateTime startDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private OffsetDateTime finishDateTime;
        private String startLocation;
        private String finishLocation;
        private List<SegmentJson> segments;
    }

    @Data
    public static class SegmentJson {
        private Transport transport;
        private String vehicle;
        private String raceNumber;
        private String carrier;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private OffsetDateTime startDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private OffsetDateTime finishDateTime;
        private String startStation;
        private String originCode;
        private String finishStation;
        private String destinationCode;
        private String startCity;
        private String finishCity;
    }

    public enum Transport {
        PLANE,
        TRAIN,
        SUBURBAN,
        BUS,
    }
}
