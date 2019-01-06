package info.frodez.config.mybatis.generator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 * mybatis-generator代码生成配置
 * @author Frodez
 * @date 2018-11-13
 */
public class CustomCommentGenerator extends DefaultCommentGenerator {

	/**
	 * 配置实体类
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.addImportedType("lombok.Data");
		topLevelClass.addImportedType("lombok.NoArgsConstructor");
		topLevelClass.addImportedType("javax.persistence.Id");
		topLevelClass.addImportedType("javax.persistence.Table");
		topLevelClass.addImportedType("javax.persistence.Column");
		topLevelClass.addImportedType("javax.persistence.Entity");
		for (IntrospectedColumn iter : introspectedTable.getAllColumns()) {
			if (!iter.isNullable()) {
				topLevelClass.addImportedType("javax.validation.constraints.NotNull");
			}
		}
		topLevelClass.addJavaDocLine("/**");
		topLevelClass.addJavaDocLine(" * @description "
			+ (introspectedTable.getRemarks() == null ? "" : introspectedTable.getRemarks()));
		topLevelClass.addJavaDocLine(" * @table " + introspectedTable.getFullyQualifiedTable());
		topLevelClass.addJavaDocLine(" * @date " + LocalDate.now().toString());
		topLevelClass.addJavaDocLine(" */");
		topLevelClass.addAnnotation("@Data");
		topLevelClass.addAnnotation("@Entity");
		topLevelClass
			.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTable() + "\")");
		topLevelClass.addAnnotation("@NoArgsConstructor");
	}

	/**
	 * 配置实体类字段
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
		IntrospectedColumn introspectedColumn) {
		String columnName = introspectedColumn.getActualColumnName();
		List<IntrospectedColumn> primaryKey = introspectedTable.getPrimaryKeyColumns();
		for (IntrospectedColumn pk : primaryKey) {
			if (columnName.equals(pk.getActualColumnName())) {
				field.addAnnotation("@Id");
				break;
			}
		}
		field.addJavaDocLine("/** ");
		field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
		String defaultValue = introspectedColumn.getDefaultValue();
		if (!StringUtils.isEmpty(defaultValue)) {
			if (field.getType().getShortName().equals("Byte")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Integer")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Double")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Long")) {
				field.setInitializationString("(long) " + defaultValue + "");
			}
			if (field.getType().getShortName().equals("String")) {
				field.setInitializationString("\"" + defaultValue + "\"");
			}
			if (field.getType().getShortName().equals("BigDecimal")) {
				field.setInitializationString("new BigDecimal(" + defaultValue + ")");
			}
		}
		field.addJavaDocLine(" */");
		if (!introspectedColumn.isNullable()) {
			field.addAnnotation("@NotNull");
		}
		String column = "@Column(name = \"" + introspectedColumn.getActualColumnName() + "\"";
		if (field.getType().getShortName().equals("String")
			|| field.getType().getShortName().equals("BigDecimal")) {
			if (introspectedColumn.getLength() != 0) {
				column = column + ", length = " + introspectedColumn.getLength();
			}
		}
		if (introspectedColumn.getScale() != 0) {
			column = column + ", scale = " + introspectedColumn.getScale();
		}
		column = column + ")";
		field.addAnnotation(column);
	}

}
