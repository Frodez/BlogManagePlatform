package frodez.util.generator;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

public interface EnhancePlugin {

	void init(String path);

	void run(Document document, CompilationUnit unit);

	void save(String path, Document document, CompilationUnit unit);

}
