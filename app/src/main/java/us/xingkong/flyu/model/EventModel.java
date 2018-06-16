package us.xingkong.flyu.model;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/10 0:12
 * @描述:
 * @更新日志:
 */
public class EventModel<T> {
    private String publisher;
    private T subscriber;

    public EventModel(String publisher, T subscriber) {
        this.publisher = publisher;
        this.subscriber = subscriber;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public T getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(T subscriber) {
        this.subscriber = subscriber;
    }
}
