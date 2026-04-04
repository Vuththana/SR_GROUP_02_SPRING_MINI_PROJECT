package org.goros.habit_tracker.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRequest {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9._]{1,18}[a-zA-Z0-9])?$", message = "Username can contain letters, numbers, . and _, but cannot start or end with them")
    private String username;

    @Email(message = "Invalid email address")
    private String email;

    @Size(min = 8, message = "Your password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain uppercase, lowercase, number, and special character")
    private String password;

    private String profileImageUrl;
}
