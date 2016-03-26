import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 李潇 on 2016/3/26.
 */

public class HackAssembler {
    private ArrayList<String> instructions = new ArrayList<String>();
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
    private void readFile (String fileName) {
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

    public void resetParser() {
        parser = new Parser(instructions);
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
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
            }
        }
        // main loop
    }

}
