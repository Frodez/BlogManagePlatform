/**
 * 本包用于配置限流策略。<br>
 * 现在实现了三种限流策略：<br>
 * 1.每用户每端点阻塞（即不能并发请求）。<br>
 * 2.每用户每端点阻塞，且每次请求之间有固定时间间隔。<br>
 * 3.每端点限制每秒请求数量。<br>
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.config.aop.request;
