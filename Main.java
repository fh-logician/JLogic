import logic.LogicTree;

public class Main {
    public static void main(String[] args) {
    
        String[] expressions = new String[] {
			
			// Pseudo Examples
            "a or (a and b)",
			"(not a and not b) or (not c or not b)",
			
			// Logic Examples
            "~(a v b)",
            "(a ^ b) ^ c",
			
			// Code Examples
			"(a || b) && (!d || c)",
			"(p && q && r) || (p && q && !r) || (p && !q && !r)",
			
			// Boolean Algebra Examples
			"(a*b*c) + (a*-b*-d) + (a*b*-c) + (a*b*d)",
			"(a+b+c)*(a+-b+-d)*(a+b+-c)*(a+b+d)"
        };

        for (String expression: expressions) {
            LogicTree tree = new LogicTree(expression);
            System.out.println(expression);
            System.out.println(tree.simplify());
            tree.printTable();
            System.out.println();
        }
    }
}
