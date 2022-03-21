package com.duckcatchandfit.datacollector;

public interface ICsvData {

    String toCsvHeader(String colSeparator);

    String toCsvRow(String colSeparator);

}
