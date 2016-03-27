import java.io.*;
import java.util.ArrayList;

/**
 * Created by 李潇 on 2016/3/26.
 */

public class HackAssembler {
    public ArrayList<String> instructions = new ArrayList<String>();
    private Parser parser;
    private SymbolTable symbolTable;

    public void HackAssembler(String path) {
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
        HackAssembler ha = new HackAssembler();
        SymbolTable st = ha.symbolTable;
        ArrayList<String> instructions = new ArrayList<>();
        // first pass
        Parser parser = ha.parser;
        int lineNumber = 0;
        while (parser.hasMoreCommands()) {
            if (parser.commandType() == Parser.COMMAND.L_COMMAND) {
                st.addEntry(parser.symbol(), lineNumber + 1);
            } else {
                lineNumber++;
            }
            parser.advance();
        }
        // second pass
        ha.resetParser();
        parser = ha.parser;
        lineNumber = 0;
        int n = 16;
        while (parser.hasMoreCommands()) {
            // 这里是否要精简？精简容易debug
            String currentCommand = null;
            if (parser.commandType() == Parser.COMMAND.A_COMMAND) {
                // is a number
                if (ha.isInteger(parser.symbol())) {
                    continue;
                }
                // if this is a variable
                if (!st.contains(parser.symbol())) {
                    st.addEntry(parser.symbol(), n++);
                }
                int addr = st.GetAddress(parser.symbol());

//                currentCommand = String.format("%16s", Integer.toBinaryString(addr)).replace(' ', '0');
            } else if (parser.commandType() == Parser.COMMAND.C_COMMAND) {
                currentCommand = parser.dest() + parser.comp() + parser.jump();
            }
            if (currentCommand != null) {
                instructions.add(currentCommand);
            }
        }
        // main loop
        ha.resetParser(instructions);
        parser = ha.parser;
        ArrayList<String> machines = new ArrayList<>();
        Code code = new Code();
        String machine = null;
        while (parser.hasMoreCommands()) {
            if (parser.commandType() == Parser.COMMAND.A_COMMAND) {
                machine = String.format("%16s", Integer.toBinaryString(Integer.parseInt(parser.symbol()))).replace(' ', '0');
            } else if (parser.commandType() == Parser.COMMAND.C_COMMAND) {
                machine = "111" + code.dest(parser.dest()) + code.comp(parser.comp()) + code.jump(parser.jump());
            }
            machines.add(machine);
            parser.advance();
        }
        
    }

}
