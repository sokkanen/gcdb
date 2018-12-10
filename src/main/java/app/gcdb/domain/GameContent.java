package app.gcdb.domain;

public enum GameContent {
    NOT_DEFINED(1, "Not defined"),
    C(2, "Cart only"),
    CI(3, "Cart with Instr."),
    CB(4, "Cart with Box"),
    CIB(5, "Complete in Box"),
    NIB(6, "New in Box");

    private final int contentNr;
    private final String contentStr;

    GameContent(int contentNr, String contentStr) {
        this.contentNr = contentNr;
        this.contentStr = contentStr;
    }

    public int getCntNr() {
        return contentNr;
    }

    public String getCntStr() {
        return contentStr;
    }

}
