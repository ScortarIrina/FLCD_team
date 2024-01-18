package State;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * An item has the form [A->alpha.beta]
 * Here the idea is the following one, the element preceded by the dot is
 * considered to be the element from the dot position (that's how we represent the dot
 * without actually representing it physically)
 * We have a string for the left hand side, a list of strings for the right hand side
 * and the position for the dot
 */
@Data
@AllArgsConstructor
public class Item {
    private String lhs;
    private List<String> rhs;
    private Integer dotLocation;

    public boolean dotIsLast(){
        return this.dotLocation == this.rhs.size();
    }

    @Override
    public int hashCode(){
        return Objects.hash(lhs, rhs, dotLocation);
    }

    @Override
    public boolean equals(Object item) {
        return item instanceof Item && Objects.equals(((Item)item).lhs, this.lhs) && ((Item)item).rhs == rhs
                && Objects.equals(((Item)item).dotLocation, this.dotLocation);
    }

    @Override
    public String toString(){
        List<String> rhs1 = this.rhs.subList(0, dotLocation);
        String rhs1_str = String.join("", rhs1);
        List<String> rhs2 = this.rhs.subList(dotLocation, this.rhs.size());
        String lhs1_str = String.join("", rhs2);
        return lhs + " -> " + rhs1_str + "." + lhs1_str;
    }
}
