import java.util.HashMap;

public class IRCode {

    public static String[] stmt = new String[100];
    public static String[] operator = new String[100];
    public static String[] arg1 = new String[100];
    public static String[] arg2 = new String[100];
    public static String[] result = new String[100];
    public static String[] locations = new String[100];
    public static HashMap<String, String> quadTable = new HashMap<String, String>();
    public static HashMap<String, String> tripleTable = new HashMap<String, String>();
    public static Integer n = 4;

    public static void initialize() {
        stmt[0] = "a=b";
        stmt[1] = "f=c+d";
        stmt[2] = "e=a-f";
        stmt[3] = "g=b*c";
    }

    public static void performCalculations() {

        Integer t = 0;
        Integer loc = 0;
        String locStr = "";

        for (Integer i = 0; i < n; i++) {

            String expression[] = stmt[i].split("\\=");

            if (expression[1].matches("[a-z](\\+|\\-|\\*)[a-z].*$")) {
                String subExp[] = expression[1].split("");
                arg1[i] = subExp[0];
                operator[i] = subExp[1];
                arg2[i] = subExp[2];
            }

            else if (expression[1].matches("[a-z]")) {
                arg1[i] = expression[1];
                operator[i] = ":=";
                arg2[i] = "";
            }

            t = i + 1;
            result[i] = "t" + t.toString();
            quadTable.put(expression[0], result[i]);
            locStr = "(" + loc.toString() + ")";
            locations[i] = locStr;
            tripleTable.put(expression[0], locations[i]);
            loc++;
        }
    }

    public static void print() {

        String expArg1 = "";
        String expArg2 = "";

        System.out.println("\nQuadruples :\n");

        for (Integer i = 0; i < n; i++) {

            expArg1 = arg1[i];
            expArg2 = arg2[i];

            if (quadTable.get(arg1[i]) != null) {
                expArg1 = quadTable.get(arg1[i]);
            }

            if (quadTable.get(arg2[i]) != null) {
                expArg2 = quadTable.get(arg2[i]);
            }

            System.out.println("Operator: " + operator[i] + "  Argument1: " + expArg1 + "  Argument2: " + expArg2
                    + "  Result: " + result[i]);
        }

        System.out.println("\nTriples :\n");

        for (Integer i = 0; i < n; i++) {

            expArg1 = arg1[i];
            expArg2 = arg2[i];

            if (tripleTable.get(arg1[i]) != null) {
                expArg1 = tripleTable.get(arg1[i]);
            }

            if (tripleTable.get(arg2[i]) != null) {
                expArg2 = tripleTable.get(arg2[i]);
            }

            System.out.println("Location : " + locations[i] + "  Operator: " + operator[i] + "  Argument1: " + expArg1
                    + "  Argument2: " + expArg2);
        }

        System.out.println("\nIndirect Triples:\n");

        for (Integer i = 0; i < n; i++) {

            System.out.println("Array Index Pointer : " + i.toString() + "  Triple Location : " + locations[i]);
        }
    }

    public static void main(String[] args) {
        initialize();
        performCalculations();
        print();
    }
}

/*

diparth@skillbox:~/pgm$ javac IRCode.java
diparth@skillbox:~/pgm$ java IRCode

Quadruples :

Operator: :=  Argument1: b  Argument2:   Result: t1
Operator: +  Argument1: c  Argument2: d  Result: t2
Operator: -  Argument1: t1  Argument2: t2  Result: t3
Operator: *  Argument1: b  Argument2: c  Result: t4

Triples :

Location : (0)  Operator: :=  Argument1: b  Argument2: 
Location : (1)  Operator: +  Argument1: c  Argument2: d
Location : (2)  Operator: -  Argument1: (0)  Argument2: (1)
Location : (3)  Operator: *  Argument1: b  Argument2: c

Indirect Triples:

Array Index Pointer : 0  Triple Location : (0)
Array Index Pointer : 1  Triple Location : (1)
Array Index Pointer : 2  Triple Location : (2)
Array Index Pointer : 3  Triple Location : (3)

*/

