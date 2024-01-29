package com.codementor.user.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user")
@Where(clause = "user_status = 'OPEN'")
@SQLDelete(sql = "UPDATE user SET user_status = 'CLOSE' WHERE user_id = ?")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "user_password", columnDefinition = "text")
    private String password;

    @Column(name = "user_nickname", columnDefinition = "VARCHAR(50)")
    private String nickname;

    @Column(name = "user_firstname", columnDefinition = "VARCHAR(50)")
    private String firstname;

    @Column(name = "user_lastname", columnDefinition = "VARCHAR(50)")
    private String lastname;

    @Column(name = "user_role", columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder.Default
    @Column(name = "user_status", columnDefinition = "VARCHAR(50) DEFAULT 'OPEN'")
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
