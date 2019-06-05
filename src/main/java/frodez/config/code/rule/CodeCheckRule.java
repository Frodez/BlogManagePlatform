package frodez.config.code.rule;

import frodez.constant.errors.exception.CodeCheckException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface CodeCheckRule {

	default void check(Class<?> klass) throws CodeCheckException {
	};

	default void check(Field field) throws CodeCheckException {
	};

	default void check(Method method) throws CodeCheckException {
	};

}
