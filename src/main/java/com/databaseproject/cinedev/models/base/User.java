package com.databaseproject.cinedev.models.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private String email;
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = hashPassword(password);
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean isPasswordCheck(String loginPassword, String hashedPassword) {
        return BCrypt.checkpw(loginPassword, hashedPassword);
    }
}
