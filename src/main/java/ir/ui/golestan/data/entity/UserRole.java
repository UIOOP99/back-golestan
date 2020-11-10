package ir.ui.golestan.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserRole {

    @Id
    private int userId;

    private Role role;


    public enum Role {
        ADMIN,
        PROFESSOR,
        STUDENT,
    }
}
