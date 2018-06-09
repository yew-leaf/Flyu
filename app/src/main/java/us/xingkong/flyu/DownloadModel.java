package us.xingkong.flyu;

import java.util.List;

import us.xingkong.flyu.app.Constants;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 20:00
 * @描述:
 * @更新日志:
 */
public class DownloadModel {

    private List<Message> message;

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public static String formatUrl(String string) {
        return Constants.URL + string.replace("\\", "");
    }

    public class Message {
        private String text;
        private List<String> img;
        private String time;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
