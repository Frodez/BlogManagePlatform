package mybatis;

import frodez.util.common.EmptyUtil;
import java.time.LocalDate;
import java.util.List;
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
		klass.addImportedType("io.swagger.annotations.ApiModel");
		klass.addImportedType("io.swagger.annotations.ApiModelProperty");
		for (IntrospectedColumn iter : table.getAllColumns()) {
			if (iter.isNullable()) {
				klass.addImportedType("org.springframework.lang.Nullable");
				break;
			}
		}
		klass.addJavaDocLine("/**");
		String tableRemark = table.getRemarks() == null ? "" : table.getRemarks();
		klass.addJavaDocLine(" * @description " + tableRemark);
		klass.addJavaDocLine(" * @table " + table.getFullyQualifiedTable());
		klass.addJavaDocLine(" * @date " + LocalDate.now().toString());
		klass.addJavaDocLine(" */");
		klass.addAnnotation("@Data");
		klass.addAnnotation("@Entity");
		String description = tableRemark;
		if (description.endsWith("表")) {
			description = description.substring(0, description.length() - 1).concat("返回数据");
		}
		klass.addAnnotation("@Table(name = \"" + table.getFullyQualifiedTable() + "\")");
		klass.addAnnotation("@ApiModel(description = \"" + description + "\")");
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
		String remark = column.getRemarks();
		for (IntrospectedColumn pk : primaryKey) {
			if (columnName.equals(pk.getActualColumnName())) {
				field.addAnnotation("@Id");
				String tableRemark = table.getRemarks();
				if (tableRemark.endsWith("表")) {
					tableRemark = tableRemark.substring(0, tableRemark.length() - 1);
				}
				remark = tableRemark + remark;
				break;
			}
		}
		field.addJavaDocLine("/** ");
		String javadocRemark = remark;
		String defaultValue = column.getDefaultValue();
		if (!column.isNullable() && EmptyUtil.no(defaultValue)) {
			javadocRemark = javadocRemark + "(不能为空,默认值:" + defaultValue + ")";
		}
		if (!column.isNullable() && EmptyUtil.yes(defaultValue)) {
			javadocRemark = javadocRemark + "(不能为空)";
		}
		if (column.isNullable() && EmptyUtil.no(defaultValue)) {
			javadocRemark = javadocRemark + "(默认值:" + defaultValue + ")";
		}
		field.addJavaDocLine(" * " + javadocRemark);
		if (EmptyUtil.no(defaultValue)) {
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
		if (column.isNullable()) {
			field.addAnnotation("@Nullable");
		}
		String columnAnnotation = "@Column(name = \"" + column.getActualColumnName() + "\"";
		if (field.getType().getShortName().equals("String") || field.getType().getShortName().equals("BigDecimal")) {
			if (column.getLength() != 0) {
				columnAnnotation = columnAnnotation + ", length = " + column.getLength();
			}
		}
		if (column.getScale() != 0) {
			columnAnnotation = columnAnnotation + ", scale = " + column.getScale();
		}
		columnAnnotation = columnAnnotation + ")";
		field.addAnnotation(columnAnnotation);
		String apiModelProperty = "@ApiModelProperty(value = \"" + remark + "\"";
		if (EmptyUtil.no(defaultValue)) {
			apiModelProperty = apiModelProperty + ", example = \"" + defaultValue + "\"";
		}
		apiModelProperty = apiModelProperty + ")";
		field.addAnnotation(apiModelProperty);
	}

}
