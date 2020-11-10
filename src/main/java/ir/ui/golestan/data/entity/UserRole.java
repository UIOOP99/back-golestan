package ir.ui.golestan.data.entity;

import ir.ui.golestan.authorization.Role;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserRole {

    @Id
    private int userId;

    private Role role;

}
