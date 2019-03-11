/**
 * 本包提供了方法参数自动验证相关注解。<br>
 * Check用于标注需要验证的方法。拥有该注解的方法才会进行参数自动验证。此注解应该写在具体方法实现上。<br>
 * common包中包括了通用验证规则注解。<br>
 * special包中包括了专有用途验证规则注解。<br>
 * 这些验证规则注解在方法无重载时，可以直接写在方法实现上。但如果有重载，则应写在虚方法上。
 * @author Frodez
 * @date 2019-03-11
 */
package frodez.config.aop.validation.annotation;
