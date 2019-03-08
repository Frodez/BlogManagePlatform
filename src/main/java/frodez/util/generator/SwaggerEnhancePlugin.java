package frodez.util.generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SwaggerEnhancePlugin {

	public void run(CompilationUnit unit) {
		addApiModel(unit);
		addApiModelProperty(unit);
	}

	private void addApiModel(CompilationUnit unit) {
		TypeDeclaration typeDeclaration = (TypeDeclaration) unit.types().get(0);
		List<String> classJavaDocs = JDTUtil.pureJavaDoc(typeDeclaration.getJavadoc());
		if (!classJavaDocs.isEmpty()) {
			for (String doc : classJavaDocs) {
				if (doc.endsWith("参数")) {
					Map<String, Object> properties = new HashMap<>();
					properties.put("description", doc);
					JDTUtil.addTypeAnnotation(unit, ApiModel.class, properties);
				}
				if (doc.endsWith("信息")) {
					Map<String, Object> properties = new HashMap<>();
					properties.put("description", doc.substring(0, doc.length() - "信息".length()));
					JDTUtil.addTypeAnnotation(unit, ApiModel.class, properties);
				}
			}
		}
	}

	private void addApiModelProperty(CompilationUnit unit) {
		TypeDeclaration typeDeclaration = (TypeDeclaration) unit.types().get(0);
		for (Object object : typeDeclaration.bodyDeclarations()) {
			FieldDeclaration field = (FieldDeclaration) object;
			List<String> fieldJavaDocs = JDTUtil.pureJavaDoc(field.getJavadoc());
			if (!fieldJavaDocs.isEmpty()) {
				Map<String, Object> properties = new HashMap<>();
				properties.put("value", fieldJavaDocs.get(0));
				if (isNotNull(field)) {
					properties.put("required", true);
				}
				JDTUtil.addFieldAnnotation(unit, field, ApiModelProperty.class, properties);
			}
		}
	}

	private boolean isNotNull(FieldDeclaration field) {
		for (Object item : field.modifiers()) {
			if (item instanceof NormalAnnotation) {
				NormalAnnotation annotation = (NormalAnnotation) item;
				if (annotation.getTypeName().getFullyQualifiedName().equals("NotNull")) {
					return true;
				}
				for (Object value : annotation.values()) {
					if (value instanceof MemberValuePair) {
						MemberValuePair valuePair = (MemberValuePair) value;
						if (valuePair.getName().getFullyQualifiedName().equals("nullable")
							&& ((BooleanLiteral) valuePair.getValue()).booleanValue()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
