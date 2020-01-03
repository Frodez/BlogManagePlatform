package frodez.config.mybatis.mapper.equal;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class EqualMapperProvider extends MapperTemplate {

	public EqualMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String selectEqual(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		//将返回值修改为实体类型
		setResultType(ms, entityClass);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.selectAllColumns(entityClass));
		sql.append(SqlHelper.fromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

	public String selectOneEqual(MappedStatement ms) {
		return selectEqual(ms);
	}

	public String deleteEqual(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

	public String updateEqual(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName));
		sql.append(SqlHelper.updateSetColumns(entityClass, null, false, false));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

	public String updateEqualSelective(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		String tableName = tableName(entityClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName));
		sql.append(SqlHelper.updateSetColumns(entityClass, null, true, isNotEmpty()));
		sql.append(" where ");
		sql.append(tableName).append(".${paramName}");
		sql.append(" = ");
		sql.append(" #{param}");
		return sql.toString();
	}

}
