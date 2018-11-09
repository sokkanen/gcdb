package app.gcdb.domain;

/** Yksinkertainen Peliolio. Luokka on käytännössä olemassa tulevaisuuden laajennuksia silmällä pitäen. Nykyisellään lähinnä selkeyttää koodia*/

public class Game {
    
    private String name;
    private Platform platform;
    
    public Game(String name, Platform platform){
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
    public String toString(){
        return platform.getName() + ": " + this.name;
    }
}
