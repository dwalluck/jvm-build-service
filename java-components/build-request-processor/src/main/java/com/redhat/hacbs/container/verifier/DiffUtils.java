package com.redhat.hacbs.container.verifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

/**
 * Diff utilities.
 */
public class DiffUtils {
    public record DiffResults(Set<String> shared, Set<String> added, Set<String> deleted, List<DiffResult<?>> diffResults) {
        @Override
        public Set<String> shared() {
            return Collections.unmodifiableSet(shared);
        }

        @Override
        public Set<String> added() {
            return Collections.unmodifiableSet(added);
        }

        @Override
        public Set<String> deleted() {
            return Collections.unmodifiableSet(deleted);
        }

        @Override
        public List<DiffResult<?>> diffResults() {
            return Collections.unmodifiableList(diffResults);
        }
    }

    public static <T extends Diffable<T>> DiffResults diff(Map<String, T> left, Map<String, T> right) {
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
            System.out.printf("ERROR: Added: %d: %s%n", added.size(), added);
        }

        if (!deleted.isEmpty()) {
            System.out.printf("ERROR: Deleted: %d: %s%n", deleted.size(), deleted);
        }

        var results = new ArrayList<DiffResult<?>>(shared.size());

        for (var s : shared) {
            var l = left.get(s);
            var r = right.get(s);
            var result = l.diff(r);
            results.add(result);

            if (result.getNumberOfDiffs() > 0) {
                System.out.printf("For name %s, there are %d differences%n", s, result.getNumberOfDiffs());

                for (var diff : result.getDiffs()) {
                    System.out.printf("  For field name %s, %s changed to %s%n", diff.getFieldName(), diff.getLeft(),
                            diff.getRight());
                }
            }

        }

        return new DiffResults(shared, added, deleted, results);
    }
}
