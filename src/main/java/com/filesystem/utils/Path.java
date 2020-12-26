package com.filesystem.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Path {

    private String[] parts = new String[0];

    public Path(String path) {
        if(Pattern.matches("^[a-zA-Z]:\\\\(((?![<>:\"\\/\\\\|?*]).)+((?<![ .])\\\\)?)*$", path)) { // Win pattern
            setParts(path.split("\\\\"));
        } else if (Pattern.matches("^(/[^/ ]*)+/?$", path)) { // linux Pattern
            setParts(path.split("/"));
        }
    }

    public void setParts(String[] parts) {

        List<String> filteredParts = new ArrayList<>();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if(part == null || "".equals(part)) continue;

            filteredParts.add(part);
        }

        this.parts = filteredParts.toArray(new String[filteredParts.size()]);
    }

    public String[] getParts() {
        return parts;
    }

    public String getLast() {
        return parts[this.parts.length - 1];
    }
}
