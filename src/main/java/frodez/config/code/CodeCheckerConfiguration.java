package frodez.config.code;

import frodez.config.code.checker.CodeChecker;
import frodez.config.code.rule.CodeCheckRule;
import frodez.config.code.rule.hibernate.CheckRule;
import frodez.config.code.rule.hibernate.LegalEnumRule;
import frodez.config.code.rule.hibernate.ValidRule;
import frodez.config.validator.ValidatorProperties;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
			return CodeChecker.builder().addRules(defaultHibernateValidatorStrategy()).build();
		} else {
			return CodeChecker.builder().build();
		}
	}

	private static List<CodeCheckRule> defaultHibernateValidatorStrategy() {
		List<CodeCheckRule> rules = new ArrayList<>();
		rules.add(new CheckRule());
		rules.add(new LegalEnumRule());
		rules.add(new ValidRule());
		return rules;
	}

}
