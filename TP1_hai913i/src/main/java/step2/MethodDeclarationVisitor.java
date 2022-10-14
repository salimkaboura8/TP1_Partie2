package step2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	MethodDeclaration currentMethod;
	HashMap<MethodDeclaration, ArrayList<MethodInvocation>> invocationTree = new HashMap<MethodDeclaration, ArrayList<MethodInvocation>>();
	
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		currentMethod = node;
		invocationTree.put(currentMethod, new ArrayList<MethodInvocation>());
		
		return super.visit(node);
	}
	public boolean visit(MethodInvocation node) {
		
		invocationTree.get(currentMethod).add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	public HashMap<MethodDeclaration, ArrayList<MethodInvocation>> getInvocationTree() {
		return invocationTree;
	}
	public ArrayList<MethodInvocation> getInvocations(MethodDeclaration m){
		return invocationTree.get(m);
	}
}

