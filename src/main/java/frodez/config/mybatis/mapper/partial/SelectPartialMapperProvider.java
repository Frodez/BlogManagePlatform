package frodez.config.mybatis.mapper.partial;

import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class SelectPartialMapperProvider extends MapperTemplate {

	public SelectPartialMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String partialNoCondition(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(tableName).append(".${fieldName} ");
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(SqlHelper.orderByDefault(entityClass));
		return sql.toString();
	}

	public String partialEqual(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(tableName).append(".${fieldName} ");
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

	public String partialOneEqual(MappedStatement ms) {
		return partialEqual(ms);
	}

	public String partialIn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(tableName).append(".${fieldName} ");
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append("<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

	public String partialOneIn(MappedStatement ms) {
		return partialIn(ms);
	}

	public String partialByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(tableName).append(".${fieldName} ");
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
		if (columnList.size() == 1) {
			EntityColumn column = columnList.iterator().next();
			sql.append(" where ");
			sql.append(column.getColumn());
			sql.append(" in ");
			sql.append("<foreach collection=\"ids\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
			sql.append(" #{item} ");
			sql.append("</foreach>");
		} else {
			throw new MapperException("继承 selectByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须有且只有一个带有 @Id 注解的字段");
		}
		return sql.toString();
	}

	public String partialByExample(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder("SELECT ");
		if (isCheckExampleEntityClass()) {
			sql.append("<bind name=\"checkExampleEntityClass\" value=\"@tk.mybatis.mapper.util.OGNL@checkExampleEntityClass(example, '");
			sql.append(entityClass.getCanonicalName());
			sql.append("')\"/>");
		}
		sql.append("<if test=\"example.distinct\">distinct</if> ");
		sql.append(tableName).append(".${fieldName} ");
		sql.append(" FROM ");
		sql.append(SqlHelper.getDynamicTableName(entityClass, tableName));
		sql.append(" ");
		sql.append(SqlHelper.updateByExampleWhereClause());
		sql.append("<if test=\"example.orderByClause != null\">");
		sql.append("order by ${example.orderByClause}");
		sql.append("</if>");
		String orderByClause = EntityHelper.getOrderByClause(entityClass);
		if (orderByClause.length() > 0) {
			sql.append("<if test=\"example.orderByClause == null\">");
			sql.append("ORDER BY " + orderByClause);
			sql.append("</if>");
		}
		sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasForUpdate(example)\">");
		sql.append("FOR UPDATE");
		sql.append("</if>");
		return sql.toString();
	}

}
