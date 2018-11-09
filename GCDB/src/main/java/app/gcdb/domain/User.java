package app.gcdb.domain;

public class User {
    private String username;
    private int passHash;
    
    public User(String username, String password){
        this.username = username;
        this.passHash = generatePassHash(password);
    }
    
    public int generatePassHash(String passWord){
        return passWord.hashCode();
    }

    public String getUsername() {
        return username;
    }

    public int getPassHash() {
        return passHash;
    }
    
}