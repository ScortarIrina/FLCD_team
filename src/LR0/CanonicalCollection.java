package LR0;

import State.State;
import Utils.Pair;
import lombok.Data;

import java.util.*;

@Data
public class CanonicalCollection {

    private List<State> states;
    private Map<Pair<Integer, String>, Integer> adjacencyList;

    public CanonicalCollection(){
        this.states = new ArrayList<>();
        this.adjacencyList = new LinkedHashMap<>();
    }

    /**
     * This method Keeps track of the states that were created by another state and through what symbol
     * @param indexFirstState - the starting state index
     * @param indexSecondState - the obtained state index
     * @param symbol - the symbol we goTo
     */

    public void connectStates(Integer indexFirstState, Integer indexSecondState, String symbol){
        this.adjacencyList.put(new Pair<>(indexFirstState, symbol), indexSecondState);
    }

    /**
     * This method adds a new state to the list of states
     * @param state - the state to be added
     */
    public void addState(State state){
        this.states.add(states.size(), state);
    }
}
