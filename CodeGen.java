public class CodeGen {

    public static String[] stmt = new String[100];
    public static Integer n = 4;

    public static void input() {

        stmt[0] = "a=b";
        stmt[1] = "f=c+d";
        stmt[2] = "e=a-f";
        stmt[3] = "g=b*c";
    }

    public static void generateAsm() {

        for(Integer i = 0; i < n; i++) {

            String expression[] = stmt[i].split("\\=");
            String tokens[] = expression[1].split("");

            if(expression[1].contains("+")) {
                System.out.println("MOV R" + i + "," + tokens[0]);
                System.out.println("ADD R" + i + "," + tokens[2]);
                System.out.println("MOV " + expression[0] + ",R" + i);
            }
            else if(expression[1].contains("-")) {
                System.out.println("MOV R" + i + "," + tokens[0]);
                System.out.println("SUB R" + i + "," + tokens[2]);
                System.out.println("MOV " + expression[0] + ",R" + i);
            }
            else if(expression[1].contains("*")) {
                System.out.println("MOV R" + i + "," + tokens[0]);
                System.out.println("MUL R" + i + "," + tokens[2]);
                System.out.println("MOV " + expression[0] + ",R" + i);
            }
            else {
                System.out.println("MOV R" + i + "," + tokens[0]);
                System.out.println("MOV " + expression[0] + ",R"+ i);
            }
        }
    }

    public static void main(String[] args) {
        input();
        generateAsm();
    }
}

/*
D:\sem6\spcc>javac CodeGen.java
D:\sem6\spcc>java CodeGen
MOV R0,b
MOV a,R0
MOV R1,c
ADD R1,d
MOV f,R1
MOV R2,a
SUB R2,f
MOV e,R2
MOV R3,b
MUL R3,c
MOV g,R3
*/