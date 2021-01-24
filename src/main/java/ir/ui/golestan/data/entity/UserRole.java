package ir.ui.golestan.data.entity;

import ir.ui.golestan.authorization.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UserRole {

    @Id
    private long userId;

    private Role role;

}
