package amirgol.coach.participants.model;

import amirgol.coach.participants.model.role.Role;
import amirgol.coach.security.otp.model.OTP;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "participants", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "participant_unique_email"))
public class Participants implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participants_seq")
    @SequenceGenerator(name = "participants_seq", sequenceName = "participants_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, name = "email")
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @jakarta.validation.constraints.Email(message = "Email format is invalid")
    private String email;

    @Column(nullable = false, name = "username")
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Column(nullable = false, name = "is_email_verified")
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "when_email_verified")
    private LocalDateTime whenEmailVerified;

    @Column(name = "failed_login_attempts", nullable = false)
    @Builder.Default
    private int failedLoginAttempts = 0;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "participants",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OTP> otps;

    public void updateUsername(String updatedName) {
        this.username = updatedName;
    }

    public void updateEmail(String updatedEmail) {
        this.email = updatedEmail;
    }

    public void markEmailAsVerified() {
        this.emailVerified = true;
        this.whenEmailVerified = LocalDateTime.now();
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("MEMBER"));
    }

    @Override
    public String getPassword() {
        return otps.isEmpty() ? null : otps.getFirst().getOtpCode();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}