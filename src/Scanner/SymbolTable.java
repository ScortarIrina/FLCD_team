package Scanner;

import Utils.Pair;
import lombok.Data;

@Data
public class SymbolTable {
    private Integer size;
    private HashTable hashTable;

    public SymbolTable(Integer size) {
        hashTable = new HashTable(size);
    }

    public Pair findPositionOfTerm(String term) {
        return hashTable.findPositionOfTerm(term);
    }

    public void addToST(String term) {
        hashTable.add(term);
    }

    @Override
    public String toString() {
        return this.hashTable.toString();
    }
}
