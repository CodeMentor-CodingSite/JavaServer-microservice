package com.codementor.user.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET status = 'CLOSE' WHERE id = ?")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", columnDefinition = "VARCHAR(100)", unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "text")
    private String password;

    @Column(name = "nickname", columnDefinition = "VARCHAR(50)")
    private String nickname;

    @Column(name = "firstname", columnDefinition = "VARCHAR(50)")
    private String firstname;

    @Column(name = "lastname", columnDefinition = "VARCHAR(50)")
    private String lastname;

    @Builder.Default
    @Column(name = "role", columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Builder.Default
    @Column(name = "status", columnDefinition = "VARCHAR(50) DEFAULT 'OPEN'")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.OPEN;

    public User encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);

        return this;
    }

    public boolean isMatchPassword(String raw, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(raw, this.password);
    }
}
