package us.xingkong.flyu;

import java.io.Serializable;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/3 21:10
 * @描述:
 * @更新日志:
 */
public class UserModel implements Serializable {
    private String username;
    private String email;
    private String password;

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
}
