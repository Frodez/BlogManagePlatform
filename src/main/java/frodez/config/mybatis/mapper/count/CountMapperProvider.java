package frodez.config.mybatis.mapper.count;

import java.util.Set;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class CountMapperProvider extends MapperTemplate {

	public CountMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String countAll(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectCount(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		return sql.toString();
	}

	/**
	 * 批量删除
	 * @author Frodez
	 * @date 2019-12-25
	 */
	public String countByIds(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectCount(entityClass));
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
			throw new MapperException("继承 deleteByIds 方法的实体类[" + entityClass.getCanonicalName() + "]中必须有且只有一个带有 @Id 注解的字段");
		}
		return sql.toString();
	}

	public String countEqual(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectCount(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

	public String countIn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectCount(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append("<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

}
