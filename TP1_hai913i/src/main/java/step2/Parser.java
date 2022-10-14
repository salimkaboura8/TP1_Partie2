package step2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Parser {
	
	public static final String projectPath = "C:\\Users\\jaste\\OneDrive\\Bureau\\Etudes\\M2\\mini_project_in_project\\project";
	public static final String projectSourcePath = projectPath + "\\src";
	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_51\\lib\\rt.jar";
	private static final ClassDeclarationVisitor classDeclaration = new ClassDeclarationVisitor();
	private static final MethodDeclarationVisitor methodDeclaration = new MethodDeclarationVisitor();
	private static final VariableDeclarationFragmentVisitor variableDeclaration = new VariableDeclarationFragmentVisitor();
	private static final ArrayList<CompilationUnit> compilationUnits = new ArrayList<>();
	static ASTParser parser;
 	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectPath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		//
		int i = 0;
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);

			CompilationUnit parse = parse(content.toCharArray());
			compilationUnits.add(parse);
			parse.accept(classDeclaration);
			parse.accept(methodDeclaration);
			parse.accept(variableDeclaration);

		}
		/*
		for(MethodDeclaration m : methodDeclaration.getMethods()) {
			m.getBody().accept(methodDeclaration);
		}
		System.out.println(methodDeclaration.getInvocations(methodDeclaration.getMethods().get(1)).get(0).getName());
		*/
		statistics();
		printInvocationTree();
		//classDeclaration.printVariables();
		
	}
	private static void statistics()
	{	
		System.out.println("\n Q1. Number of classes : " + classDeclaration.getClasses().size());
		System.out.println("\n Q2. Number of lines : " + getNumberOfLines());
		System.out.println("\n Q3. Number of methods : " + methodDeclaration.getMethods().size());
		System.out.println("\n Q4. Number of packages : " + getNumberOfPackages());
		System.out.println("\n Q5. Average number of methods per class : " + averageMethodPerClass());
		System.out.println("\n Q6. Average number of lines per method : " + getAverageNumberOfLinesPerMethods());
		System.out.println("\n Q7. Average number of attributes per class : " + averageAttributesPerClass());
		int n = 2;
		System.out.println("\n Q8. 10% classes with most methods :");

		for(TypeDeclaration t :topTenClassesWithMost(new TypeDeclarationMethodsComparator()))
		{
			System.out.println(t.getName());
		}
		System.out.println("\n Q9. 10% classes with most attributes :");
		for(TypeDeclaration t :topTenClassesWithMost(new TypeDeclarationAttributesComparator()))
		{
			System.out.println(t.getName());
		}
		System.out.println("\n Q10. Common between Q9 and Q10  :");
		for(TypeDeclaration t :commonMostMethodsAttributes())
		{
			System.out.println(t.getName());
		}
		System.out.println("\n Q11. Classes with more than " + n + " methods :");
		for(TypeDeclaration t : getClassWithMoreThanNMethods(n))
		{
			System.out.println(t.getName());
		}
		getMethodsWithMostLinesForAllClasses();
		System.out.print("\n Q13. Max number of parameters : ");
		System.out.println(getMethodWithMaxParameters());
	
	}
	
	private static int getNbLines()
	{
		int i = 0;
		for(MethodDeclaration classes : methodDeclaration.getMethods())
		{
			System.out.println(classes.getBody().toString());
		}
		return i;
	}
	private static int getMethodWithMaxParameters()
	{
		int i = 0;
		String methodName = "";
		for(MethodDeclaration methodDeclaration : methodDeclaration.getMethods())
		{
			int j = methodDeclaration.parameters().size();
			if(i < j)
			{
				methodName = methodDeclaration.getName().toString();
				i = j;
			}
		}
		return i;
	}
	private static float averageAttributesPerClass()
	{
		int i = 0;
		for(TypeDeclaration c : classDeclaration.getClasses())
		{
			i+= c.getFields().length;
		}
		return (float)i/(float)classDeclaration.getClasses().size();
	}
	private static float averageMethodPerClass()
	{
		return (float)methodDeclaration.getMethods().size()/(float)classDeclaration.getClasses().size();
	}
	private static void printClassesInfo() {
		for (TypeDeclaration classes : classDeclaration.getClasses()) {
			System.out.println("Class " + classes.getName());
			System.out.println("Methods:");
			for(MethodDeclaration methodDeclaration : classes.getMethods())
			{
				System.out.println("   -" + methodDeclaration.getName());
			}
		}
	}
	private static ArrayList<TypeDeclaration> getClassWithMoreThanNMethods(int n)
	{
		ArrayList<TypeDeclaration> res = new ArrayList<TypeDeclaration>();
		for (TypeDeclaration classes : classDeclaration.getClasses()) {
			if(classes.getMethods().length > n)
			{
				res.add(classes);
			}
		}
		return res;
	}
	
	
	//La méthode prend en paramètre un comparateur (Soit comparateur d'attributs soit de méthodes)
	//Elle retourne les 10% des classes qui possèdent le plus grand nombre d'attributs ou de méthodes
	private static ArrayList<TypeDeclaration> topTenClassesWithMost(Comparator<TypeDeclaration> comparator)
	{
		ArrayList<TypeDeclaration> res = new ArrayList<TypeDeclaration>();
		
		int n = (int) Math.ceil(0.1 * classDeclaration.getClasses().size());
		
		ArrayList<TypeDeclaration> sorted = new ArrayList<TypeDeclaration>();
		
		sorted = (ArrayList<TypeDeclaration>) classDeclaration.getClasses();
		
		sorted.sort(comparator);
		
		int length = sorted.size();
		for(int i = length - n; i < length; i++)
		{
			res.add(sorted.get(i));
		}
		return res;
	}
	
	private static ArrayList<TypeDeclaration> commonMostMethodsAttributes()
	{
		ArrayList<TypeDeclaration> res = new ArrayList<TypeDeclaration>();
		ArrayList<TypeDeclaration> classesMostMethods = new ArrayList<TypeDeclaration>();
		ArrayList<TypeDeclaration> classesMostAttributes = new ArrayList<TypeDeclaration>();
		classesMostMethods = topTenClassesWithMost(new TypeDeclarationMethodsComparator());
		classesMostAttributes = topTenClassesWithMost(new TypeDeclarationAttributesComparator());
		for(TypeDeclaration t : classesMostMethods)
		{
			if(classesMostAttributes.contains(t))
			{
				res.add(t);
			}
		}
		return res;
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		
		parse.accept(methodDeclaration);

		for (MethodDeclaration method : methodDeclaration.getMethods()) {
			System.out.println("Method name: " + method.getName()
					+ " Return type: " + method.getReturnType2());
		}
	}


	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2
					.getVariables()) {
				System.out.println("variable name: "
						+ variableDeclarationFragment.getName()
						+ " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}

		}
	}
	
	// navigate method invocations inside method
		public static void printMethodInvocationInfo(CompilationUnit parse) {

			MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
			parse.accept(visitor1);
			for (MethodDeclaration method : visitor1.getMethods()) {

				MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
				method.accept(visitor2);

				for (MethodInvocation methodInvocation : visitor2.getMethods()) {
					System.out.println("method " + method.getName() + " invoc method "
							+ methodInvocation.getName());
				}

			}
		}
	private static int getNumberOfPackages()
	{
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		for(CompilationUnit c : compilationUnits) {
			map.putIfAbsent(c.getPackage().toString(), true);
		}
		return map.size();
	}
	private static int getNumberOfLines()
	{
		int i = 0;
		for(CompilationUnit c : compilationUnits)
		{
			String[] lines = c.toString().split("\r\n|\r|\n"); 
			i+= lines.length;
		}
		return i;
	}
	private static void printInvocationTree()
	{
		System.out.println("Invocation Tree:");
		for(MethodOfClass mc: ClassDeclarationVisitor.methodsOfClasses)
		{
			
			GraphNode mainNode = new GraphNode(mc.myclass.getName().toString(),mc.method.getName().toString());
			//System.out.println("Method : "+mc.myclass.getName()+"."+mc.method.getName()+" --> ");
			ArrayList<MethodInvocation> methodsCalled  = ClassDeclarationVisitor.invocationTree.get(mc);
			
			if(!methodsCalled.isEmpty()) {
				for(MethodInvocation mCalled : methodsCalled) {
					
					GraphNode childNode = new GraphNode(getObjectType(mCalled),mCalled.getName().toString());
					
					mainNode.children.add(childNode);
					//System.out.print("\t"+getObjectType(mCalled));
					//System.out.println("."+mCalled.getName());
				}
			}
			GraphNode.myMethods.add(mainNode);
		}
		GraphNode.showTree();
	}
	private static String getObjectType(MethodInvocation mCalled) {
		
		//Vérifier si il y'a un objet dans l'appel			
		if(mCalled.getExpression()!=null) {
				if(mCalled.getExpression().resolveTypeBinding()!=null) {
					return mCalled.getExpression().resolveTypeBinding().getName();	
				}
			}
		else {
			if(mCalled.resolveMethodBinding()!=null)
			return mCalled.resolveMethodBinding().getDeclaringClass().getName();	
		}
		return "";
	}
	private static float getAverageNumberOfLinesPerMethods()
	{
		int i = 0;
		for(MethodDeclaration c : methodDeclaration.getMethods())
		{
			String[] lines = c.toString().split("\r\n|\r|\n"); 
			i+= lines.length;
		}
		return i/methodDeclaration.getMethods().size();
	}

	private static ArrayList<String> getMethodWithMostLinesPerClass(TypeDeclaration c)
	{
		ArrayList<String> res = new ArrayList<>();
		ArrayList<MethodDeclaration> sorted = new ArrayList<>();
		
		MethodDeclaration[] methods = c.getMethods();
		for (int i=0 ; i< methods.length ; i++) {
			sorted.add(methods[i]);
		}
		sorted.sort(new MethodDeclarationNumberOfLineComparator());
		
		int n = (int) Math.ceil(0.1 * sorted.size());
		
		for(int i= sorted.size() - n ; i<sorted.size();i++) {
			res.add(sorted.get(i).getName().toString());
		}
		return res;
	}
	private static void getMethodsWithMostLinesForAllClasses() {
		System.out.println("\n Q12. 10% of methods with most lines for: ");

		for (TypeDeclaration c : classDeclaration.getClasses()) {
			System.out.println("\n   Class : "+c.getName());
			
			if(getMethodWithMostLinesPerClass(c).isEmpty())
				System.out.println("This class has no methods. x(");
			for(String s : getMethodWithMostLinesPerClass(c)) {
				System.out.println(s);
			}
		}
	}
}
