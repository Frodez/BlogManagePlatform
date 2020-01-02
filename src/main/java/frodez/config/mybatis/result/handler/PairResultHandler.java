package frodez.config.mybatis.result.handler;

import com.fasterxml.classmate.ResolvedType;
import frodez.config.mybatis.result.CustomHandler;
import frodez.util.beans.pair.Pair;
import frodez.util.reflect.TypeUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;

/**
 * Pair自定义ResultHandler<br>
 * <strong> 使用须知:<br>
 * 1.resultType必须设置为java.util.HashMap!!!否则本Handler无效!<br>
 * 2.请在返回值字段名前添加key和value两种关键词,会与Pair中的K和V相对应。关键词与原字段名间请用.号连接。<br>
 * 3.如果key或者value的字段只有一个,则可以直接用key或者value代替。<br>
 * </strong>
 * @author Frodez
 * @date 2019-12-28
 */
public class PairResultHandler implements CustomHandler {

	private PairResultHandlerContext context;

	private List<Pair<?, ?>> many = new ArrayList<>();

	@Override
	public boolean support(List<ResultMap> resultMaps) {
		ResultMap map = resultMaps.get(0);
		return Map.class.isAssignableFrom(map.getType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext<? extends Object> resultContext) {
		Object object = resultContext.getResultObject();
		if (Map.class.isAssignableFrom(object.getClass())) {
			Map<String, Object> map = (Map<String, Object>) object;
			Object key = context.key.resolve(map.get("key"));
			Object value = context.value.resolve(map.get("value"));
			Pair<?, ?> pair = new Pair<>(key, value);
			many.add(pair);
		}
	}

	@Override
	public List<?> getResult() {
		return many;
	}

	/**
	 * 设置上下文
	 * @author Frodez
	 * @date 2019-12-28
	 */
	@Override
	public void setContext(CustomHandlerContext context) {
		this.context = (PairResultHandlerContext) context;
	}

	/**
	 * 获取泛型的具体类型
	 * @author Frodez
	 * @date 2019-12-27
	 */
	@Override
	public CustomHandlerContext resolveContext(Method method, Configuration configuration) {
		ResolvedType resolvedType = TypeUtil.resolve(method.getGenericReturnType());
		PairResultHandlerContext context = new PairResultHandlerContext();
		ResolvedType keyType;
		ResolvedType valueType;
		if (TypeUtil.belongToSimpleType(Pair.class, resolvedType)) {
			List<ResolvedType> pairType = TypeUtil.resolveGenericType(Pair.class, resolvedType);
			keyType = pairType.get(0);
			valueType = pairType.get(1);
			if (!TypeUtil.isSimpleType(keyType) || !TypeUtil.isSimpleType(valueType)) {
				error(resolvedType);
				return null;
			}
		} else if (TypeUtil.belongToComplexType(List.class, resolvedType)) {
			resolvedType = TypeUtil.resolveGenericType(List.class, resolvedType).get(0);
			if (!TypeUtil.belongToSimpleType(Pair.class, resolvedType)) {
				error(resolvedType);
				return null;
			}
			List<ResolvedType> pairType = TypeUtil.resolveGenericType(Pair.class, resolvedType);
			keyType = pairType.get(0);
			valueType = pairType.get(1);
			if (!TypeUtil.isSimpleType(keyType) || !TypeUtil.isSimpleType(valueType)) {
				error(resolvedType);
				return null;
			}
		} else {
			error(resolvedType);
			return null;
		}
		context.key = TypeCondition.generate(keyType.getErasedType(), configuration);
		context.value = TypeCondition.generate(valueType.getErasedType(), configuration);
		return context;
	}

	private void error(ResolvedType resolvedType) {
		throw new IllegalArgumentException("不支持把" + resolvedType.getBriefDescription() + "转换为Pair<K,V>或者List<Pair<K,V>>!");
	}

	private class PairResultHandlerContext implements CustomHandlerContext {

		private TypeCondition key;

		private TypeCondition value;

	}

}
