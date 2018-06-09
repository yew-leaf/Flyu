package us.xingkong.flyu.base;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 1:05
 * @描述:
 * @更新日志:
 */
public interface OnRequestListener<T> {
    void success(T result);

    void failure(String errorMsg);
}
