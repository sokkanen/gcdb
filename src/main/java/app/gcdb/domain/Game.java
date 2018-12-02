package app.gcdb.domain;

/**
 * Peliolio, joka sisältää pelin nimen, alustan, kunnon ja sisällön.
 */
public class Game implements Comparable<Game> {

    private String name;
    private int id;

    // Pelin kunto 1-10 GEM_MINT, MINT, NEAR_MINT, EXCELLENT, VERY_GOOD, GOOD, FAIR, POOR, VERY_POOR, NOT_DEFINED
    // Sisältö 1-6 NIB, CIB, CB, CI, C, NOT_DEFINED;
    private int condition;
    private int content;
    private String comment;
    private int platform;

    public Game(String name, int platform, int condition, int content, int id, String comment) {
        this.name = name;
        this.platform = platform;
        this.condition = condition;
        this.content = content;
        this.id = id;
        this.comment = comment;
    }

    public Game(String name, int condition, int content, String comment) {
        this.name = name;
        this.condition = condition;
        this.content = content;
        this.comment = comment;
    }

    public Game(int id) {
        this.id = id;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCondition() {
        return condition;
    }

    public int getContent() {
        return content;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int compareTo(Game comparable) {
        return this.name.compareTo(comparable.name);
    }
}
