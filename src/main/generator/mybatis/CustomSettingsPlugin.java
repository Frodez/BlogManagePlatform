package mybatis;

import frodez.config.mybatis.DataMapper;
import java.time.LocalDate;
import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * mybatis-generator参数配置插件
 * @author Frodez
 * @date 2018-11-13
 */
public class CustomSettingsPlugin extends PluginAdapter {

	/**
	 * 基础Mapper,要与application.yml中mapper.mappers属性对应
	 */
	private static final String MAPPER_NAME = DataMapper.class.getName();

	/**
	 * 配置生成的Mapper接口
	 * @param i
	 * @param klass
	 * @param table
	 * @author Frodez
	 * @date 2018-12-13
	 */
	private void configImportAndJavaDoc(Interface i, TopLevelClass klass, IntrospectedTable table) {
		// 获取实体类
		FullyQualifiedJavaType entity = new FullyQualifiedJavaType(table.getBaseRecordType());
		// import接口
		i.addImportedType(new FullyQualifiedJavaType(MAPPER_NAME));
		i.addSuperInterface(new FullyQualifiedJavaType(MAPPER_NAME + "<" + entity.getShortName() + ">"));
		// import实体类
		i.addImportedType(entity);
		// import Spring Repository注解
		i.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
		i.addAnnotation("@Repository");
		i.addJavaDocLine("/**");
		i.addJavaDocLine(" * @description " + (table.getRemarks() == null ? "" : table.getRemarks()));
		i.addJavaDocLine(" * @table " + table.getFullyQualifiedTable());
		i.addJavaDocLine(" * @date " + LocalDate.now().toString());
		i.addJavaDocLine(" */");
	}

	/**
	 * 配置序列化
	 * @author Frodez
	 * @date 2019-01-13
	 */
	private void makeSerializable(TopLevelClass klass, IntrospectedTable table) {
		klass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
		klass.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("1L");
		field.setName("serialVersionUID");
		field.addJavaDocLine("");
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("long"));
		field.setVisibility(JavaVisibility.PRIVATE);
		if (table.getTargetRuntime() == TargetRuntime.MYBATIS3_DSQL) {
			context.getCommentGenerator().addFieldAnnotation(field, table, klass.getImportedTypes());
		} else {
			context.getCommentGenerator().addFieldComment(field, table);
		}
		klass.getFields().add(0, field);
	}

	@Override
	public boolean clientGenerated(Interface i, TopLevelClass klass, IntrospectedTable table) {
		configImportAndJavaDoc(i, klass, table);
		return true;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass klass, IntrospectedTable table) {
		makeSerializable(klass, table);
		return true;
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass klass, IntrospectedTable table) {
		makeSerializable(klass, table);
		return true;
	}

	@Override
	public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass klass, IntrospectedTable table) {
		makeSerializable(klass, table);
		return true;
	}

	// 开启验证
	@Override
	public boolean validate(List<String> arg0) {
		return true;
	}

	// 不生成setter
	@Override
	public boolean modelSetterMethodGenerated(Method method, TopLevelClass klass, IntrospectedColumn column,
		IntrospectedTable table, ModelClassType modelClassType) {
		return false;
	}

	// 不生成getter
	@Override
	public boolean modelGetterMethodGenerated(Method method, TopLevelClass klass, IntrospectedColumn column,
		IntrospectedTable table, ModelClassType modelClassType) {
		return false;
	}

	// 下面所有return false的方法都不生成。这些都是基础的CURD方法，使用通用Mapper实现
	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass klass,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, TopLevelClass klass, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass klass, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass klass,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass klass,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass klass,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass klass,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface i, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface i, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface i, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface i, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass klass, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface i, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface i,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface i,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface i,
		IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean providerGenerated(TopLevelClass klass, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean providerApplyWhereMethodGenerated(Method method, TopLevelClass klass, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean providerInsertSelectiveMethodGenerated(Method method, TopLevelClass klass, IntrospectedTable table) {
		return false;
	}

	@Override
	public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass klass,
		IntrospectedTable table) {
		return false;
	}

}
