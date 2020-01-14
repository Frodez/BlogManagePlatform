package frodez.config.swagger.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.aop.validation.annotation.common.MapEnum.MapEnumHelper;
import frodez.config.aop.validation.annotation.common.Match;
import frodez.config.aop.validation.annotation.special.IdCard;
import frodez.config.aop.validation.annotation.special.Mobile;
import frodez.config.swagger.SwaggerProperties;
import frodez.constant.settings.DefStr;
import frodez.util.common.EmptyUtil;
import frodez.util.common.StrUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.reflect.TypeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 自动处理字段描述
 * @author Frodez
 * @date 2019-12-14
 */
@Component
@Profile({ "dev", "test" })
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 300)
public class DefaultModelPlugin implements ModelPropertyBuilderPlugin {

	private boolean useCustomerizedPluggins = false;

	@Autowired
	public DefaultModelPlugin(SwaggerProperties properties) {
		this.useCustomerizedPluggins = properties.getUseCustomerizedPluggins();
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return useCustomerizedPluggins;
	}

	@Override
	public void apply(ModelPropertyContext context) {
		BeanPropertyDefinition definition = context.getBeanPropertyDefinition().orNull();
		if (definition == null) {
			return;
		}
		Class<?> modelClass = definition.getAccessor().getDeclaringClass();
		Field field = definition.getField().getAnnotated();
		if (!modelClass.isAnnotationPresent(ApiModel.class)) {
			return;
		}
		resolveDescription(context, field);
		resolveMapEnum(context, field);
		resolveExample(context, modelClass, field);
	}

	private void resolveMapEnum(ModelPropertyContext context, Field field) {
		MapEnum legalEnum = field.getAnnotation(MapEnum.class);
		if (legalEnum != null) {
			String descs = MapEnumHelper.getDescs(legalEnum.value(), legalEnum.descMethod());
			Object defaultValue = MapEnumHelper.getDefaultValue(legalEnum.value());
			ModelPropertyBuilder builder = context.getBuilder();
			builder.description(descs);
			builder.example(defaultValue);
			builder.allowableValues(MapEnumHelper.getAllowableValues(legalEnum.value()));
			builder.defaultValue(defaultValue == null ? DefStr.EMPTY : defaultValue.toString());
		}
	}

	private void resolveExample(ModelPropertyContext context, Class<?> modelClass, Field field) {
		Object retVal = ReflectUtil.get(field, ReflectUtil.instance(modelClass));
		if (retVal != null) {
			ModelPropertyBuilder builder = context.getBuilder();
			ApiModelProperty modelProperty = field.getAnnotation(ApiModelProperty.class);
			if (modelProperty != null && EmptyUtil.no(modelProperty.example())) {
				return;
			}
			builder.example(retVal);
			builder.defaultValue(String.valueOf(retVal));
		}
	}

	private void resolveDescription(ModelPropertyContext context, Field field) {
		ModelPropertyBuilder builder = context.getBuilder();
		String description = StrUtil.orEmpty((String) ReflectUtil.get(ModelPropertyBuilder.class, "description", builder));
		String resolve = recursiveResolveList(TypeUtil.resolve(field.getGenericType()));
		if (resolve != null) {
			//如果还是有ApiModelProperty,就不在原描述后增加列表的描述
			if (!field.isAnnotationPresent(ApiModelProperty.class)) {
				description = StrUtil.concat(description, resolve);
			}
		}
		resolve = resolveRange(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveSize(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveRegex(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveDate(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveNull(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveEmail(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveIDCard(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		resolve = resolveMobile(field);
		if (resolve != null) {
			description = String.join(",", description, resolve);
		}
		builder.description(description);
	}

	private String recursiveResolveList(ResolvedType resolvedType) {
		if (TypeUtil.isSimpleType(resolvedType)) {
			ApiModel apiModel = resolvedType.getErasedType().getAnnotation(ApiModel.class);
			if (apiModel != null) {
				return apiModel.description();
			} else {
				return "";
			}
		} else if (TypeUtil.belongToComplexType(Collection.class, resolvedType)) {
			resolvedType = TypeUtil.resolveGenericType(Collection.class, resolvedType).get(0);
			String result = recursiveResolveList(resolvedType);
			if (result == null) {
				return null;
			} else {
				return StrUtil.concat(result, "的列表");
			}
		} else {
			return null;
		}
	}

	private String resolveRange(Field field) {
		String min = Optional.ofNullable(field.getAnnotation(Min.class)).map((item) -> String.valueOf(item.value())).orElse(null);
		if (min == null) {
			min = Optional.ofNullable(field.getAnnotation(DecimalMin.class)).map(DecimalMin::value).orElse(null);
		}
		if (field.isAnnotationPresent(PositiveOrZero.class)) {
			if (min == null || new BigDecimal(min).compareTo(BigDecimal.ZERO) < 0) {
				min = "非负数";
			}
		}
		if (field.isAnnotationPresent(Positive.class)) {
			if (min == null || new BigDecimal(min).compareTo(BigDecimal.ZERO) <= 0) {
				min = "正数";
			}
		}
		min = min == null ? null : StrUtil.concat("最小值为", min);
		if (field.isAnnotationPresent(DecimalMin.class)) {
			if (!field.getAnnotation(DecimalMin.class).inclusive()) {
				min = min == null ? null : StrUtil.concat(min, "且不能等于最小值");
			}
		}
		String max = Optional.ofNullable(field.getAnnotation(Max.class)).map((item) -> String.valueOf(item.value())).orElse(null);
		if (max == null) {
			max = Optional.ofNullable(field.getAnnotation(DecimalMax.class)).map(DecimalMax::value).orElse(null);
		}
		if (field.isAnnotationPresent(NegativeOrZero.class)) {
			if (max == null || new BigDecimal(max).compareTo(BigDecimal.ZERO) > 0) {
				max = "非正数";
			}
		}
		if (field.isAnnotationPresent(Negative.class)) {
			if (max == null || new BigDecimal(min).compareTo(BigDecimal.ZERO) >= 0) {
				max = "负数";
			}
		}
		max = max == null ? null : StrUtil.concat("最大值为", max);
		if (field.isAnnotationPresent(DecimalMax.class)) {
			if (!field.getAnnotation(DecimalMax.class).inclusive()) {
				min = min == null ? null : StrUtil.concat(min, "且不能等于最大值");
			}
		}
		String digit = Optional.ofNullable(field.getAnnotation(Digits.class)).map((item) -> {
			String integer = String.valueOf(item.integer());
			String fraction = String.valueOf(item.fraction());
			return StrUtil.concat("整数位", integer, ",", "小数位", fraction);
		}).orElse(null);
		if (min == null && max == null && digit == null) {
			return null;
		}
		return StrUtil.join(",", min, max, digit);
	}

	private String resolveSize(Field field) {
		Size size = field.getAnnotation(Size.class);
		if (size != null) {
			if (size.min() == 0) {
				return StrUtil.concat("长度不能大于", String.valueOf(size.max()));
			} else {
				return StrUtil.concat("长度不能小于", String.valueOf(size.min()), "且不能大于", String.valueOf(size.max()));
			}
		}
		Length length = field.getAnnotation(Length.class);
		if (length != null) {
			if (length.min() == 0) {
				return StrUtil.concat("长度不能大于", String.valueOf(length.max()));
			} else {
				return StrUtil.concat("长度不能小于", String.valueOf(length.min()), "且不能大于", String.valueOf(length.max()));
			}
		}
		return null;
	}

	private String resolveRegex(Field field) {
		Pattern pattern = field.getAnnotation(Pattern.class);
		if (pattern != null) {
			return StrUtil.concat("必须满足", pattern.regexp(), "的正则表达式");
		}
		Match match = field.getAnnotation(Match.class);
		if (match != null) {
			return StrUtil.concat("必须满足", match.value(), "的正则表达式");
		}
		return null;
	}

	private String resolveDate(Field field) {
		Future future = field.getAnnotation(Future.class);
		if (future != null) {
			return "必须为未来的时间";
		}
		FutureOrPresent futureOrPresent = field.getAnnotation(FutureOrPresent.class);
		if (futureOrPresent != null) {
			return "必须为未来或现在的时间";
		}
		Past past = field.getAnnotation(Past.class);
		if (past != null) {
			return "必须为过去的时间";
		}
		PastOrPresent pastOrPresent = field.getAnnotation(PastOrPresent.class);
		if (pastOrPresent != null) {
			return "必须为过去或现在的时间";
		}
		return null;
	}

	private String resolveNull(Field field) {
		return field.isAnnotationPresent(Null.class) ? "必须为空" : null;
	}

	private String resolveEmail(Field field) {
		return field.isAnnotationPresent(Email.class) ? "必须为合法的email" : null;
	}

	private String resolveIDCard(Field field) {
		return field.isAnnotationPresent(IdCard.class) ? "必须为合法的身份证号" : null;
	}

	private String resolveMobile(Field field) {
		return field.isAnnotationPresent(Mobile.class) ? "必须为合法的手机号" : null;
	}

}
