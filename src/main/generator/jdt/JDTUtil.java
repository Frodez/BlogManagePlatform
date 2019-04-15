package jdt;

import frodez.util.io.FileUtil;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

public class JDTUtil {

	public static Hashtable<String, String> defaultOptions() {
		Hashtable<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_11);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_11);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
		return options;
	}

	public static ASTParser defaultParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS11);
		parser.setCompilerOptions(defaultOptions());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		return parser;
	}

	public static void commit(String path, Document document, CompilationUnit unit) throws MalformedTreeException,
		BadLocationException, IOException, URISyntaxException {
		unit.rewrite(document, defaultOptions()).apply(document);
		FileUtil.writeString(document.get(), path);
	}

	public static List<String> pureJavaDoc(Javadoc javadoc) {
		List<String> list = new ArrayList<>();
		if (javadoc != null && javadoc.tags() != null) {
			for (Object item : javadoc.tags()) {
				TagElement tag = (TagElement) item;
				String content = tag.toString();
				list.add(content.substring(4, content.length()).toString());
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static void addImport(CompilationUnit unit, Class<?> klass) {
		if (unit == null || klass == null) {
			throw new IllegalArgumentException();
		}
		for (Object object : unit.imports()) {
			ImportDeclaration importDeclaration = (ImportDeclaration) object;
			if (importDeclaration.getName().getFullyQualifiedName().equals(klass.getName())) {
				return;
			}
		}
		ImportDeclaration importDeclaration = unit.getAST().newImportDeclaration();
		importDeclaration.setName(unit.getAST().newName(klass.getName()));
		unit.imports().add(importDeclaration);
	}

	@SuppressWarnings("unchecked")
	public static void addTypeAnnotation(CompilationUnit unit, Class<? extends Annotation> annotationClass, Map<String,
		Object> properties) {
		if (unit == null || annotationClass == null) {
			throw new IllegalArgumentException();
		}
		TypeDeclaration typeDeclaration = (TypeDeclaration) unit.types().get(0);
		if (properties == null) {
			addMarkerAnnotation(unit, typeDeclaration.modifiers(), annotationClass.getSimpleName());
		} else {
			addNormalAnnotation(unit, typeDeclaration.modifiers(), annotationClass.getSimpleName(), properties);
		}
		addImport(unit, annotationClass);
	}

	@SuppressWarnings("unchecked")
	public static void addFieldAnnotation(CompilationUnit unit, FieldDeclaration field, Class<
		? extends Annotation> annotationClass, Map<String, Object> properties) {
		if (unit == null || annotationClass == null) {
			throw new IllegalArgumentException();
		}
		if (properties == null) {
			addMarkerAnnotation(unit, field.modifiers(), annotationClass.getSimpleName());
		} else {
			addNormalAnnotation(unit, field.modifiers(), annotationClass.getSimpleName(), properties);
		}
		addImport(unit, annotationClass);
	}

	private static void addMarkerAnnotation(CompilationUnit unit, List<Object> list, String simpleName) {
		AST ast = unit.getAST();
		MarkerAnnotation markerAnnotation = ast.newMarkerAnnotation();
		markerAnnotation.setTypeName(ast.newSimpleName(simpleName));
		attachAnnotation(list, markerAnnotation);
	}

	@SuppressWarnings("unchecked")
	private static void addNormalAnnotation(CompilationUnit unit, List<Object> list, String simpleName, Map<String,
		Object> properties) {
		AST ast = unit.getAST();
		NormalAnnotation normalAnnotation = ast.newNormalAnnotation();
		normalAnnotation.setTypeName(ast.newSimpleName(simpleName));
		for (Entry<String, Object> entry : properties.entrySet()) {
			MemberValuePair pair = ast.newMemberValuePair();
			pair.setName(ast.newSimpleName(entry.getKey()));
			pair.setValue(getExpression(ast, entry.getValue()));
			normalAnnotation.values().add(pair);
		}
		attachAnnotation(list, normalAnnotation);
	}

	@SuppressWarnings("unchecked")
	private static Expression getExpression(AST ast, Object value) {
		Expression expression = null;
		if (value == null) {
			expression = ast.newNullLiteral();
		}
		if (value.getClass().isArray()) {
			ArrayInitializer arrayInitializer = ast.newArrayInitializer();
			for (Object object : (Object[]) value) {
				arrayInitializer.expressions().add(getExpression(ast, object));
			}
			expression = arrayInitializer;
		}
		if (value instanceof Boolean) {
			expression = ast.newBooleanLiteral((Boolean) value);
		}
		if (value instanceof Number) {
			expression = ast.newNumberLiteral(value.toString());
		}
		if (value.getClass() == char.class) {
			CharacterLiteral characterLiteral = ast.newCharacterLiteral();
			characterLiteral.setCharValue((char) value);
			expression = characterLiteral;
		}
		if (value instanceof String) {
			StringLiteral stringLiteral = ast.newStringLiteral();
			stringLiteral.setLiteralValue((String) value);
			expression = stringLiteral;
		}
		if (value instanceof Class) {
			TypeLiteral typeLiteral = ast.newTypeLiteral();
			typeLiteral.setType(ast.newSimpleType(ast.newName(value.getClass().getName())));
			expression = typeLiteral;
		}
		return expression;
	}

	private static void attachAnnotation(List<Object> list, org.eclipse.jdt.core.dom.Annotation annotation) {
		Iterator<Object> iterator = list.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			if (iterator.next() instanceof Modifier) {
				break;
			}
			++index;
		}
		list.add(index, annotation);
	}

	public static boolean belongTo(Type type, Class<?> klass) {
		if (type == null || klass == null) {
			throw new IllegalArgumentException();
		}
		if (type.isArrayType()) {
			return belongTo(((ArrayType) type).getElementType(), klass);
		}
		if (type.isPrimitiveType()) {
			return ((PrimitiveType) type).getPrimitiveTypeCode().toString().equalsIgnoreCase(klass.getSimpleName());
		}
		if (type.isSimpleType()) {
			SimpleType simpleType = (SimpleType) type;
			if (simpleType.getName().isQualifiedName()) {
				return simpleType.getName().getFullyQualifiedName().equals(klass.getName());
			} else {
				return simpleType.getName().getFullyQualifiedName().equals(klass.getSimpleName());
			}
		}
		return false;
	}

}
