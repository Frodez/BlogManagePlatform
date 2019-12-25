package frodez.config.mybatis.in;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class InMapperProvider extends MapperTemplate {

	public InMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String selectIn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		//将返回值修改为实体类型
		setResultType(ms, entityClass);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append("<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

	public String selectOneIn(MappedStatement ms) {
		return selectIn(ms);
	}

	public String deleteIn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append("<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

	public String updateIn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName));
		sql.append(SqlHelper.updateSetColumns(entityClass, null, false, false));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append("<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

	public String updateInSelective(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName));
		sql.append(SqlHelper.updateSetColumns(entityClass, null, true, isNotEmpty()));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" in ");
		sql.append("<foreach collection=\"params\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sql.append(" #{item} ");
		sql.append("</foreach>");
		return sql.toString();
	}

}
