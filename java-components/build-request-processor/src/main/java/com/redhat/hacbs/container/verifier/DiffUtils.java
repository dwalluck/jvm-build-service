package com.redhat.hacbs.container.verifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

/**
 * Diff utils.
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
        LinkedHashSet<String> added;
        left.keySet().forEach(key -> {
            if (right.containsKey(key)) {
                shared.add(key);
            } else {
                deleted.add(key);
            }
        });
        added = right.keySet().stream().filter(key -> !right.containsKey(key))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (!added.isEmpty()) {
            System.out.printf("%d classes were added: %s%n", added.size(), added);
        }

        if (!deleted.isEmpty()) {
            System.out.printf("%d classes were removed: %s%n", deleted.size(), deleted);
        }

        var results = new ArrayList<DiffResult<?>>(shared.size());

        shared.forEach(s -> {
            var l = left.get(s);
            var r = right.get(s);
            var result = l.diff(r);
            results.add(result);

            if (result.getNumberOfDiffs() > 0) {
                System.out.printf("Class %s has %d differences%n", s, result.getNumberOfDiffs());
                var diffs = result.getDiffs();
                for (var diff : diffs) {
                    System.out.printf("  %s: %s changed to %s%n", diff.getFieldName(), diff.getLeft(), diff.getRight());
                }
            }
        });
        return new DiffResults(shared, added, deleted, results);
    }
}
