import java.io.*;
import java.util.ArrayList;

/**
 * Created by 李潇 on 2016/3/26.
 */

public class HackAssembler {
    public ArrayList<String> instructions = new ArrayList<String>();
    private Parser parser;
    private SymbolTable symbolTable;

    HackAssembler(String path) {
        // read file
        readFile(path);
        // init parser
        parser = new Parser(instructions);
        // init symbol table
        symbolTable = new SymbolTable();
    }

    private void readFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                this.instructions.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetParser(ArrayList<String> instructions) {
        parser = new Parser(instructions);
    }

    public void resetParser() {
        parser = new Parser(instructions);
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String args[]) {
        String fileName = "src/Add.asm";
        HackAssembler ha = new HackAssembler(fileName);
        SymbolTable st = ha.symbolTable;
        ArrayList<String> instructions = new ArrayList<>();
        // first pass
        Parser parser = ha.parser;
        int lineNumber = 0;
        while (true) {
            String info = parser.getCurrentLine();
            if (parser.commandType() == Parser.COMMAND.L_COMMAND) {
                String symbol = parser.symbol();
                st.addEntry(parser.symbol(), lineNumber);
            } else {
                lineNumber++;
            }
            if (parser.hasMoreCommands()) {
                parser.advance();
            } else {
                break;
            }
        }
        // second pass
        ha.resetParser();
        parser = ha.parser;
        lineNumber = 0;
        int n = 16;
        while (true) {
            // 这里是否要精简？精简容易debug
            String currentCommand = null;
            if (parser.commandType() == Parser.COMMAND.A_COMMAND) {
                String addr = null;
                // is a number
                if (ha.isInteger(parser.symbol())) {
                    addr = parser.symbol();
                }
                // if this is a variable
                else if (!st.contains(parser.symbol())) {
                    String info = parser.symbol();
                    st.addEntry(parser.symbol(), n++);
                    addr = ((Integer) st.GetAddress(parser.symbol())).toString();
                } else {
                    addr = ((Integer) st.GetAddress(parser.symbol())).toString();
                }
                currentCommand = "@" + addr;
            } else if (parser.commandType() == Parser.COMMAND.C_COMMAND) {
                if (parser.dest() != "") currentCommand = parser.dest() + "=" + parser.comp();
                else currentCommand = parser.comp();
                if (parser.jump() != "") currentCommand += ";" + parser.jump();
            }
            if (currentCommand != null) {
                instructions.add(currentCommand);
            }
            if (parser.hasMoreCommands()) {
                parser.advance();
            } else {
                break;
            }
        }
        // main loop
        ha.resetParser(instructions);
        parser = ha.parser;
        ArrayList<String> machines = new ArrayList<>();
        Code code = new Code();
        String machine = null;
        while (true) {
            if (parser.commandType() == Parser.COMMAND.A_COMMAND) {
                machine = String.format("%16s", Integer.toBinaryString(Integer.parseInt(parser.symbol()))).replace(' ', '0');
            } else if (parser.commandType() == Parser.COMMAND.C_COMMAND) {
                machine = "111" + code.comp(parser.comp()) + code.dest(parser.dest()) + code.jump(parser.jump());
            }
            machines.add(machine);
            if (parser.hasMoreCommands()) {
                parser.advance();
            } else {
                break;
            }
        }
        String newName = fileName.replace("asm", "hack");
        File writename = new File(newName); // 相对路径，如果没有则要建立一个新的output。txt文件
        try {
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            for (int i = 0; i < machines.size(); i++) {
                out.write(machines.get(i) + "\r\n");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
