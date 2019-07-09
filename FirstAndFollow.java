import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FirstAndFollow {

    public static String[] productions = new String[100];
    public static String[] inputSymbols = new String[100];
    public static Integer n = 4;
    public static HashMap<String, String> firstSet = new HashMap<String, String>();
    public static HashMap<String, String> followSet = new HashMap<String, String>();

    public static void inputAndInitialize() {

        inputSymbols[0] = "E";
        inputSymbols[1] = "A";
        inputSymbols[2] = "T";
        inputSymbols[3] = "B";
        inputSymbols[4] = "F";

        productions[0] = "TA";
        productions[1] = "+TA|%";
        productions[2] = "FB";
        productions[3] = "*FB|%";
        productions[4] = "(E)|id";

        /*
            E` has been renamed as A (E` -> A)
            T` has been renamed as A (T` -> B)
            This has been done so to reduce string processing time, reduce test cases and improve overall optimization.
        */

        firstSet.put("E", "");
        firstSet.put("A", "");
        firstSet.put("T", "");
        firstSet.put("B", "");
        firstSet.put("F", "");

        followSet.put("E", "$");
        followSet.put("A", "");
        followSet.put("T", "");
        followSet.put("B", "");
        followSet.put("F", "");

    }

    public static void findFirst() {

        for (Integer i = n; i >= 0; i = i - 1) {

            String currSymbol = inputSymbols[i];
            String currProduction = productions[i];
            String productionTokens[] = currProduction.split("\\|");
            Integer numOfProd = productionTokens.length;
            String existingFirst = getFirst(currSymbol);

            for (Integer j = 0; j < numOfProd; j++) {

                if (productionTokens[j].matches("^(\\+|\\*|\\(|\\%).*$")) {

                    existingFirst = existingFirst + productionTokens[j].charAt(0);
                    setFirst(currSymbol, existingFirst);
                }

                else if (productionTokens[j].matches("^[A-Z].*$")) {

                    String newFirst = getFirst(Character.toString(productionTokens[j].charAt(0)));
                    existingFirst = existingFirst + newFirst;
                    setFirst(currSymbol, existingFirst);
                }

                else if (productionTokens[j].matches("^[a-z]+.*$")) {

                    String terminal = productionTokens[j].replaceAll("[A-Z]", "");
                    existingFirst = existingFirst + terminal;
                    setFirst(currSymbol, existingFirst);
                }
            }
        }
    }

    public static void setFirst(String Symbol, String first) {
        firstSet.put(Symbol, first);
    }

    public static String getFirst(String symbol) {
        return (firstSet.get(symbol));
    }

    public static void printFirstSet() {
        System.out.println("Non-Terminal\tFirstSet");
        for (Map.Entry<String, String> entry : firstSet.entrySet()) {
            System.out.println("\t" + entry.getKey() + "\t{ " + entry.getValue() + " }");
        }
    }

    public static void findFollow() {

        for (Integer i = 0; i <= n; i++) {

            String symbolForFollow = inputSymbols[i];

            for (Integer k = 0; k <= n; k++) {

                String currSymbol = inputSymbols[k];
                String productionTokens[] = productions[k].split("\\|");
                Integer numOfProd = productionTokens.length;

                for (Integer j = 0; j < numOfProd; j++) {

                    Integer indexForFollowSymbol = productionTokens[j].indexOf(symbolForFollow);

                    if (indexForFollowSymbol >= 0) {

                        indexForFollowSymbol = indexForFollowSymbol + 1;
                        followHelper(symbolForFollow, currSymbol, productionTokens[j], indexForFollowSymbol);
                    } 
                }
            }
        }
    }

    public static void followHelper(String symbolForWhichFollowHasToBeFound, String currentSymbolInProcess, String currentProduction, Integer index) {

        String existingFollow = getFollow(symbolForWhichFollowHasToBeFound);

        if (index + 1 > currentProduction.length()) {

            existingFollow = existingFollow + getFollow(currentSymbolInProcess);
            setFollow(symbolForWhichFollowHasToBeFound, existingFollow);
        }

        else  {

            String followedBySymbol = Character.toString(currentProduction.charAt(index));

            if (followedBySymbol.equals(")")) {

                existingFollow = existingFollow + followedBySymbol;
                setFollow(symbolForWhichFollowHasToBeFound, existingFollow);
            }

            else if (followedBySymbol.matches("[A-Z]")) {

                String firstOfFollowedSymbol = getFirst(followedBySymbol).replace("%", "");
                existingFollow = existingFollow + firstOfFollowedSymbol + getFollow(currentSymbolInProcess);
                setFollow(symbolForWhichFollowHasToBeFound, existingFollow);
            }
        }
    }

    public static void setFollow(String Symbol, String follow) {

        StringBuilder sb = new StringBuilder();
        follow.chars().distinct().forEach(c -> sb.append((char) c));
        followSet.put(Symbol, sb.toString());
    }

    public static String getFollow(String symbol) {

        return (followSet.get(symbol));
    }

    public static void printFollowSet() {

        System.out.println("Non-Terminal\tFollowSet");
        for (Map.Entry<String, String> entry : followSet.entrySet()) {
            System.out.println("\t" + entry.getKey() + "\t{ " + entry.getValue() + " }");
        }
    }

    public static void main(String[] args) {

        inputAndInitialize();
        findFirst();
        printFirstSet();
        findFollow();
        printFollowSet();
    }
} 

/*
diparth@skillbox:~/pgm$ javac FirstAndFollow.java
diparth@skillbox:~/pgm$ java FirstAndFollow
Non-Terminal    FirstSet
        A       { +% }
        B       { *% }
        T       { (id }
        E       { (id }
        F       { (id }
Non-Terminal    FollowSet
        A       { $) }
        B       { +$) }
        T       { +$) }
        E       { $) }
        F       { *+$) }
NOTE: % stands for epsilon.
*/
