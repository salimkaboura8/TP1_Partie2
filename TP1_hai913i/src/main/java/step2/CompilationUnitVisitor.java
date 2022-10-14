package step2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CompilationUnitVisitor extends ASTVisitor{
	private List<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
	
	public boolean visit(CompilationUnit node) {
		compilationUnits.add(node);
		return super.visit(node);
	}
	
	public List<CompilationUnit> getVariables() {
		return compilationUnits;
	}
}
