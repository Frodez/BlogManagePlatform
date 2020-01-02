package frodez.config.mybatis.mapper.ids;

import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class IdsMapperProvider extends MapperTemplate {

	public IdsMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 批量查询是否存在
	 * @author Frodez
	 * @date 2019-12-25
	 */
	public String existByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		//将返回值修改为实体类型
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CASE WHEN ");
		Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
		if (pkColumns.size() == 1) {
			sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
		} else {
			sql.append("COUNT(*) ");
		}
		sql.append(" = ${ids.size} THEN 1 ELSE 0 END AS result ");
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		if (pkColumns.size() == 1) {
			EntityColumn column = pkColumns.iterator().next();
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

	/**
	 * 批量查询
	 * @author Frodez
	 * @date 2019-12-25
	 */
	public String selectByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		//将返回值修改为实体类型
		setResultType(ms, entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
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

	/**
	 * 批量删除
	 * @author Frodez
	 * @date 2019-12-25
	 */
	public String deleteByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
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
			throw new MapperException("继承 deleteByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须有且只有一个带有 @Id 注解的字段");
		}
		return sql.toString();
	}

}
