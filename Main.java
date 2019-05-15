import logic.LogicTree;

public class Main {
    public static void main(String[] args) {
    
        String[] expressions = new String[] {
            "a or (a and b)",
			"(not a and not b) or (not c or not b)",
            "~(a v b)",
            "(a ^ b) ^ c",
			"(a || b) && (!d || c)",
			"(p && q && r) || (p && q && !r) || (p && !q && !r)",
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