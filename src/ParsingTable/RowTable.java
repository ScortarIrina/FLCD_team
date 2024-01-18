package ParsingTable;

import State.ActionTypeEnum;
import Utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class RowTable {

    public int stateIndex;

    public ActionTypeEnum action;

    public String reduceNonTerminal;

    public List<String> reduceContent = new ArrayList<>();

    public List<Pair<String, Integer>> shifts = new ArrayList<>();

    public String reduceProductionString() {
        return this.reduceNonTerminal + " -> " + this.reduceContent;
    }

    @Override
    public String toString() {
        return "Row {" +
                "\n\t\tstateIndex = " + stateIndex +
                ",\n\t\taction ='" + action + '\'' +
                ",\n\t\treduceNonTerminal ='" + reduceNonTerminal + '\'' +
                ",\n\t\treduceContent = " + reduceContent +
                ",\n\t\tshifts = " + shifts +
                "\n\t}";
    }

}
