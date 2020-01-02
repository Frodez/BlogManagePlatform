package frodez.config.code;

import frodez.config.code.checker.CodeChecker;
import frodez.config.code.rule.CodeCheckRule;
import frodez.config.validator.ValidatorProperties;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.ContextUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("contextUtil")
public class CodeCheckerConfiguration {

	@Value("${spring.profiles.active}")
	private List<String> actives;

	@Autowired
	private ValidatorProperties properties;

	@Bean("hibernateValidatorCodeChecker")
	public CodeChecker defaultHibernateValidatorChecker() {
		boolean needCheck = actives.stream().anyMatch((active) -> {
			return properties.getEnviroments().contains(active);
		});
		if (needCheck) {
			return CodeChecker.builder().addRules(rules()).build();
		} else {
			return CodeChecker.builder().build();
		}
	}

	private List<CodeCheckRule> rules() {
		List<CodeCheckRule> rules = new ArrayList<>();
		List<Class<? extends CodeCheckRule>> ruleClasses = ContextUtil.classes(properties.getRulePath(), CodeCheckRule.class);
		for (Class<? extends CodeCheckRule> klass : ruleClasses) {
			rules.add(ReflectUtil.instance(klass));
		}
		return rules;
	}

}
