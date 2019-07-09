import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Assembler {

    public static StringBuilder codeBuilder = new StringBuilder();
    public static String codeString = new String();
    public static String startAddress = "";
    public static String baseRegister = "";
    public static HashMap<String, String> symbolTable = new HashMap<String, String>();
    public static HashMap<String, Integer> symbolLocations = new HashMap<String, Integer>();
    public static String passOneCode = "";
    public static String passTwoCode = "";
    public static String forwardRefCode = "";
    
    public static void readFile() throws Exception {

        String fileName = "asmcode.txt";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        while(line != null) {
            codeBuilder.append(line).append("\n");
            line = reader.readLine();
        }
        reader.close();
        codeString = codeBuilder.toString();
    }

    public static void identifySpecifics() {

        String startAddrAndBaseReg = "\\s*[A-Z]+\\s*START\\s*(\\d+)\\n*\\s*USING\\s*\\*,\\s*\\n*(\\d+)\\s*\\n*";
        String scanSymbols = "^([A-Z]{4})\\s*(?:DC|DS)\\s*(?:F'(\\d+)'|(\\d+)F)*";

        Pattern pattern = Pattern.compile(startAddrAndBaseReg, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(codeString);
        if (matcher.find()) {
            startAddress = matcher.group(1);
            baseRegister = matcher.group(2);
        }
        
        pattern = Pattern.compile(scanSymbols, Pattern.MULTILINE);
        matcher = pattern.matcher(codeString);
        while (matcher.find()) {
            Integer lc =  getLineNumberFromIndex(matcher.start(1)) - 3;
            Integer addr = lc * 4;
            if (matcher.group(3) == null) {
                symbolTable.put(matcher.group(1), matcher.group(2));
                symbolLocations.put(matcher.group(1), addr );
                forwardRefCode = forwardRefCode + addr.toString() + "  " + matcher.group(2) + "\n";
            }
            else {
                symbolTable.put(matcher.group(1), "-");
                symbolLocations.put(matcher.group(1), addr );
                forwardRefCode = forwardRefCode + addr.toString() + "  " + "-" + "\n";
            }

            
        }
    }

    public static void assemblerPassCodeRoller() {

        String instRegex = "^(?:(L|A|ST)\\s*\\d+\\s*,\\s*([A-Z]{4}))";
        Pattern pattern = Pattern.compile(instRegex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(codeString);
        Integer locationCount = 0;
        String codeStringArr[] = codeString.split("\n");

        while(matcher.find()) {

            String passOne = codeStringArr[getLineNumberFromIndex(matcher.start(2))-1].replace(matcher.group(2), "_("+ startAddress +","+ baseRegister +")");
            passOne = locationCount.toString() + "  " + passOne + "\n";
            passOneCode = passOneCode + passOne;

            String passTwo = codeStringArr[getLineNumberFromIndex(matcher.start(2))-1].replace(matcher.group(2), ""+ symbolLocations.get(matcher.group(2))+"("+ startAddress +","+ baseRegister +")");
            passTwo = locationCount.toString() + "  " + passTwo + "\n";
            passTwoCode = passTwoCode + passTwo;

            locationCount = locationCount + 4;
        }
        passOneCode = passOneCode + forwardRefCode;
        passTwoCode = passTwoCode + forwardRefCode;
    }

    public static Integer getLineNumberFromIndex(Integer index) {
        Integer c = 1;
        Integer line = 1, col = 1;
        while (c <= index) {
            if (codeString.charAt(c) == '\n') {
                ++line;
                col = 1;
            } 
            else {
                ++col;
            }
            c++;
        }
        return line;
    }

    public static void main(String[] args) throws Exception {
        readFile();
        identifySpecifics();
        assemblerPassCodeRoller(); 
        System.out.println("Assembly code:");
        System.out.println(codeString);
        System.out.println("Pass One:");
        System.out.println(passOneCode);
        System.out.println("Pass Two:");
        System.out.println(passTwoCode);
    }
}
/*
diparth@skillbox:~/pgm$ javac Assembler.java
diparth@skillbox:~/pgm$ java Assembler
Assembly code:
JOHN START 0
USING *, 15
L 1, FIVE
A 1, FOUR
ST 1, TEMP
FOUR DC F'4'
FIVE DC F'5'
TEMP DS 1F
END

Pass One:
0  L 1, _(0,15)
4  A 1, _(0,15)
8  ST 1, _(0,15)
12  4
16  5
20  -

Pass Two:
0  L 1, 16(0,15)
4  A 1, 12(0,15)
8  ST 1, 20(0,15)
12  4
16  5
20  -

*/
