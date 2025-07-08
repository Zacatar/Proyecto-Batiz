package ArbolSintactico;

public class Ifx extends Statx {
    private Expx condition;
    private Statx thenBlock;
    private Statx elseBlock;

    public Ifx(Expx condition, Statx thenBlock, Statx elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Expx getCondition() {
        return condition;
    }

    public Statx getThenBlock() {
        return thenBlock;
    }

    public Statx getElseBlock() {
        return elseBlock;
    }

    @Override
    public String toString() {
        return "If (" + condition + ") Then " + thenBlock + " Else " + elseBlock;
    }
    
}
