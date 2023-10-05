package ru.otus.core.loader;

import java.util.List;
import java.util.stream.Collectors;

public class ParamTable {
    List<String> param;
    public ParamTable(List<String> param){
        this.param = param;
    }

    public String getParamName() {
        return param.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(",", "(", ")"));
    }
    public String getParamСhar() {
        String result = "(";
        for (String item : param){
            result = result + "?,";
        }
        return result.substring(0, result.length() - 1) + ")";
    }
}
