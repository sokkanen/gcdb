package app.gcdb.domain;

public enum GameCondition {
    NOT_DEFINED(1, "Not defined"),
    VERY_POOR(2, "Very Poor"),
    POOR(3, "Poor"),
    FAIR(4, "Fair"),
    GOOD(5, "Good"),
    VERY_GOOD(6, "Very good"),
    EXCELLENT(7, "Excellent"),
    NEAR_MINT(8, "Near mint"),
    MINT(9, "Mint"),
    GEM_MINT(10, "Gem mint");

    private final int conditionNr;
    private final String conditionStr;

    GameCondition(int conditionNr, String conditionStr) {
        this.conditionNr = conditionNr;
        this.conditionStr = conditionStr;
    }

    public int getCndNr() {
        return conditionNr;
    }

    public String getCndStr() {
        return conditionStr;
    }
}
