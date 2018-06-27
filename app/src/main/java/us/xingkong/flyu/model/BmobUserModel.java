package us.xingkong.flyu.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/22 20:47
 * @描述:
 * @更新日志:
 */
public class BmobUserModel extends BmobUser {

    private BmobFile avatar;
    private String signature;

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
