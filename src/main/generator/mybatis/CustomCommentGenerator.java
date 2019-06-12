package mybatis;

import frodez.util.common.EmptyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.springframework.lang.Nullable;

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
		resolveImports(klass, table);
		resolveEntityJavadoc(klass, table);
		resolveEntityAnnotation(klass, table);
	}

	private void resolveImports(TopLevelClass klass, IntrospectedTable table) {
		klass.addImportedType(Data.class.getCanonicalName());
		klass.addImportedType(Id.class.getCanonicalName());
		klass.addImportedType(Table.class.getCanonicalName());
		klass.addImportedType(Column.class.getCanonicalName());
		klass.addImportedType(Entity.class.getCanonicalName());
		klass.addImportedType(ApiModel.class.getCanonicalName());
		klass.addImportedType(ApiModelProperty.class.getCanonicalName());
		boolean hasNullable = false;
		boolean hasNotNull = false;
		for (IntrospectedColumn iter : table.getAllColumns()) {
			if (iter.isNullable()) {
				hasNullable = true;
			} else {
				hasNotNull = true;
			}
		}
		if (hasNullable) {
			klass.addImportedType(Nullable.class.getCanonicalName());
		}
		if (hasNotNull) {
			klass.addImportedType(NotNull.class.getCanonicalName());
		}
	}

	private void resolveEntityJavadoc(TopLevelClass klass, IntrospectedTable table) {
		String tableRemark = table.getRemarks() == null ? "" : table.getRemarks();
		klass.addJavaDocLine(" * @description " + tableRemark);
		klass.addJavaDocLine(" * @table " + table.getFullyQualifiedTable());
		klass.addJavaDocLine(" * @date " + LocalDate.now().toString());
		klass.addJavaDocLine(" */");
	}

	private void resolveEntityAnnotation(TopLevelClass klass, IntrospectedTable table) {
		String tableRemark = table.getRemarks() == null ? "" : table.getRemarks();
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
		resolvePrimaryKey(field, table, column);
		resolveFieldJavadoc(field, table, column);
		resolveDefaultValue(field, table, column);
		resolveNullable(field, table, column);
		resolveColumn(field, table, column);
		resolveApiModelProperty(field, table, column);
	}

	private void resolvePrimaryKey(Field field, IntrospectedTable table, IntrospectedColumn column) {
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
	}

	private void resolveFieldJavadoc(Field field, IntrospectedTable table, IntrospectedColumn column) {
		String defaultValue = column.getDefaultValue();
		String remark = column.getRemarks();
		if (column.isNullable()) {
			if (EmptyUtil.no(defaultValue)) {
				remark = remark + "(默认值:" + defaultValue + ")";
			}
		} else {
			if (EmptyUtil.no(defaultValue)) {
				remark = remark + "(不能为空,默认值:" + defaultValue + ")";
			} else {
				remark = remark + "(不能为空)";
			}
		}
		field.addJavaDocLine("/** ");
		field.addJavaDocLine(" * " + remark);
		field.addJavaDocLine(" */");
	}

	private void resolveDefaultValue(Field field, IntrospectedTable table, IntrospectedColumn column) {
		String defaultValue = column.getDefaultValue();
		if (EmptyUtil.no(defaultValue)) {
			String typeName = field.getType().getShortName();
			if (typeName.equals("Boolean")) {
				field.setInitializationString(Boolean.valueOf(defaultValue).toString());
			} else if (typeName.equals("Byte")) {
				field.setInitializationString(defaultValue);
			} else if (typeName.equals("Short")) {
				field.setInitializationString(defaultValue);
			} else if (typeName.equals("Integer")) {
				field.setInitializationString(defaultValue);
			} else if (typeName.equals("Long")) {
				field.setInitializationString(defaultValue + "L");
			} else if (typeName.equals("Double")) {
				field.setInitializationString(defaultValue);
			} else if (typeName.equals("Float")) {
				field.setInitializationString(defaultValue + "F");
			} else if (typeName.equals("String")) {
				field.setInitializationString("\"" + defaultValue + "\"");
			} else if (typeName.equals("BigDecimal")) {
				BigDecimal bigDecimal = new BigDecimal(defaultValue);
				if (bigDecimal.equals(BigDecimal.ZERO)) {
					field.setInitializationString("BigDecimal.ZERO");
				} else if (bigDecimal.equals(BigDecimal.ONE)) {
					field.setInitializationString("BigDecimal.ONE");
				} else if (bigDecimal.equals(BigDecimal.TEN)) {
					field.setInitializationString("BigDecimal.TEN");
				} else {
					field.setInitializationString("new BigDecimal(" + defaultValue + ")");
				}
			}
		}
	}

	private void resolveNullable(Field field, IntrospectedTable table, IntrospectedColumn column) {
		if (column.isNullable()) {
			field.addAnnotation("@Nullable");
		} else {
			field.addAnnotation("@NotNull");
		}
	}

	private void resolveColumn(Field field, IntrospectedTable table, IntrospectedColumn column) {
		String columnAnnotation = "@Column(name = \"" + column.getActualColumnName() + "\"";
		String typeName = field.getType().getShortName();
		if (typeName.equals("String") || typeName.equals("BigDecimal")) {
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

	private void resolveApiModelProperty(Field field, IntrospectedTable table, IntrospectedColumn column) {
		String defaultValue = column.getDefaultValue();
		String remark = column.getRemarks();
		String apiModelProperty = "@ApiModelProperty(value = \"" + remark + "\"";
		if (EmptyUtil.no(defaultValue)) {
			apiModelProperty = apiModelProperty + ", example = \"" + defaultValue + "\"";
		}
		apiModelProperty = apiModelProperty + ")";
		field.addAnnotation(apiModelProperty);
	}

}
