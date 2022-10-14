package step2;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationNumberOfLineComparator implements Comparator<MethodDeclaration>{

	@Override
	public int compare(MethodDeclaration o1, MethodDeclaration o2) {
		return Integer.compare(getNumberOfLinesPerMethod(o1), getNumberOfLinesPerMethod(o2));
	}
	private static int getNumberOfLinesPerMethod(MethodDeclaration method)
	{	
		String[] lines = method.toString().split("\r\n|\r|\n"); 
		return lines.length;
	}

}
