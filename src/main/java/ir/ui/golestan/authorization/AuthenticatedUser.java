package ir.ui.golestan.authorization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AuthenticatedUser {

    long userId;

    String username;

    String firstName;

    String lastName;

    String email;

    Role role;

}
