package com.stronans.domotics.model.measurements;

public enum MeasureType {

    NONE(""),
    Temperature("TEMPERATURE"),
    Moisture("MOISTURE");

    private String measureType;

    MeasureType(String measureType) {
        this.measureType = measureType;
    }

    public static String getOrdinal(int i) {
        String result = NONE.getVal();
        for (MeasureType m : MeasureType.values()) {
            if (m.ordinal() == i) {
                result = m.getVal();
                break;
            }
        }
        return result;
    }

    public static MeasureType match(String measurementString) {
        MeasureType result = NONE;

        switch (measurementString.toUpperCase()) {
            case "":
                break;

            case "TEMPERATURE":
                result = Temperature;
                break;

            case "MOISTURE":
                result = Moisture;
                break;

            default:
                throw new MeasureTypeException("String passed does not match a valid MeasureType!");
        }

        return result;
    }

    public String getVal() {
        return measureType;
    }
}
