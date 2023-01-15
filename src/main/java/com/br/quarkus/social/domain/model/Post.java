package com.br.quarkus.social.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_text")
    private String text;
    @Column(name = "date_time")
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @PrePersist
    public void prePersist(){
       setTimestamp(LocalDateTime.now());
    }

}
