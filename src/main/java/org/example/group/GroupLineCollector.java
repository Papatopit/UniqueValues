package org.example.group;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.UNORDERED;

public class GroupLineCollector implements Collector<String[], Map<String, Map<Integer, Set<String[]>>>, List<Set<String>>> {

    @Override
    public Supplier<Map<String, Map<Integer, Set<String[]>>>> supplier() {
        return () -> new HashMap<>();
    }

    @Override
    public BiConsumer<Map<String, Map<Integer, Set<String[]>>>, String[]> accumulator() {
        return (Map<String, Map<Integer, Set<String[]>>> acc, String[] candidate) -> {

            var tempGroup = new HashSet<String[]>();
            tempGroup.add(candidate);

            for (int i = 0; i < candidate.length; ++i) {
                if (("".equals(candidate[i])) || ("\"\"".equals(candidate[i])))
                    continue;
                if (acc.containsKey(candidate[i])) {
                    if (acc.get(candidate[i]).containsKey(i)) {
                        tempGroup.addAll(acc.get(candidate[i]).get(i));
                    }
                }
            }

            if (tempGroup.size() > 1) {
                //Ищем "существующую группу". Этот указатель понадобится для добавления несуществующих раннее слов.
                Set<String[]> pointerToExistingGroup = new HashSet<>();
                for (int indexWord = 0; indexWord < candidate.length; ++indexWord) {
                    if (acc.containsKey(candidate[indexWord])) {
                        Set<Integer> setOfPositions = acc.get(candidate[indexWord]).keySet();
                        for (int indexWordPosition : setOfPositions) {
                            pointerToExistingGroup = acc.get(candidate[indexWord]).get(indexWordPosition);
                            break;
                        }
                    }
                }

                for (int i = 0; i < candidate.length; ++i) {
                    if (("".equals(candidate[i])) || ("\"\"".equals(candidate[i])))
                        continue;
                    if (acc.containsKey(candidate[i])) {
                        if (acc.get(candidate[i]).containsKey(i))
                            acc.get(candidate[i]).get(i).addAll(tempGroup);
                        else {
                            acc.get(candidate[i]).put(i, pointerToExistingGroup);
                        }
                    } else {
                        HashMap<Integer, Set<String[]>> newMap = new HashMap<>();
                        newMap.put(i, pointerToExistingGroup);
                        acc.put(candidate[i], newMap);
                    }
                }
                return;
            }

            for (int i = 0; i < candidate.length; ++i) {
                if (("".equals(candidate[i])) || ("\"\"".equals(candidate[i])))
                    continue;
                if (acc.containsKey(candidate[i])) {
                    acc.get(candidate[i]).put(i, tempGroup);
                } else {
                    HashMap<Integer, Set<String[]>> newMap = new HashMap<>();
                    newMap.put(i, tempGroup);
                    acc.put(candidate[i], newMap);
                }
            }

        };
    }

    @Override
    public BinaryOperator<Map<String, Map<Integer, Set<String[]>>>> combiner() {
        return (a1, a2) -> {
            throw new UnsupportedOperationException("Параллельность не реализована");
        };
    }


    @Override
    public Function<Map<String, Map<Integer, Set<String[]>>>, List<Set<String>>> finisher() {
        return (Map<String, Map<Integer, Set<String[]>>> inputMap) -> {

            Set processedGroups = new HashSet<Set<String[]>>();
            var result = new ArrayList<Set<String>>();

            for (String keyWord : inputMap.keySet()) {
                for (int keyPosition : inputMap.get(keyWord).keySet()) {
                    if (processedGroups.contains(inputMap.get(keyWord).get(keyPosition)))
                        continue;
                    processedGroups.add(inputMap.get(keyWord).get(keyPosition));
                    var group = new HashSet<String>();
                    for (String[] arrayStrings : inputMap.get(keyWord).get(keyPosition)) {
                        String arrayStringsToSingle = arrayStrings[0];
                        for (int i = 1; i < arrayStrings.length; ++i) {
                            arrayStringsToSingle += ";";
                            arrayStringsToSingle += arrayStrings[i];
                        }
                        group.add(arrayStringsToSingle);
                    }
                    result.add(group);
                }
            }
            return result;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(UNORDERED));
    }
}
