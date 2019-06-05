package frodez.config.code.checker;

import frodez.config.code.rule.CodeCheckRule;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.reflect.BeanUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.springframework.util.Assert;

public class CodeChecker {

	private List<CodeCheckRule> rules;

	private CodeChecker(List<CodeCheckRule> rules) {
		Assert.notNull(rules, "rules must not be null");
		this.rules = rules;
	}

	public boolean needCheck() {
		return !rules.isEmpty();
	}

	public void checkClass(Class<?> klass) throws CodeCheckException {
		Assert.notNull(klass, "klass must not be null");
		for (Field field : BeanUtil.getSetterFields(klass)) {
			checkField(field);
		}
	}

	public void checkField(Field field) throws CodeCheckException {
		Assert.notNull(field, "field must not be null");
		for (CodeCheckRule rule : rules) {
			rule.check(field);
		}
	}

	public void checkMethod(Method method) throws CodeCheckException {
		Assert.notNull(method, "field must not be null");
		for (CodeCheckRule rule : rules) {
			rule.check(method);
		}
	}

	public static CodeCheckerBuilder builder() {
		return new CodeCheckerBuilder();
	}

	public static final class CodeCheckerBuilder {

		private List<CodeCheckRule> rules = new ArrayList<>();

		private CodeCheckerBuilder() {
		}

		public CodeCheckerBuilder addRules(CodeCheckRule... rules) {
			for (CodeCheckRule rule : rules) {
				this.rules.add(rule);
			}
			return this;
		}

		public CodeCheckerBuilder addRules(Collection<CodeCheckRule> rules) {
			this.rules.addAll(rules);
			return this;
		}

		public CodeCheckerBuilder manageAllRules(Consumer<? super CodeCheckRule> action) {
			rules.forEach(action);
			return this;
		}

		public CodeCheckerBuilder removeRules(Predicate<? super CodeCheckRule> filter) {
			rules.removeIf(filter);
			return this;
		}

		public CodeChecker build() {
			return new CodeChecker(rules);
		}

	}

}
