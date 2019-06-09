package frodez.config.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 端点验证策略注解<br>
 * 拥有本注解的端点,其是否验证策略会强制使用注解的配置,即覆盖掉默认配置。
 * @author Frodez
 * @date 2019-06-06
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyStrategy {

	/**
	 * @author Frodez
	 * @date 2019-06-06
	 */
	VerifyStrategyEnum value();

	/**
	 * 端点验证策略
	 * @author Frodez
	 * @date 2019-06-06
	 */
	public static enum VerifyStrategyEnum {

		/**
		 * 不需要验证
		 */
		FREE_PASS,
		/**
		 * 必须验证
		 */
		MUST_VERIFY;

	}

}
