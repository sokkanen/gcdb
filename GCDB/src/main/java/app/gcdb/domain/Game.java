package app.gcdb.domain;

/**
 * Peliolio, joka sisältää pelin nimen, alustan, kunnon ja sisällön.
 */
public class Game implements Comparable<Game>{

    private String name;
    private Platform platform;

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

    public Game(String name, Platform platform, Condition cnd, Content cnt) {
        this.name = name;
        this.platform = platform;
        this.condition = cnd;
        this.content = cnt;
    }

    public Game(String name, Platform platform) {
        this.name = name;
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    public int compareTo(Game comparable) {
        return this.name.compareTo(comparable.name);
    }

    @Override
    public String toString() {
        return platform.getName() + ": " + this.name;
    }
}
