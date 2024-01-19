package ParsingTree;

import lombok.Data;

@Data
public class ParsingTreeRow {

    private Integer index;
    private String info;
    private ParsingTreeRow parent; // the state from which the current state was derived by performing SHIFT/REDUCE
    private ParsingTreeRow rightSibling; // right sibling of the current node
    private ParsingTreeRow leftChild;
    private Integer level;

    public ParsingTreeRow(String info) {
        this.info = info;
    }

    @Override
    public String toString(){

        return "\tParsingTreeRow {" +
                "\n\t\t\t\tindex = " + index +
                ", \n\t\t\t\tinfo = " + info +
                ", \n\t\t\t\tleftChild = " + (leftChild != null ? leftChild.getIndex() : -1) +
                ", \n\t\t\t\trightChild = " + (rightSibling != null ? rightSibling.getIndex() : -1) +
                ", \n\t\t\t\tparent = " + (parent != null ? parent.getIndex() : -1) +
                ", \n\t\t\t\tlevel = " + level +
                "\n\t\t}";
    }
}
