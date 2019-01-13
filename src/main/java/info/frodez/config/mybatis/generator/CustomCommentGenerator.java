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
	public void addModelClassComment(TopLevelClass klass, IntrospectedTable table) {
		klass.addImportedType("lombok.Data");
		klass.addImportedType("javax.persistence.Id");
		klass.addImportedType("javax.persistence.Table");
		klass.addImportedType("javax.persistence.Column");
		klass.addImportedType("javax.persistence.Entity");
		for (IntrospectedColumn iter : table.getAllColumns()) {
			if (!iter.isNullable()) {
				klass.addImportedType("javax.validation.constraints.NotNull");
			}
		}
		klass.addJavaDocLine("/**");
		klass.addJavaDocLine(" * @description " + (table.getRemarks() == null ? "" : table.getRemarks()));
		klass.addJavaDocLine(" * @table " + table.getFullyQualifiedTable());
		klass.addJavaDocLine(" * @date " + LocalDate.now().toString());
		klass.addJavaDocLine(" */");
		klass.addAnnotation("@Data");
		klass.addAnnotation("@Entity");
		klass.addAnnotation("@Table(name = \"" + table.getFullyQualifiedTable() + "\")");
	}

	/**
	 * 配置实体类字段
	 * @author Frodez
	 * @date 2018-12-13
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable table, IntrospectedColumn column) {
		String columnName = column.getActualColumnName();
		List<IntrospectedColumn> primaryKey = table.getPrimaryKeyColumns();
		for (IntrospectedColumn pk : primaryKey) {
			if (columnName.equals(pk.getActualColumnName())) {
				field.addAnnotation("@Id");
				break;
			}
		}
		field.addJavaDocLine("/** ");
		field.addJavaDocLine(" * " + column.getRemarks());
		String defaultValue = column.getDefaultValue();
		if (!StringUtils.isEmpty(defaultValue)) {
			if (field.getType().getShortName().equals("Byte")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Short")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Integer")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Double")) {
				field.setInitializationString(defaultValue);
			}
			if (field.getType().getShortName().equals("Long")) {
				field.setInitializationString(defaultValue + "L");
			}
			if (field.getType().getShortName().equals("String")) {
				field.setInitializationString("\"" + defaultValue + "\"");
			}
			if (field.getType().getShortName().equals("BigDecimal")) {
				field.setInitializationString("new BigDecimal(" + defaultValue + ")");
			}
		}
		field.addJavaDocLine(" */");
		if (!column.isNullable()) {
			field.addAnnotation("@NotNull");
		}
		String columnAnnotation = "@Column(name = \"" + column.getActualColumnName() + "\"";
		if (field.getType().getShortName().equals("String")
			|| field.getType().getShortName().equals("BigDecimal")) {
			if (column.getLength() != 0) {
				columnAnnotation = columnAnnotation + ", length = " + column.getLength();
			}
		}
		if (column.getScale() != 0) {
			columnAnnotation = columnAnnotation + ", scale = " + column.getScale();
		}
		columnAnnotation = columnAnnotation + ")";
		field.addAnnotation(columnAnnotation);
	}

}
