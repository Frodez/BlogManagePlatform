/**
 * 本包用于支持限流策略配置的实现。<br>
 * facade是接口。<br>
 * impl是实现，目前包括RepeatLock和TimeoutLock所用的两组实现，分别使用guava-cache和redis。<br>
 * impl中的KeyGenerator用于RepeatLock和TimeoutLock。
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.config.aop.request.checker;
