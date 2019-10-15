package com.github.nbsllc;

import org.json.JSONObject;

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
            String[] nodes = currentPath.split("\\.");

            JSONObject parent = root;
            for (String node : nodes) {
                String[] pair = node.split(":", 2);
                String key = pair[0];

                if (parent.has(key)) {
                    parent = parent.getJSONObject(key);
                } else {
                    JSONObject child = new JSONObject();
                    if (pair.length > 1) {
                        parent.put(key, parseValue(pair[1]));
                    } else {
                        parent.put(key, child);
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
        Pattern integerPattern = Pattern.compile("Integer\\((.+)\\)");
        Matcher matcher = integerPattern.matcher(value);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(1));
        }

        return value;
    }
}
