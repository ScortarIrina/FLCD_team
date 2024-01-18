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
     * With this method we keep track of which states are created by another state and through what symbol
     *
     * @param indexFirstState  - the index of the state we start from
     * @param indexSecondState - the index of the state we obtained
     * @param symbol           - the symbol we goTo
     */

    public void connectStates(Integer indexFirstState, Integer indexSecondState, String symbol){
        this.adjacencyList.put(new Pair<>(indexFirstState, symbol), indexSecondState);
    }

    /**
     * With this method we add a new state to the list of states
     * @param state - the state we are about to add
     */
    public void addState(State state){
        this.states.add(states.size(), state);
    }
}
