package frodez.config.code.rule.hibernate;

import frodez.config.aop.validation.annotation.common.AnyExist;
import frodez.config.code.rule.CodeCheckRule;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.common.EmptyUtil;
import java.lang.reflect.Field;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnyExistRule implements CodeCheckRule {

	private boolean hasCheck(Field field) {
		return field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(NotEmpty.class) || field.isAnnotationPresent(NotBlank.class);
	}

	@Override
	public void check(Class<?> klass) throws CodeCheckException {
		AnyExist annotation = klass.getAnnotation(AnyExist.class);
		if (annotation != null) {
			String[] fields = annotation.value();
			if (EmptyUtil.no(fields)) {
				try {
					for (String string : fields) {
						Field field = klass.getDeclaredField(string);
						if (hasCheck(field)) {
							throw new CodeCheckException(klass.getCanonicalName(), "的", string, "字段存在非空检查,不需要添加@AnyExist注解");
						}
						if (field == null) {
							throw new CodeCheckException(klass.getCanonicalName(), "没有", string, "字段");
						}
					}
				} catch (Exception e) {
					log.error("[AnyExistRule.check]", e);
				}
			}
		}
	}

}
