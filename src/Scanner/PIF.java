package Scanner;

import Utils.Pair;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PIF {
    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;
    private List<Integer> types;

    public PIF() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    /**
     * This method adds a token/identifier/constant to the corresponding list, their position in the symbol table (ST),
     * and also adds the category in the list of types.
     *
     * @param pair - pair composed of the token/constant/identifier + its position in the ST
     * @param type - category of the token (2, 3, 4) or constant (0) or identifier (1)
     */
    public void add(Pair<String, Pair<Integer, Integer>> pair, Integer type){
        this.tokenPositionPair.add(pair);
        this.types.add(type);
    }

    @Override
    public String toString(){
        StringBuilder computedString = new StringBuilder();
        for(int i = 0; i < this.tokenPositionPair.size(); i++) {
            computedString.append(this.tokenPositionPair.get(i).getFirst())
                    .append(" - ")
                    .append(this.tokenPositionPair.get(i).getSecond())
                    .append(" -> ")
                    .append(types.get(i))
                    .append("\n");
        }

        return computedString.toString();
    }
}
