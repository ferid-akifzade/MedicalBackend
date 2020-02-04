package org.medical.libs;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrdinaryUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String date;
    private String roles;

    @Transient
    private final String ROLES_DELIMITER = ":";

    private String getROLES_DELIMITER(){return ":";}

    public OrdinaryUser(String name, String surname, String email, String password, String date, String ...roles) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.date = date;
        setRoles(roles);
    }

    public void setRoles(String ...roles){
        this.roles = String.join(ROLES_DELIMITER,roles);
    }
    public String[] getRoles() {
        if (this.roles == null || this.roles.isEmpty()) return new String[]{};
        return this.roles.split(ROLES_DELIMITER);
    }

}
