/**
 * 本包提供了配置限流策略相关注解。<br>
 * 这里所有的注解都用于controller上。对于service，仅支持Limit注解。<br>
 * 1.RepeatLock支持每用户每端点阻塞（即不能并发请求）。<br>
 * 2.TimeoutLock支持每用户每端点阻塞，且每次请求之间有固定时间间隔。<br>
 * 3.Limit支持每端点限制每秒请求数量。<br>
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.config.aop.request.annotation;
