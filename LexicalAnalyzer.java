import java.util.*;

public class LexicalAnalyzer {

    public static  String[] tokens;
    public static HashMap<String, String> nounMap = new HashMap<String, String>();

    public static void inputAndTokenize() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Input String : ");
        String input = scanner.nextLine();

        scanner.close();
        tokens = input.split("[\\s]+");
    }

    public static void identifyTokens() {

        Integer n = tokens.length;
        Integer noun = 0;

        System.out.println("\n\tLexeme\tType");
        System.out.println("\t------------");

        for(Integer i = 0; i < n; i++) {

            switch(tokens[i]) {
                case "If" :
                case "then" :
                    System.out.println("\t" + tokens[i] + "\tKeyword ");
                    break;

                case "hate" :
                case "like" :
                    System.out.println("\t" + tokens[i] + "\tVerb ");
                    break;
                case "they":
                    System.out.println("\t" + tokens[i] + "\tAction ");
                    break;
                case "." :
                    System.out.println("\t" + tokens[i] + "\tOperator");
                    break;
                case "$":
                    System.out.println("\t" + tokens[i] + "\t<eof>");
                    break;
                default :
                    if(tokens[i].matches("[a-z]+")) {
                        String check = nounMap.get(tokens[i]);
                        if(check==null) {
                            noun = noun + 1;
                            String nounStr = tokens[i] + " Noun " + noun.toString();
                            nounMap.put(tokens[i], nounStr);
                            System.out.println(nounStr);
                        }
                        else {
                            String nounStr = nounMap.get(tokens[i]);
                            System.out.println(nounStr);
                        }


                        //System.out.println("\t" + tokens[i] + "\tNoun " + noun);
                    }
                    else {
                        System.out.println("\t" + tokens[i] + "\tUnidentified ");
                    }
                    break;
            }
        }
        System.out.println("\nNumber Of Tokens :  " + n);
        System.out.println("\n");
    }

    public static void main(String[] args) {
        inputAndTokenize();
        identifyTokens();
    }
}

/*
Enter Input String :
If dogs hate cats they chase . If cats like milk they drink . $

   Lexeme Type
   ------------
   If Keyword
    dogs Noun 1
   hate   Verb
    cats Noun 2
   they   Action
    chase Noun 3
   .  Operator
   If Keyword
    cats Noun 2
   like   Verb
    milk Noun 4
   they   Action
    drink Noun 5
   .  Operator
   $  <eof>

Number Of Tokens :  15
 */