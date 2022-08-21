package ptithcm.onlinejudge.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {


    @Id
    @Column(name = "user_id", nullable = false, length = 10)
    private String id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "active")
    private Byte active;

    public Byte getActive() {
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}