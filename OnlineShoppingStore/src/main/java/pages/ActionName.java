package pages;

public enum ActionName {
    CANCEL("Cancel"), SHOW("Show"), FINISH("Finish");
    private final String label;

    ActionName(String label) {
        this.label = label;
    }
    public String getName() {
        return this.label;
    }
}
