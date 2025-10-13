package at.technikum.model.enums;



public enum AgeRestriction {
    ALL,
    AGE_6,
    AGE_12,
    AGE_16,
    AGE_18;

    public static AgeRestriction fromString(String value) {
        switch (value.trim().toUpperCase()) {
            case "ALL": return ALL;
            case "6": case "AGE_6": return AGE_6;
            case "12": case "AGE_12": return AGE_12;
            case "16": case "AGE_16": return AGE_16;
            case "18": case "AGE_18": return AGE_18;
            default: throw new IllegalArgumentException("Unknown age restriction: " + value);
        }
    }

    public String getDisplayName() {
        if (this == ALL) return "All Ages";
        return this.name().replace("AGE_", "") + "+";
    }
}
