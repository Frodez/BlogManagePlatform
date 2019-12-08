package frodez.config.code.rule;

import frodez.constant.errors.exception.CodeCheckException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface CodeCheckRule {

	default boolean support(Class<?> klass) throws CodeCheckException {
		return false;
	};

	default void check(Class<?> klass) throws CodeCheckException {
	};

	default boolean support(Field field) throws CodeCheckException {
		return false;
	};

	default void check(Field field) throws CodeCheckException {
	};

	default boolean support(Method method) throws CodeCheckException {
		return false;
	};

	default void check(Method method) throws CodeCheckException {
	};

}
