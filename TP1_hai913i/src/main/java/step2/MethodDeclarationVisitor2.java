package step2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodDeclarationVisitor2 extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		ClassDeclarationVisitor.invocationTree.put(ClassDeclarationVisitor.currentMethod, new ArrayList<MethodInvocation>());
		
		return super.visit(node);
	}
	public boolean visit(MethodInvocation node) {
		
		ClassDeclarationVisitor.invocationTree.get(ClassDeclarationVisitor.currentMethod).add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
}

