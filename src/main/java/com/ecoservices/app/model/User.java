package com.ecoservices.app.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@CompoundIndex(name="identification" ,def = "{'idtype' :1 , 'idnumber': 1}" ,unique = true)
public class User {

    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    private String password;
    private String fullname;
    private String idtype;
    private String idnumber;
    private boolean enabled;
    private String boss; //TODO cahnge to db reference
    private String creationRole;
    @DBRef
    private Set<Role> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getCreationRole() {
        return creationRole;
    }

    public void setCreationRole(String creationRole) {
        this.creationRole = creationRole;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIdtype() { return idtype; }

    public void setIdtype(String idtype) { this.idtype = idtype; }

    public String getIdnumber() { return idnumber; }

    public void setIdnumber(String idnumber) { this.idnumber = idnumber; }

    public String getBoss() { return boss; }

    public void setBoss(String boss) { this.boss = boss; }
}
