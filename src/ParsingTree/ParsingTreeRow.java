package ParsingTree;

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index){
        this.index = index;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public ParsingTreeRow getParent() {
        return parent;
    }

    public void setParent(ParsingTreeRow parent){
        this.parent = parent;
    }

    public ParsingTreeRow getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(ParsingTreeRow rightSibling){
        this.rightSibling = rightSibling;
    }

    public ParsingTreeRow getLeftChild(){
        return this.leftChild;
    }

    public void setLeftChild(ParsingTreeRow leftChild){
        this.leftChild = leftChild;
    }

    public Integer getLevel(){
        return this.level;
    }

    public void setLevel(Integer level){
        this.level = level;
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
