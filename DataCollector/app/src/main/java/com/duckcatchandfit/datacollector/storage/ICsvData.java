package com.duckcatchandfit.datacollector.storage;

public interface ICsvData {

    String toCsvHeader(String colSeparator);

    String toCsvRow(String colSeparator);

}
