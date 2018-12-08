package ru.naumen.sd40.log.parser.parseMods;

import java.util.List;
import java.util.Map;

public interface DataType {
    List<String> getTypeProperties();
    Map<String, String> fullNameMap();
    Map<String, String> unitsMap();
    String getDisplayName();
}
