import java.util.ArrayList;

/**
 * Created by 李潇 on 2016/3/26.
 */
/*
 * divide instruction into several parts
 *
 */
class Parser {
    private String currentLine;
    private ArrayList<String> instructions;
    private int index = -1;
    private ArrayList<String> newInstructions = new ArrayList<>();
    public enum COMMAND {
        A_COMMAND,
        C_COMMAND,
        L_COMMAND
    }

    //
    Parser(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public boolean hasMoreCommands() {
        return !(index >= instructions.size());
    }

    public void advance() {
        boolean ignore = false;
        while (!ignore) {
            index++;
            String temp = instructions.get(index).replaceAll(" ", "");
            // is comment?
            if (temp.contains("//")) {
                ignore = true;
            }
            // all white space?
            else if (temp.length() == 0) {
                ignore = true;
            }
            // is a valid instruction
            else {
                ignore = false;
                currentLine = temp;
            }
        }
        newInstructions.add(currentLine);
    }

    public COMMAND commandType() {
        if (currentLine.charAt(0) == '@') {
            return COMMAND.A_COMMAND;
        } else if (currentLine.charAt(0) == '(') {
            return COMMAND.L_COMMAND;
        } else {
            return COMMAND.C_COMMAND;
        }

    }

    public String symbol() {
        if (commandType() == COMMAND.A_COMMAND || commandType() == COMMAND.L_COMMAND) {
            return currentLine.substring(1);
        }
        return null;
    }

    public String dest() {
        if (commandType() == COMMAND.C_COMMAND && currentLine.contains("=")) {
            String[] arr = currentLine.split("=");
            return arr[0];
        }
        return null;
    }

    public String comp() {
        if (commandType() == COMMAND.C_COMMAND) {
            String temp = currentLine;
            if (temp.contains("=")) {
                temp = temp.split("=")[1];
            }
            if (temp.contains(";")) {
                temp = temp.split(";")[0];
            }
            return temp;
        }
        return null;
    }

    public String jump() {
        if (commandType() == COMMAND.C_COMMAND && currentLine.contains(";")) {
            String[] arr = currentLine.split(";");
            return arr[1];
        }
        return null;
    }

}
