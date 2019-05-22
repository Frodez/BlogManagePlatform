package frodez.config.validator;

import frodez.util.reflect.BeanUtil;
import frodez.util.spring.ContextUtil;
import java.io.IOException;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

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

	/**
	 * class文件后缀
	 */
	private static final String CLASS_SUFFIX = "**/*.class";

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		try {
			ValidatorProperties properties = ContextUtil.get(ValidatorProperties.class);
			if (properties.getCodeReview()) {
				log.info("[ValidateChecker]hibernate-validator代码校验开始");
				check(properties);
				log.info("[ValidateChecker]hibernate-validator代码校验结束");
			} else {
				log.info("[ValidateChecker]hibernate-validator代码校验已关闭");
			}
		} catch (IOException | ClassNotFoundException | LinkageError e) {
			log.error("[ValidateChecker]", e);
		}
	}

	private void check(ValidatorProperties properties) throws IOException, ClassNotFoundException, LinkageError {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
		for (String path : properties.getModelPath()) {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils
				.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(path)) + CLASS_SUFFIX;
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					String className = metadataReaderFactory.getMetadataReader(resource).getClassMetadata()
						.getClassName();
					Class<?> klass = ClassUtils.forName(className, null);
					if (!BeanUtils.isSimpleProperty(klass)) {
						for (Field field : BeanUtil.getSetterFields(klass)) {
							CodeCheckUtil.checkField(field);
						}
					}
				}
			}
		}
	}

}
