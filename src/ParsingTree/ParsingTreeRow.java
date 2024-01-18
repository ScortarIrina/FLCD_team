package ParsingTree;

import lombok.Data;

@Data
public class ParsingTreeRow {

    private Integer index;
    private String info;
    private ParsingTreeRow parent;
    private ParsingTreeRow rightSibling;
    private ParsingTreeRow leftChild;
    private Integer level;

    public ParsingTreeRow(String info) {
        this.info = info;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("\tParsingTree.ParsingTreeRow {");
        result.append("\n\t\t\t\tindex = ").append(index);
        result.append(", \n\t\t\t\tinfo = ").append(info);
        result.append(", \n\t\t\t\tleftChild = ").append(leftChild != null ? leftChild.getIndex() : -1);
        result.append(", \n\t\t\t\trightChild = ").append(rightSibling != null ? rightSibling.getIndex() : -1);
        result.append(", \n\t\t\t\tparent = ").append(parent != null ? parent.getIndex() : -1);
        result.append(", \n\t\t\t\tlevel = ").append(level);
        result.append("\n\t\t}");

        return result.toString();
    }
}
