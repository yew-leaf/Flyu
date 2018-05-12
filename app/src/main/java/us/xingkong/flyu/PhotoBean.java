package us.xingkong.flyu;

import java.io.Serializable;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: PhotoBean
 * @创建时间: 2018/5/12 22:12
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class PhotoBean implements Serializable {
    private String uri;
    private boolean check;
    private int position;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
