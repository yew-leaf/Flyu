package us.xingkong.flyu.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 21:08
 * @描述:
 * @更新日志:
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
