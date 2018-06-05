package us.xingkong.flyu;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 11:17
 * @描述:
 * @更新日志:
 */
public class UploadModel implements Serializable {
    private String name;
    private String text;
    private ArrayList<File> img;
    private String created_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<File> getImg() {
        return img;
    }

    public void setImg(ArrayList<File> img) {
        this.img = img;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
