package frodez.config.swagger.util;

import static springfox.documentation.spi.schema.contexts.ModelContext.returnValue;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.ImmutableSet;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.config.swagger.plugin.SuccessPlugin.SwaggerModel;
import frodez.util.beans.result.PageData;
import frodez.util.beans.result.Result.ResultEnum;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.OperationContext;

/**
 * swagger工具类
 * @author Frodez
 * @date 2019-12-05
 */
@UtilityClass
public class SwaggerUtil {

	private static Function<? super ResultEnum, ? extends String> mapper = (iter) -> {
		return iter.getDesc() + ",自定义状态码:" + iter.getVal();
	};

	/**
	 * 从OperationContext转换出ModelContext
	 * @author Frodez
	 * @date 2019-12-15
	 */
	public static ModelContext resolveModelContext(OperationContext context, Type type) {
		String group = context.getGroupName();
		DocumentationType documentation = context.getDocumentationType();
		AlternateTypeProvider provider = context.getAlternateTypeProvider();
		GenericTypeNamingStrategy strategy = context.getGenericsNamingStrategy();
		@SuppressWarnings("rawtypes") ImmutableSet<Class> types = context.getIgnorableParameterTypes();
		return returnValue(group, type, documentation, provider, strategy, types);
	}

	/**
	 * 生成说明
	 * @param status 需要生成说明的状态
	 * @author Frodez
	 * @date 2019-12-05
	 */
	public static String statusDescription(Collection<ResultEnum> status) {
		Assert.notNull(status, "predicate must not be null");
		List<String> strings = status.stream().map(mapper).collect(Collectors.toList());
		return String.join(" | ", strings);
	}

	/**
	 * 生成说明
	 * @param predicate 筛选需要生成说明的状态
	 * @author Frodez
	 * @date 2019-12-05
	 */
	public static String statusDescription(Predicate<? super ResultEnum> predicate) {
		Assert.notNull(predicate, "predicate must not be null");
		List<String> strings = Stream.of(ResultEnum.values()).filter(predicate).map(mapper).collect(Collectors.toList());
		return String.join(" | ", strings);
	}

	/**
	 * 解析类型
	 * @author Frodez
	 * @date 2019-12-05
	 */
	public static ResolvedType resolvedType(TypeResolver resolver, Success success) {
		Assert.notNull(resolver, "resolver must not be null");
		Assert.notNull(success, "success must not be null");
		Class<?> response = success.value();
		if (Void.class != response && void.class != response) {
			Container containerType = success.containerType();
			if (containerType == Container.PAGE) {
				ResolvedType type = resolver.resolve(SwaggerModel.class, resolver.resolve(PageData.class, response));
				return type;
			} else if (containerType == Container.LIST) {
				ResolvedType type = resolver.resolve(SwaggerModel.class, resolver.resolve(List.class, response));
				return type;
			} else if (containerType == Container.SET) {
				ResolvedType type = resolver.resolve(SwaggerModel.class, resolver.resolve(Set.class, response));
				return type;
			} else {
				ResolvedType type = resolver.resolve(SwaggerModel.class, response);
				return type;
			}
		}
		return null;
	}

}
