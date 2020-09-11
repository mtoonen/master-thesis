package nl.meine.master.testsuite;

public class Label {
    public Label(String label, Integer confidence) {
        this.label = label;
        this.confidence = confidence;
    }

    private String label;
    private Integer confidence;

    public String getLabel() {
        return label;
    }

    public Integer getConfidence() {
        return confidence;
    }
}
