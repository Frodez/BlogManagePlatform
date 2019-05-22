package frodez.config.validator;

import frodez.util.common.StrUtil;
import frodez.util.reflect.BeanUtil;
import frodez.util.spring.ContextUtil;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
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
							checkField(field);
						}
					}
				}
			}
		}
	}

	private void checkField(Field field) {
		Class<?> type = field.getType();
		if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
			checkCollectionOrMap(field, (ParameterizedType) field.getGenericType());
			return;
		}
		assertComplexPropertyValid(field, field.getType());
	}

	private boolean isComplex(Class<?> type) {
		return !BeanUtils.isSimpleProperty(type);
	}

	private void assertComplexPropertyValid(Field field, Class<?> type) {
		if (isComplex(type) && field.getAnnotation(Valid.class) == null) {
			throw new RuntimeException(StrUtil.concat(field.getDeclaringClass().getName(), ".", field.getName(),
				"是复杂类型,需要加上@", Valid.class.getName(), "注解!"));
		}
	}

	private void checkCollectionOrMap(Field field, ParameterizedType genericType) {
		if (Map.class.isAssignableFrom((Class<?>) genericType.getRawType())) {
			return;
		}
		Type[] actualTypes = genericType.getActualTypeArguments();
		for (Type actualType : actualTypes) {
			//如果是直接类型,直接判断
			if (actualType instanceof Class) {
				assertComplexPropertyValid(field, (Class<?>) actualType);
				continue;
			}
			//如果是泛型类型
			if (actualType instanceof ParameterizedType) {
				checkCollectionOrMap(field, (ParameterizedType) actualType);
				continue;
			}
		}
	}

}
