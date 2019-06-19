package frodez.config.validator;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.code.checker.CodeChecker;
import frodez.util.spring.ContextUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * hibernate-validator代码检查器<br>
 * 在validator相关配置中配置开启检查,则会对指定的实体进行类型检查.<br>
 * 检查要求实体中类型是复杂类型(即同样的业务类型,准确来说,需要使用@Valid注解进行级联检查的类型)的字段,必须拥有@Valid注解.<br>
 * 否则抛出异常.
 * @author Frodez
 * @date 2019-5-22
 */
@Slf4j
@Component
public class ValidateCodeChecker implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	private ValidatorProperties properties;

	@Autowired
	@Qualifier("hibernateValidatorCodeChecker")
	private CodeChecker codeChecker;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		try {
			if (codeChecker.needCheck()) {
				log.info("[frodez.config.validator.ValidateCodeChecker.ValidateChecker]hibernate-validator代码校验开始");
				check();
				log.info("[frodez.config.validator.ValidateCodeChecker.ValidateChecker]hibernate-validator代码校验结束");
			} else {
				log.info("[frodez.config.validator.ValidateCodeChecker.ValidateChecker]未开启hibernate-validator代码校验功能");
			}
		} catch (IOException | ClassNotFoundException | LinkageError e) {
			log.error("[frodez.config.validator.ValidateCodeChecker.ValidateChecker]发生错误,程序终止", e);
			ContextUtil.exit();
		}
	}

	private void check() throws IOException, ClassNotFoundException, LinkageError {
		for (String path : properties.getModelPath()) {
			for (Class<?> klass : ContextUtil.classes(path)) {
				if (klass.getAnnotation(ValidateBean.class) != null) {
					codeChecker.checkClass(klass);
				}
			}
		}
	}

}
