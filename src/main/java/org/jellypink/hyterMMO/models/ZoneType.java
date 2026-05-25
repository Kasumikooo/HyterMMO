package org.jellypink.hyterMMO.models;

public enum ExternalZoneType {

    GREEN("&aGreen"),
    YELLOW("&eYellow"),
    RED("&cRed"),
    BLACK("&8Black");

    private final String displayName;

    ExternalZoneType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ExternalZoneType fromString(String text) {
        for (ExternalZoneType type : ExternalZoneType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
