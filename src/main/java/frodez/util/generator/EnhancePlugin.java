package frodez.util.generator;

import frodez.util.io.FileUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

public abstract class EnhancePlugin {

	protected String path;

	protected CompilationUnit unit;

	protected Document document;

	void init(String path) throws IOException, URISyntaxException {
		ASTParser parser = JDTUtil.defaultParser();
		String source = FileUtil.fileToString(path);
		parser.setSource(source.toCharArray());
		this.path = path;
		unit = (CompilationUnit) parser.createAST(null);
		document = new Document(source);
		unit.recordModifications();
	}

	abstract void run();

	void close() throws MalformedTreeException, BadLocationException, IOException, URISyntaxException {
		JDTUtil.commit(path, document, unit);
	}

}
