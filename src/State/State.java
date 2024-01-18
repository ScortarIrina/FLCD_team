package State;

import LR0.Grammar;
import lombok.Data;

import java.util.*;

@Data
public class State {
    private ActionTypeEnum stateActionType;
    private final Set<Item> items;

    public State(Set<Item> states){
        this.items = states;
        this.setActionForState();
    }

    /**
     * With this method we get the symbols which come after the dot
     *
     * @return a list with the corresponding symbols
     */
    public List<String> getPartAfterDot(){
        Set<String> symbols = new LinkedHashSet<>();

        for(Item i: items){
            if(i.getDotLocation() < i.getRhs().size())
                symbols.add(i.getRhs().get(i.getDotLocation()));
        }

        return new ArrayList<>(symbols);
    }

    /**
     * This method sets the action for the state (SHIFT, REDUCE, ACCEPT, REDUCE_REDUCE_CONFLICT, SHIFT_REDUCE_CONFLICT)
     */
    public void setActionForState(){
        if(items.size() == 1 && ((Item)items.toArray()[0]).getRhs().size() == ((Item)items.toArray()[0]).getDotLocation()
                && Objects.equals(((Item) this.items.toArray()[0]).getLhs(), Grammar.firstState)){
            this.stateActionType = ActionTypeEnum.ACCEPT;
        } else if(items.size() == 1 && ((Item) items.toArray()[0]).getRhs().size() == ((Item) items.toArray()[0]).getDotLocation())
        {
            this.stateActionType = ActionTypeEnum.REDUCE;
        } else if(!items.isEmpty() && this.items.stream().allMatch(i -> i.getRhs().size() > i.getDotLocation())){
            this.stateActionType = ActionTypeEnum.SHIFT;
        } else if(items.size() > 1 && this.items.stream().allMatch(i -> i.getRhs().size() == i.getDotLocation())){
            this.stateActionType = ActionTypeEnum.REDUCE_REDUCE_CONFLICT;
        } else {
            this.stateActionType = ActionTypeEnum.SHIFT_REDUCE_CONFLICT;
        }
    }

    @Override
    public int hashCode(){
        return Objects.hash(items);
    }

    @Override
    public boolean equals(Object item){
        if(item instanceof  State){
            return ((State) item).getItems().equals(this.getItems());
        }

        return false;
    }

    @Override
    public String toString(){
        return stateActionType + " - " + items;
    }

}
