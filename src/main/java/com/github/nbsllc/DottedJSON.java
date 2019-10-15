package com.github.nbsllc;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DottedJSON {
    private List<String> paths;

    public DottedJSON(List<String> paths) {
        this.paths = paths;
    }

    public JSONObject toJSONObject() {
        JSONObject root = new JSONObject();
        for (String currentPath : paths) {
            String[] pair = currentPath.split(":", 2);
            String[] nodes = pair[0].split("\\.");

            JSONObject parent = root;
            for (int i = 0; i < nodes.length; i++) {
                String node = nodes[i];
                if (parent.has(node)) {
                    parent = parent.getJSONObject(node);
                } else {
                    JSONObject child = new JSONObject();
                    if (i == nodes.length - 1) {
                        parent.put(node, parseValue(pair[1]));
                    } else {
                        parent.put(node, child);
                    }
                    parent = child;
                }
            }
        }

        return root;
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    private Object parseValue(String value) {
        Pattern dataTypePattern = Pattern.compile("^([^(]+)\\(([^)]+)\\)$");
        Matcher matcher = dataTypePattern.matcher(value);
        if (matcher.find()) {
            try {
                Class<?> dataType = Class.forName("java.lang." + matcher.group(1));
                Method valueOf = dataType.getMethod("valueOf", String.class);
                return valueOf.invoke(null, matcher.group(2));
            } catch (Exception e) {
                String error = String.format("Unable to parse data type: %s.", value);
                throw new RuntimeException(error, e);
            }
        }

        return value;
    }
}
