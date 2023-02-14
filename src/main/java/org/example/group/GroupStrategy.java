package org.example.group;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public abstract class GroupStrategy implements Collector<String[], Map<String, Map<Integer, Set<String[]>>>, List<Set<String>>>{
    public Supplier<Map<String, Map<Integer, Set<String[]>>>> supplier() {
        return null;
    }

    public BiConsumer<Map<String, Map<Integer, Set<String[]>>>, String[]> accumulator() {
        return null;
    }

    public BinaryOperator<Map<String, Map<Integer, Set<String[]>>>> combiner() {
        return null;
    }

    public Function<Map<String, Map<Integer, Set<String[]>>>, List<Set<String>>> finisher() {
        return null;
    }

    public Set<Collector.Characteristics> characteristics() {
        return Collections.emptySet();
    }

}
