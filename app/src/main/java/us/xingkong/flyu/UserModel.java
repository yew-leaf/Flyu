package us.xingkong.flyu;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 21:10
 * @描述:
 * @更新日志:
 */
@Entity
public class UserModel {
    @Id
    private String username;
    private String email;
    private String password;
    private boolean isLogged = false;

    @Generated(hash = 1756045049)
    public UserModel(String username, String email, String password,
                     boolean isLogged) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isLogged = isLogged;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean getIsLogged() {
        return this.isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
}
