package ArbolSintactico;

public class Whilex extends Statx {
    private Expx condition;
    private Statx body;

    public Whilex(Expx condition, Statx body) {
        this.condition = condition;
        this.body = body;
    }

    public Expx getCondition() {
        return condition;
    }

    public Statx getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "While (" + condition + ") Do " + body;
    }
}