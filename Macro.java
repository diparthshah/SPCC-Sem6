import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;

public class Macro {

    public static StringBuilder codeBuilder = new StringBuilder();
    public static String codeString = new String();
    public static String macroName = "";
    public static String macroArgs;
    public static String macroData = "";
    public static String macroCallArgs;
    public static HashMap<String, String> mdtTable = new HashMap<String, String>();
    public static HashMap<String, String> argListPassOne = new HashMap<String, String>();
    public static HashMap<String, String> argListPassTwo = new HashMap<String, String>();

    public static void readFile() throws Exception {

        String fileName = "macrocode.txt";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        while (line != null) {
            codeBuilder.append(line).append("\n");
            line = reader.readLine();
        }
        reader.close();
        codeString = codeBuilder.toString();
    }

    public static void identifySpecifics() {

        String macroAndArgs = "MACRO\\n*\\s*([A-Z]+)\\s*(.*)";
        String macroContent = "MACRO(?:.*\\n)*MEND";
        String macroCall = "MEND(?:\\n*.*)NONE\\s*(.*)";

        Pattern pattern = Pattern.compile(macroAndArgs, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(codeString);

        while (matcher.find()) {
            macroName = matcher.group(1).toString();
            macroArgs = matcher.group(2).toString();
        }

        pattern = Pattern.compile(macroContent, Pattern.MULTILINE);
        matcher = pattern.matcher(codeString);

        while (matcher.find()) {
            macroData = matcher.group(0).toString();
        }

        macroCall = macroCall.replace("NONE", macroName);
        pattern = Pattern.compile(macroCall, Pattern.MULTILINE);
        matcher = pattern.matcher(codeString);

        while (matcher.find()) {
            macroCallArgs = matcher.group(1).toString();
        }
    }

    public static void passOneRoller() {

        String macroArgsList[] = macroArgs.split("\\,");
        Integer index = 1;
        for (Integer i = 0; i < macroArgsList.length; i++) {
            argListPassOne.put(macroArgsList[i], index.toString());
            index++;
        }
        for (Integer i = 0; i < macroArgsList.length; i++) {
            macroData = macroData.replace(macroArgsList[i], "#" + argListPassOne.get(macroArgsList[i]).toString());
        }
    }

    public static void passTwoRoller() {

        String macroCallArgsList[] = macroCallArgs.split("\\,");
        Integer index = 1;
        for (Integer i = 0; i < macroCallArgsList.length; i++) {
            argListPassTwo.put(macroCallArgsList[i], "#" + index.toString());
            macroData = macroData.replace("#" + index.toString(), macroCallArgsList[i]);
            index++;
        }
    }

    public static void passOnePrinter() {

        System.out.println("\nMNT");
        System.out.println("Name : " + macroName + "  Index: 1");
        System.out.println("\nALA");
        System.out.println("Index\tArguments");
        for (String name : argListPassOne.keySet()) {
            String key = name.toString();
            String value = argListPassOne.get(name).toString();
            System.out.println(value + "\t" + key);
        }
        System.out.println("\nMDT");
        System.out.println("Index\tDefinitions");
        String macroDataArr[] = macroData.split("\n");
        for (Integer i = 0; i < macroDataArr.length; i++) {
            System.out.println(i.toString() + "\t" + macroDataArr[i]);
        }
    }

    public static void passTwoPrinter() {

        System.out.println("\nMNT");
        System.out.println("Name : " + macroName + "  Index: 1");
        System.out.println("\nALA");
        System.out.println("Index\tArguments");
        for (String name : argListPassTwo.keySet()) {
            String key = name.toString();
            String value = argListPassTwo.get(name).toString();
            System.out.println(value.replace("#", "") + "\t" + key);
        }
        System.out.println("\nMDT");
        System.out.println("Index\tDefinitions");
        String macroDataArr[] = macroData.split("\n");
        for (Integer i = 0; i < macroDataArr.length; i++) {
            System.out.println(i.toString() + "\t" + macroDataArr[i]);
        }

    }

    public static void main(String[] args) throws Exception {
        readFile();
        System.out.println("Macro Code: ");
        System.out.println(codeString);
        identifySpecifics();
        passOneRoller();
        System.out.println("Pass One: ");
        passOnePrinter();
        passTwoRoller();
        System.out.println("\nPass Two: ");
        passTwoPrinter();
    }
}

/*
diparth@skillbox:~/pgm$ javac Macro.java
diparth@skillbox:~/pgm$ java Macro
Macro Code: 
ABC START
MACRO
ADD &ARG1,&ARG2
L 1,&ARG1
A 1,&ARG2
MEND
ADD DATA1,DATA2
DATA1 DC F'9'
DATA2 DC F'4'
END

Pass One: 

MNT
Name : ADD  Index: 1

ALA
Index	Arguments
2	&ARG2
1	&ARG1

MDT
Index	Definitions
0	MACRO
1	ADD #1,#2
2	L 1,#1
3	A 1,#2
4	MEND

Pass Two: 

MNT
Name : ADD  Index: 1

ALA
Index	Arguments
2	DATA2
1	DATA1

MDT
Index	Definitions
0	MACRO
1	ADD DATA1,DATA2
2	L 1,DATA1
3	A 1,DATA2
4	MEND
*/
