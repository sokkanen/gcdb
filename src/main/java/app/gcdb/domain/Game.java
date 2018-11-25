package app.gcdb.domain;

/**
 * Peliolio, joka sisältää pelin nimen, alustan, kunnon ja sisällön.
 */
public class Game implements Comparable<Game> {

    private String name;
    private int id;

    public enum Condition {
        MINT,
        NEAR_MINT,
        EXCELLENT,
        VERY_GOOD,
        GOOD,
        FAIR,
        POOR,
        NOT_DEFINED;
    }

    public enum Content {
        NIB,
        CIB,
        CB,
        CI,
        C,
        NOT_DEFINED;
    }
    private Condition condition;
    private Content content;
    private String comment;

    public Game(String name, Platform platform, Condition cnd, Content cnt, int id, String comment) {
        this.name = name;
        this.condition = cnd;
        this.content = cnt;
        this.id = id;
        this.comment = comment;
    }

    public Game(String name, Platform platform) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Game comparable) {
        return this.name.compareTo(comparable.name);
    }
}
