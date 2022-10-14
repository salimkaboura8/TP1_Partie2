package step2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ClassDeclarationVisitor extends ASTVisitor{

	MethodDeclarationVisitor2 methodDeclarationVisitor = new MethodDeclarationVisitor2();
	static TypeDeclaration currentClass;
	static MethodOfClass currentMethod;
	static HashMap<MethodOfClass, ArrayList<MethodInvocation>> invocationTree = new HashMap<MethodOfClass, ArrayList<MethodInvocation>>();
	List<TypeDeclaration> classes = new ArrayList<TypeDeclaration>();
	static List<MethodOfClass> methodsOfClasses = new ArrayList<MethodOfClass>();
	
	
	
	public boolean visit(TypeDeclaration node) {
		classes.add(node);
		if(!node.isInterface()) {
			for(MethodDeclaration m : node.getMethods())
			{
				currentMethod = new MethodOfClass(node,m);
				invocationTree.put(currentMethod, new ArrayList<MethodInvocation>());
				methodsOfClasses.add(currentMethod);
				m.accept(methodDeclarationVisitor);
			}
		}
		
		return super.visit(node);
	}

	
	public List<TypeDeclaration> getTypes() {
		return classes;
	}
	public List<TypeDeclaration> getClasses()
	{
		ArrayList <TypeDeclaration> res = new ArrayList<>();
		for (TypeDeclaration t : classes) {
			if (!t.isInterface()) {
				res.add(t);
			}
				
		}
		return res;
	}
	
	public List<MethodOfClass> getMethodsOfClasses()
	{	
		return methodsOfClasses;
	}
	HashMap<MethodOfClass, ArrayList<MethodInvocation>> getInvocationTree() {
		return invocationTree;
	}
}
