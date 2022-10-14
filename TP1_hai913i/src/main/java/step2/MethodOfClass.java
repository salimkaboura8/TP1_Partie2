package step2;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodOfClass {

	TypeDeclaration myclass;
	MethodDeclaration method;
	
	
	public MethodOfClass(TypeDeclaration myclass, MethodDeclaration method) {
		this.myclass = myclass;
		this.method = method;
	}
	
	
}
