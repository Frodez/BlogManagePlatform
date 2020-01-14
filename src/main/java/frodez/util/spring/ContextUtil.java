package frodez.util.spring;

import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import frodez.util.common.StreamUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * spring工具类
 * @author Frodez
 * @date 2018-12-21
 */
@Component("contextUtil")
public class ContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		Assert.notNull(context, "context must not be null");
	}

	/**
	 * 关闭spring应用,默认返回码1
	 * @author Frodez
	 * @date 2019-06-04
	 */
	public static void exit() {
		exit(1);
	}

	/**
	 * 关闭spring应用
	 * @param exitCode 返回码
	 * @author Frodez
	 * @date 2019-06-04
	 */
	public static void exit(int exitCode) {
		SpringApplication.exit(context, () -> exitCode);
	}

	/**
	 * 获取spring上下文环境
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static ApplicationContext context() {
		return context;
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static <T> T bean(Class<T> klass) {
		return context.getBean(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 * @param <T>
	 * @date 2018-12-21
	 */
	public static <T> Map<String, T> beans(Class<T> klass) {
		return context.getBeansOfType(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param beanName bean的名字
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@SuppressWarnings("unchecked")
	public static <T> T bean(String beanName, Class<T> klass) {
		return (T) context.getBean(beanName);
	}

	/**
	 * 根据ant风格模式字符串匹配路径,并获取路径下所有类
	 * @author Frodez
	 * @throws LinkageError
	 * @throws ClassNotFoundException
	 * @date 2019-05-23
	 */
	@SneakyThrows
	public static List<Class<?>> classes(String pattern) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = StreamUtil.filterList(resolver.getResources(getPackagePath(pattern)), Resource::isReadable);
		List<Class<?>> classes = new ArrayList<>();
		MetadataReaderFactory readerFactory = bean(MetadataReaderFactory.class);
		for (Resource resource : resources) {
			classes.add(ClassUtils.forName(readerFactory.getMetadataReader(resource).getClassMetadata().getClassName(), null));
		}
		return classes;
	}

	/**
	 * 根据ant风格模式字符串匹配路径,并获取路径下所有指定父类的类
	 * @author Frodez
	 * @param <T>
	 * @throws LinkageError
	 * @throws ClassNotFoundException
	 * @date 2019-05-23
	 */
	@SuppressWarnings("unchecked")
	@SneakyThrows
	public static <T> List<Class<? extends T>> classes(String pattern, Class<T> parent) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = StreamUtil.filterList(resolver.getResources(getPackagePath(pattern)), Resource::isReadable);
		List<Class<? extends T>> classes = new ArrayList<>();
		MetadataReaderFactory readerFactory = bean(MetadataReaderFactory.class);
		for (Resource resource : resources) {
			Class<?> klass = ClassUtils.forName(readerFactory.getMetadataReader(resource).getClassMetadata().getClassName(), null);
			if (parent.isAssignableFrom(klass)) {
				classes.add((Class<? extends T>) klass);
			}
		}
		return classes;
	}

	/**
	 * 根据ant风格模式字符串匹配路径,并获取路径下所有满足要求的类
	 * @author Frodez
	 * @throws LinkageError
	 * @throws ClassNotFoundException
	 * @date 2019-05-23
	 */
	@SneakyThrows
	public static List<Class<?>> classes(String pattern, Predicate<Class<?>> predicate) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = StreamUtil.filterList(resolver.getResources(getPackagePath(pattern)), Resource::isReadable);
		List<Class<?>> classes = new ArrayList<>();
		MetadataReaderFactory readerFactory = bean(MetadataReaderFactory.class);
		for (Resource resource : resources) {
			Class<?> klass = ClassUtils.forName(readerFactory.getMetadataReader(resource).getClassMetadata().getClassName(), null);
			if (predicate.test(klass)) {
				classes.add(klass);
			}
		}
		return classes;
	}

	/**
	 * 转换成包名,ant风格
	 * @author Frodez
	 * @date 2019-12-14
	 */
	public static String getPackagePath(String pattern) {
		String packagePath = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(pattern));
		return StrUtil.concat(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX, packagePath, DefStr.CLASS_SUFFIX);
	}

}
