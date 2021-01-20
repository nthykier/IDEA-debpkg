package com.github.nthykier.debpkg.deb822;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Supplier;

public class Deb822DataSets {

    private static final Map<String, List<String>> dataListCache = new ConcurrentSkipListMap<>();
    private static final Map<String, Supplier<List<String>>> virtualDataLists;

    public static @NotNull List<@NotNull String> getDataList(@NotNull String name) {
        return dataListCache.computeIfAbsent(name, Deb822DataSets::loadDataList);
    }

    private static @NotNull List<@NotNull String> loadDataList(@NotNull String name) {
        Supplier<List<String>> virtualDataSet = virtualDataLists.get(name);
        if (virtualDataSet == null) {
            ArrayList<String> dataset = new ArrayList<>();
            try (BufferedReader reader = openResource(name + ".list")) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals("") || line.startsWith("#")) {
                        continue;
                    }
                    dataset.add(line.trim());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not read dataset " + name, e);
            }

            dataset.trimToSize();
            return Collections.unmodifiableList(dataset);
        }
        return virtualDataSet.get();
    }

    private static @NotNull BufferedReader openResource(String resource) {
        InputStream in = Deb822DataSets.class.getResourceAsStream(resource);
        if (in == null) {
            throw new IllegalArgumentException("Cannot find resource " + resource);
        }
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    private static List<String> virtualArchiveSectionsWithComponents() {
        List<String> sections = getDataList("archive-sections");
        List<String> components = getDataList("components");
        ArrayList<String> combined = new ArrayList<>((sections.size() + 1) * components.size());
        combined.addAll(sections);
        for (String component : components) {
            String prefix = component + "/";
            for (String section : sections) {
                combined.add(prefix + section);
            }
        }
        combined.trimToSize();
        return Collections.unmodifiableList(combined);
    }

    static {
        virtualDataLists = Collections.unmodifiableMap(new HashMap<String, Supplier<List<String>>>(){{
            put("virtual/archive-sections-with-components", Deb822DataSets::virtualArchiveSectionsWithComponents);
        }});
    }
}
