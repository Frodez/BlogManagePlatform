package frodez.config.mybatis;

import org.apache.ibatis.type.BigDecimalTypeHandler;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.ByteTypeHandler;
import org.apache.ibatis.type.DoubleTypeHandler;
import org.apache.ibatis.type.FloatTypeHandler;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LongTypeHandler;
import org.apache.ibatis.type.ShortTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer;

@Configuration
public class MybatisConfig {

	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return new ConfigurationCustomizer() {

			@SuppressWarnings("unused")
			private int a = 1;

			@Override
			public void customize(org.apache.ibatis.session.Configuration configuration) {
				TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
				registry.register(Object.class, JdbcType.BIT, new BooleanTypeHandler());
				registry.register(Object.class, JdbcType.BOOLEAN, new BooleanTypeHandler());
				registry.register(Object.class, JdbcType.TINYINT, new ByteTypeHandler());
				registry.register(Object.class, JdbcType.SMALLINT, new ShortTypeHandler());
				registry.register(Object.class, JdbcType.INTEGER, new IntegerTypeHandler());
				registry.register(Object.class, JdbcType.BIGINT, new LongTypeHandler());
				registry.register(Object.class, JdbcType.FLOAT, new FloatTypeHandler());
				registry.register(Object.class, JdbcType.DOUBLE, new DoubleTypeHandler());
				registry.register(Object.class, JdbcType.VARCHAR, new StringTypeHandler());
				registry.register(Object.class, JdbcType.LONGVARCHAR, new StringTypeHandler());
				registry.register(Object.class, JdbcType.CHAR, new StringTypeHandler());
				registry.register(Object.class, JdbcType.REAL, new BigDecimalTypeHandler());
				registry.register(Object.class, JdbcType.DECIMAL, new BigDecimalTypeHandler());
				registry.register(Object.class, JdbcType.NUMERIC, new BigDecimalTypeHandler());
			}
		};
	}

}
