package org.test.core;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StringAccumulator {

    private static final String SPECIAL_CHARS = "\\.[]{}()<>*+-=!?^$";
    private static final String DEFAULT_DELIMITER = ",";
    private static final IntPredicate validateRange = value -> value <= 1000;
    private static final Predicate<Integer> negative = value -> value < 0;

    public int add(String numbers) throws Exception {
        Integer sum = 0;
        List<Integer> intList = null;
        if (null != numbers && !numbers.isEmpty()) {
            StringBuilder pattern = new StringBuilder();
            parseDelimiters(numbers, pattern);
            String delimiter = pattern.length() > 0 ? pattern.toString() : DEFAULT_DELIMITER;
            int startIndex = pattern.length() > 0 ? 1 : 0; //if 1, mean skip first header line which has the delimiter pattern
            String[] arr = numbers.split("\r?\n");
            for (int i = startIndex; i < arr.length; i++) {
                String[] line = arr[i].split(delimiter); //Process each line and sum it

                intList = Arrays.stream(line)
                        .filter(s -> s.matches("-?\\d+")) //check for digit
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                List<Integer> negativeIntegers = intList.stream().filter(negative).collect(Collectors.toList());

                if (!negativeIntegers.isEmpty())
                    throw new IllegalArgumentException("Negative numbers are not allowed! " + negativeIntegers);

                sum += intList.stream().mapToInt(Integer::valueOf).filter(validateRange).sum();
            }
        }
        return sum;
    }

    private void parseDelimiters(final String input, final StringBuilder pattern) {
        String[] arr = input.split("\r?\n");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].startsWith("//") && arr[i].length() > 2) {
                String[] delimiters = arr[i].substring(2).split("\\|");
                if (delimiters.length > 0) pattern.setLength(0);
                for (int x = 0; x < delimiters.length; x++) {
                    String d = delimiters[x];
                    d.chars().forEach(ch -> {
                        if (SPECIAL_CHARS.chars().anyMatch(sc -> sc == ch)) { // escape special characters
                            pattern.append("\\");
                        }
                        pattern.append(Character.toChars(ch));
                    });

                    if (x < delimiters.length - 1)
                        pattern.append("|");
                }

                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        StringAccumulator accumulator = new StringAccumulator();
        System.out.println(accumulator.add("//asd|***|###\n1***2###3\n4\n5asd2000asd10"));
        System.out.println(accumulator.add("//***|###\n1***2###3\n4\n5asd2000asd10"));
        System.out.println(accumulator.add("1,2,3,-4"));
    }
}
