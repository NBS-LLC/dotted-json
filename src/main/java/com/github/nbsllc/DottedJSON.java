package com.github.nbsllc;

import org.json.JSONObject;

import java.util.List;

/**
 * Convert dotted strings into JSON.
 */
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
                        parent.put(key, pair[1]);
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
}
