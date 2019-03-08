package frodez.util.generator;

import frodez.util.io.FileUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

public class EnhanceSourceCodeGenerator {

	private static final String PATH =
		"file:/D:/Code/Eclipse/workspace/BlogManagePlatform/src/main/java/frodez/dao/param/user/LoginParam.java";

	private static CompilationUnit unit;

	private static Document document;

	public static void main(String[] args) throws URISyntaxException, IOException, MalformedTreeException,
		BadLocationException {
		init();
		//		TypeDeclaration typeDeclaration = (TypeDeclaration) unit.types().get(0);
		//		NormalAnnotation annotation = (NormalAnnotation) typeDeclaration.modifiers().get(2);
		//		MemberValuePair pair = (MemberValuePair) annotation.values().get(1);
		//		ArrayInitializer array = (ArrayInitializer) pair.getValue();
		//		System.out.println(array.expressions().get(0).getClass());
		new SwaggerEnhancePlugin().run(unit);
		close();
	}

	private static void init() throws IOException, URISyntaxException {
		ASTParser parser = JDTUtil.defaultParser();
		String source = FileUtil.fileToString(PATH);
		parser.setSource(source.toCharArray());
		unit = (CompilationUnit) parser.createAST(null);
		document = new Document(source);
		unit.recordModifications();
	}

	private static void close() throws MalformedTreeException, BadLocationException, IOException, URISyntaxException {
		JDTUtil.commit(PATH, document, unit);
	}

	/**
	 * ASTParser parser = JDTUtil.defaultParser(); String source = FileUtil.fileToString(PATH);
	 * parser.setSource(source.toCharArray()); CompilationUnit unit = (CompilationUnit) parser.createAST(null);
	 * unit.recordModifications(); AST ast = unit.getAST(); TypeDeclaration typeDeclaration = (TypeDeclaration)
	 * unit.types().get(0); System.out.println(typeDeclaration.getJavadoc());
	 * System.out.println("-------------------------------"); System.out.println(typeDeclaration.modifiers());
	 * System.out.println("-------------------------------"); System.out.println(typeDeclaration.superInterfaceTypes());
	 * System.out.println("-------------------------------"); System.out.println(typeDeclaration.getName());
	 * System.out.println("-------------------------------"); System.out.println(typeDeclaration.typeParameters());
	 * System.out.println("-------------------------------"); System.out.println(typeDeclaration.getSuperclassType());
	 * System.out.println("-------------------------------"); System.out.println(typeDeclaration.bodyDeclarations());
	 * VariableDeclarationFragment variable = ast.newVariableDeclarationFragment();
	 * variable.setName(ast.newSimpleName("test")); FieldDeclaration field = ast.newFieldDeclaration(variable);
	 * field.setType(ast.newPrimitiveType(PrimitiveType.INT));
	 * field.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
	 * typeDeclaration.bodyDeclarations().add(field); JDTUtil.commit(PATH, new Document(source), unit);
	 */

}
