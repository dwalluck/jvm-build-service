package com.redhat.hacbs.container.verifier.asm;

import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Diff utilities.
 */
public class DiffUtils {
    public static <T extends Diffable<T>> Triple<Set<String>, Set<String>, Set<String>> diff(Map<String, T> left, Map<String, T> right) {
        var shared = new LinkedHashSet<String>();
        var deleted = new LinkedHashSet<String>();
        var added = new LinkedHashSet<String>();

        for (var key : left.keySet()) {
            if (right.containsKey(key)) {
                shared.add(key);
            } else {
                deleted.add(key);
            }
        }

        for (var key : right.keySet()) {
            if (!right.containsKey(key)) {
                added.add(key);
            }
        }

        if (!added.isEmpty()) {
            System.out.printf("Added: %d: %s%n", added.size(), added);
        }

        if (!deleted.isEmpty()) {
            System.out.printf("Deleted: %d: %s%n", deleted.size(), deleted);
        }

        if (!shared.isEmpty()) {
            System.out.printf("Shared: %d: %s%n", shared.size(), shared);
        }

        for (var s : shared) {
            var l = left.get(s);
            var r = right.get(s);
            var result = l.diff(r);

            if (result.getNumberOfDiffs() > 0) {
                System.out.printf("For %s, %s%n", s, result);

                for (var diff : result.getDiffs()) {
                    System.out.printf("  %s%n", diff);
                }
            }

        }

        return new ImmutableTriple<>(shared, added, deleted);
    }
}
