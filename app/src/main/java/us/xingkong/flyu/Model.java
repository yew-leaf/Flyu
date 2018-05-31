package us.xingkong.flyu;

import cn.bmob.v3.BmobObject;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/22 20:47
 * @描述:
 * @更新日志:
 */
public class Model extends BmobObject {
    private String words;
    private String photo;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
