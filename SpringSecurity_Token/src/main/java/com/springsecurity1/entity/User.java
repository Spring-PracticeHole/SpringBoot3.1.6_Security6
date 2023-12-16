package com.springsecurity1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @Column(unique = true)
    private String id;
    private String pw;
    private String name;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String addr1;

    @Column(nullable = true)
    private String addr2;

    @Column(nullable = true)
    private String postcode;

    @Column(nullable = true)
    private String img;

    @CreatedDate
    private LocalDateTime loginAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;
}
