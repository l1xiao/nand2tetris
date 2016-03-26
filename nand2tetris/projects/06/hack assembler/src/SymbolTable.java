import java.util.HashMap;

/**
 * Created by 李潇 on 2016/3/26.
 */
public class SymbolTable {
    private HashMap<String, Integer> table;
    SymbolTable() {
        table = new HashMap<>();
        table.put("SP", 0);
        table.put("LCL", 1);
        table.put("ARG", 2);
        table.put("THIS", 3);
        table.put("THAT", 4);
        table.put("SCREEN", 16384);
        table.put("KBD", 24576);
        for (int i = 0; i < 16; i++) {
            table.put("R"+i, i);
        }
    }
    public void addEntry(String symbol, int address) {
        table.put(symbol, address);
    }
    public  boolean contains(String symbol) {
        return table.containsKey(symbol);
    }
    public int GetAddress(String symbol) {
        return table.get(symbol);
    }
}
