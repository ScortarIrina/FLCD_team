package Scanner;

import Utils.Pair;
import lombok.Data;

@Data
public class SymbolTable {
    private Integer size;
    private HashTable hashTable;

    public SymbolTable(Integer size){
        hashTable = new HashTable(size);
    }

    public Pair findPositionOfTerm(String term){
        return hashTable.findPositionOfTerm(term);
    }

    public boolean addToST(String term){
        return hashTable.add(term);
    }

    @Override
    public String toString(){
        return this.hashTable.toString();
    }
}
