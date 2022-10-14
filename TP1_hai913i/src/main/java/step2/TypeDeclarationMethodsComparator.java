package step2;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationMethodsComparator implements Comparator<TypeDeclaration>{

	@Override
	public int compare(TypeDeclaration o1, TypeDeclaration o2) {
		return Integer.compare(o1.getMethods().length, o2.getMethods().length);
	}

}
