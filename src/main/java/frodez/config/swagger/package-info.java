/**
 * 本包用于配置swagger。<br>
 * 实现了一些插件,可以做到诸如根据是否免登录来判断是否应该携带token作为请求参数,<br>
 * 和使用实体类时利用@ApiModel注解的内容替代一部分@ApiParam的信息(@ApiModel中不存在的全部设置为默认值),<br>
 * 从而减少代码编写。
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.config.swagger;
