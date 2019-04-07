import logic.LogicTree;

public class Main {
    public static void main(String[] args) {
    
        String[] expressions = new String[] {
            "a ^ b",
            "~(a v b)",
            "(a ^ b) ^ c",
            "a ^ b ^ c",
            "a ^ (b ^ c)",
            "~(a v b) ^ c",
            "~(a v b) ^ ~(a v c)",
            "~(~(a v b) ^ ~(a v c))",
            "a v b v c v d"
        };

        for (String expression: expressions) {
            LogicTree tree = new LogicTree(expression);
            System.out.println(expression);
            tree.printTable();
            System.out.println();
        }
    }
}
