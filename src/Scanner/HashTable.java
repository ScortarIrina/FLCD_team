package Scanner;

import Utils.Pair;
import lombok.Data;

import java.util.ArrayList;

@Data
public class HashTable {
    private Integer size;
    private ArrayList<ArrayList<String>> hashTable;

    public HashTable(Integer size) {
        this.size = size;
        this.hashTable = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.hashTable.add(new ArrayList<>());
        }
    }

    /**
     * This method searches for the position of a term from the hash table
     *
     * @param elem - the element for which we want to find the position
     * @return - the position of the element
     */
    public Pair<Integer, Integer> findPositionOfTerm(String elem) {
        int pos = hash(elem);

        if (!hashTable.get(pos).isEmpty()) {
            ArrayList<String> elems = this.hashTable.get(pos);
            for (int i = 0; i < elems.size(); i++) {
                if (elems.get(i).equals(elem)) {
                    return new Pair<>(pos, i);
                }
            }
        }

        return null;
    }

    /**
     * This method hashes the given key
     *
     * @param key - the element for which we compute the hash
     * @return - the hash of the element
     */
    private Integer hash(String key) {
        int sum_chars = 0;
        char[] key_characters = key.toCharArray();
        for (char c : key_characters) {
            sum_chars += c;
        }
        return sum_chars % size;
    }

    /**
     * This method checks if the hashtable contains a given element
     *
     * @param elem - the element to look for
     * @return - true if the element is in the hash table, false otherwise
     */
    public boolean containsTerm(String elem) {
        return this.findPositionOfTerm(elem) != null;
    }

    /**
     * This method adds a new element in the hash table
     *
     * @param elem - the element to be added
     * @return - true if the element was added successfully, false if the element was already in the hash table
     */
    public boolean add(String elem) {
        // return false if the element is already in the hash table
        if (containsTerm(elem)) {
            return false;
        }

        // get the hash of the element
        Integer pos = hash(elem);

        ArrayList<String> elems = this.hashTable.get(pos);
        elems.add(elem);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder computedString = new StringBuilder();
        for (int i = 0; i < this.hashTable.size(); i++) {
            if (!this.hashTable.get(i).isEmpty()) {
                computedString.append(i);
                computedString.append(" - ");
                computedString.append(this.hashTable.get(i));
                computedString.append("\n");
            }
        }
        return computedString.toString();
    }

}
