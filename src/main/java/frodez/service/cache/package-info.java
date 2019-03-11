/**
 * 本包用于存放所有与业务有关的cache。<br>
 * base包为cache的基础包，包括了基本redis service和cache接口。<br>
 * vm包存放了使用jvm作为存储的cache实现。<br>
 * 在没有特殊需求时，可以直接使用基本redis service。但建议最好实现cache接口，对专用的业务逻辑使用专门的cache。
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.service.cache;
