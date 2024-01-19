package Utils;

import lombok.Data;

@Data
public class Pair<First,Second> {
    private final First first;
    private final Second second;

    @Override
    public String toString() {
        return "Pair(" +
                "first = " + first +
                ", second = " + second +
                ')';
    }
}
