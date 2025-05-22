package ru.axenix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_history_id_gen")
    @SequenceGenerator(name = "search_history_id_gen", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String fromYandexCode;

    @NotNull
    private String toYandexCode;

    @Column
    @NotNull
    private Integer count = 1;

    @Column
    @NotNull
    private Boolean isFavorite = false;

    @Column
    @UpdateTimestamp
    private LocalDateTime lastSearched;
}
