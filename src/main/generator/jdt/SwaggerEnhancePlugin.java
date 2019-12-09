package jdt;

import frodez.config.aop.validation.annotation.ValidateBean;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.constant.settings.DefDesc;
import frodez.util.beans.param.QueryPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

public class SwaggerEnhancePlugin extends EnhancePlugin {

	@Override
	public void init(String path) throws IOException, URISyntaxException {
		super.init(path);
	}

	@Override
	public void run() {
		addValidateBean(super.unit);
		addApiModel(super.unit);
		addApiModelProperty(super.unit);
	}

	@Override
	public void close() throws MalformedTreeException, BadLocationException, IOException, URISyntaxException {
		super.close();
	}

	private void addValidateBean(CompilationUnit unit) {
		JDTUtil.addTypeAnnotation(unit, ValidateBean.class);
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
				boolean isPageQuery = JDTUtil.belongTo(field.getType(), QueryPage.class);
				Map<String, Object> properties = new HashMap<>();
				if (isPageQuery) {
					properties.put("value", "DefDesc.Message.PAGE_QUERY");
					JDTUtil.addImport(unit, DefDesc.class);
				} else {
					properties.put("value", fieldJavaDocs.get(0));
				}
				//				if (isNotNull(field)) {
				//					properties.put("required", true);
				//				}
				if (JDTUtil.hasAnnotation(field, MapEnum.class)) {
					continue;
				}
				JDTUtil.addFieldAnnotation(unit, field, ApiModelProperty.class, properties);
			}
		}
	}

}
