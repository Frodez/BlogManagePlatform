package mybatis;

import java.util.Collections;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

/**
 * mybatis-generatorXML文件格式化配置
 * @author Frodez
 * @date 2018-11-21
 */
public class CustomXmlFormatter implements XmlFormatter {

	protected Context context;

	@Override
	public String getFormattedContent(Document document) {
		return CustomXmlGenerator.generaterContent(document);
	}

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	private static class CustomXmlGenerator {

		/** The Constant lineSeparator. */
		private static final String lineSeparator;

		static {
			String ls = System.getProperty("line.separator");
			if (ls == null) {
				ls = "\n";
			}
			lineSeparator = ls;
		}

		private static String generaterContent(Document document) {
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".concat(lineSeparator).concat(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">")
				.concat(lineSeparator).concat(getFormattedContent(document.getRootElement(), 0));
		}

		private static String getFormattedContent(XmlElement element, int indentLevel) {
			StringBuilder sb = new StringBuilder();
			xmlIndent(sb, indentLevel);
			sb.append('<');
			sb.append(element.getName());
			Collections.sort(element.getAttributes());
			for (Attribute att : element.getAttributes()) {
				sb.append(' ');
				sb.append(att.getFormattedContent());
			}
			if (element.getElements().size() > 0) {
				sb.append(">");
				sb.append(lineSeparator);
				xmlIndent(sb, indentLevel + 1);
				sb.append(lineSeparator);
				sb.append("</");
				sb.append(element.getName());
				sb.append('>');
			} else {
				sb.append(" />");
			}
			return sb.toString();
		}

		public static void xmlIndent(StringBuilder sb, int indentLevel) {
			for (int i = 0; i < indentLevel; i++) {
				sb.append("    ");
			}
		}

	}

}
