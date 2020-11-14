package ir.ui.golestan.authorization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize
public class AuthenticatedUser {

    int userId;

    String username;

    String firstName;

    String lastName;

    String email;

    @JsonIgnore
    Role role;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AuthenticationJwtObjectBuilder {
    }
}
