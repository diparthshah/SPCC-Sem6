import java.util.Scanner;

public class RecursionRemover {

    public static String[] productionInput = new String[100];
    public static Integer numOfSym = 0;

    public static void acceptInput() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter number of symbols : ");
        numOfSym = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\nEnter productions : ");
        for (Integer i = 0; i < numOfSym; i++) {
            productionInput[i] = scanner.nextLine();
        }
        scanner.close();

    }

    public static void identifyAlphaAndBeta(String symbol, String input) {

        String tokens[] = input.split("[{" + symbol + "}\\|]");
        String alpha = tokens[2];
        String beta = tokens[3];
        produceNewRules(symbol, alpha, beta);

    }

    public static void produceNewRules(String symbolStr, String alphaStr, String betaStr) {

        String symbolBar = symbolStr + "`";
        String rule = symbolStr + "->" + betaStr + symbolBar;
        String ruleBar = symbolBar + "->" + alphaStr + symbolBar + " | ep";
        System.out.println(rule);
        System.out.println(ruleBar);

    }

    public static void processRules() {

        System.out.println("\nNew Rules\n");

        for (Integer i = 0; i < numOfSym; i++) {

            String rawTokens[] = productionInput[i].split("\\s+");

            if (!rawTokens[0].equals(rawTokens[2])) {

                System.out.println(productionInput[i]);

            } else {

                identifyAlphaAndBeta(rawTokens[0], productionInput[i]);
            }
        }

    }

    public static void main(String[] args) {
        acceptInput();
        processRules();
    }
}

/*
	diparth@skillbox:~/misc$ java RecursionRemover

	Enter number of symbols : 
	3

	Enter productions : 
	E -> E + T | T
	T -> T * F | F
	F -> ID

	New Rules

	E-> TE`
	E`-> + T E` | ep
	T-> FT`
	T`-> * F T` | ep
	F -> ID

*/
