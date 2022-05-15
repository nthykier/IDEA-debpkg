package com.github.nthykier.debpkg.deb822;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Supplier;

public class Deb822DataSets {

    private static final Map<String, Set<String>> dataListCache = new ConcurrentSkipListMap<>();
    private static final Map<String, Supplier<Set<String>>> virtualDataLists;

    public static @NotNull Set<@NotNull String> getDataSet(@NotNull String name) {
        return dataListCache.computeIfAbsent(name, Deb822DataSets::loadDataSet);
    }

    private static @NotNull Set<@NotNull String> loadDataSet(@NotNull String name) {
        Supplier<Set<String>> virtualDataSet = virtualDataLists.get(name);
        if (virtualDataSet == null) {
            Set<String> dataset = new LinkedHashSet<>();
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
            return Collections.unmodifiableSet(dataset);
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

    private static Set<String> virtualArchiveSectionsWithComponents() {
        Set<String> sections = getDataSet("archive-sections");
        Set<String> components = getDataSet("components");
        Set<String> combined = new LinkedHashSet<>((sections.size() + 1) * components.size());
        combined.addAll(sections);
        for (String component : components) {
            String prefix = component + "/";
            for (String section : sections) {
                combined.add(prefix + section);
            }
        }
        return Collections.unmodifiableSet(combined);
    }

    static {
        virtualDataLists = Collections.unmodifiableMap(new HashMap<String, Supplier<Set<String>>>(){{
            put("virtual/archive-sections-with-components", Deb822DataSets::virtualArchiveSectionsWithComponents);
        }});
    }
}
