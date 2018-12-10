package app.gcdb.domain;

/**
 * Game-luokalla kuvataan pelejä. Muuttujina id, nimi, alusta, kunto, alue ja kommentti.
 */
public class Game implements Comparable<Game> {

    private String name;
    private int id;

    private int condition;
    private int content;
    private String comment;
    private int platform;
    private String region;

    public Game(String name, int platform, int condition, int content, int id, String region, String comment) {
        this.name = name;
        this.platform = platform;
        this.condition = condition;
        this.content = content;
        this.id = id;
        this.comment = comment;
        this.region = region;
    }

    public Game(String name, int platform, int condition, int content, int id, String comment) {
        this.name = name;
        this.platform = platform;
        this.condition = condition;
        this.content = content;
        this.id = id;
        this.comment = comment;
        this.region = "";
    }

    public Game(String name, int condition, int content, String region, String comment) {
        this.name = name;
        this.condition = condition;
        this.content = content;
        this.region = region;
        this.comment = comment;
    }

    public Game(int id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
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

    /**
     * Metodi vertaa kahta Game-oliota nimien perusteella.
     *
     * @param comparable verrattava Game-olio.
     *
     * @return Palauttaa -1,0 tai 1 riippuen verrattavien keskenäisestä
     * järjestyksestä.
     */
    @Override
    public int compareTo(Game comparable) {
        return this.name.compareTo(comparable.name);
    }
}
