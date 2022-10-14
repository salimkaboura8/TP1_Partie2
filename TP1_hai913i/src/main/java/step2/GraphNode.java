package step2;

import java.util.ArrayList;

public class GraphNode {
	
	private String className;
	private String methodName;
	
	ArrayList<GraphNode> children = new ArrayList<GraphNode>();

	static ArrayList<GraphNode> tree = new ArrayList<GraphNode>();
	static ArrayList<GraphNode> myMethods = new ArrayList<GraphNode>();
	static int indent=0;

	
	public GraphNode(String className, String methodName) {

		this.className = className;
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public static void showTree() {

		System.out.println("\n****CALL GRAPH*****");
		for(GraphNode n : myMethods) {
			{
			System.out.print("\nMethod : "+n.className+"."+n.methodName+"-->  ");
			if(n.children.size()>0)
				showChildren(n);
			}
		}	
	}
	private static void showChildren(GraphNode mynode) {
		indent++;

		if(mynode.children.size()>0) {
			System.out.print("\n");

			for(int i=0; i<indent ; i++ ) {
				System.out.print("\t");
			}
			System.out.print("\t -->");
			for(GraphNode child : mynode.children) {
				
				System.out.print(child.className+"."+child.methodName+"\t");
				showChildren(child);
			}
			System.out.print("\n\t");
		}
		else {
			for(GraphNode n : myMethods) {	
				
				if(n.className.equals(mynode.className) && n.methodName.equals(mynode.methodName) && n.children.size()>0) {
					showChildren(n);
				}
			}
		}
		indent--;

	}

	public static boolean doesTreeHave(String cname, String mname) {
		for(GraphNode n : tree) {
			if(n.className.equals(cname) && n.methodName.equals(mname)) {
				return true;
			}
		}
		return false;
	}

}
