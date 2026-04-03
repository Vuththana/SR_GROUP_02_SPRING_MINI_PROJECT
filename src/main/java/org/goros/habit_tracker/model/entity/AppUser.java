package org.goros.habit_tracker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements UserDetails {

    private UUID userId;
    private String username;
    private String email;
    private String password;
    private Long level;
    private Long xp;
    private String profile_image;
    private Boolean isVerified;
    private Instant createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
