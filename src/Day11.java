import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public class Day11 {
    public static void main(String... args) {
        partI(testInput);
        partI(realInput);

        partII(testInput);
        partII(realInput);
    }

    static record Operation(String n, ToLongFunction<Long> f) {
    }

    static class Monkey {
        Integer number;
        List<Long> startingItems;
        Operation operation;
        Integer divisor;
        Integer trueTarget;
        Integer falseTarget;

        @Override
        public String toString() {
            return List.of("Number", number, "Starting items", startingItems, "Operation", operation.n(),
                    "Divisor", divisor, "True target", trueTarget, "False target", falseTarget).toString();
        }

        public void validate() {
            if (number == null || startingItems == null || operation == null ||
                divisor == null || trueTarget == null || falseTarget == null) {

                throw new IllegalStateException();
            }
        }
    }

    private static void partI(String input) {
        List<Monkey> monkeys = parseMonkeys(input);
        Map<Monkey, Integer> inspections = new HashMap<>();

        for (int round = 0; round < 20; round++) {
            for (Monkey m : monkeys) {
                while (!m.startingItems.isEmpty()) {
                    inspections.merge(m, 1, Integer::sum);
                    Long item = m.operation.f.applyAsLong(m.startingItems.remove(0)) / 3;
                    boolean test = item % m.divisor == 0;
                    monkeys.get(test ? m.trueTarget : m.falseTarget).startingItems.add(item);
                }
            }

            System.out.println();
            System.out.println("After round " + (round + 1));

            for (Monkey m : monkeys) {
                System.out.println("Monkey " + m.number + ": " + m.startingItems);
            }
        }

        printInspectionsAndResult(monkeys, inspections);
    }

    private static void partII(String input) {
        List<Monkey> monkeys = parseMonkeys(input);

        int smallishCommonDivisor = monkeys.stream()
                                           .mapToInt(m -> m.divisor)
                                           .reduce(1, Math::multiplyExact);

        System.out.println("Modulo " + smallishCommonDivisor + " after each round");

        Map<Monkey, Integer> inspections = new HashMap<>();

        for (int round = 0; round < 10_000; round++) {
            for (Monkey m : monkeys) {
                while (!m.startingItems.isEmpty()) {
                    inspections.merge(m, 1, Integer::sum);
                    Long item = m.operation.f.applyAsLong(m.startingItems.remove(0));
                    boolean test = item % m.divisor == 0;
                    monkeys.get(test ? m.trueTarget : m.falseTarget).startingItems.add(item);
                }
            }

            for (Monkey m : monkeys) {
                m.startingItems = m.startingItems.stream().map(i -> i % smallishCommonDivisor).collect(Collectors.toList());
            }
        }

        printInspectionsAndResult(monkeys, inspections);
    }

    private static List<Monkey> parseMonkeys(String input) {
        List<Monkey> monkeys = new ArrayList<>();

        for (String singleMonkeyData : input.split("\n\n")) {
            Monkey monkey = new Monkey();

            singleMonkeyData.lines()
                            .map(String::trim)
                            .forEach(line -> {
                                if (line.startsWith("Monkey ")) {
                                    monkey.number = Integer.parseInt(line.substring("Monkey ".length()).replace(":", ""));
                                } else if (line.startsWith("Starting items: ")) {
                                    monkey.startingItems = Arrays.stream(line.substring("Starting items: ".length()).split(","))
                                                                 .map(part -> Long.parseLong(part.trim()))
                                                                 .collect(Collectors.toList());
                                } else if (line.startsWith("Operation: ")) {
                                    String op = line.substring("Operation: ".length());

                                    if (op.equals("new = old * old")) {
                                        monkey.operation = new Operation("new = old * old", x -> Math.multiplyExact(x, x));
                                    } else if (op.startsWith("new = old + ")) {
                                        int term = Integer.parseInt(op.substring("new = old + ".length()));
                                        monkey.operation = new Operation("new = old + " + term, x -> Math.addExact(x, term));
                                    } else if (op.startsWith("new = old * ")) {
                                        int factor = Integer.parseInt(op.substring("new = old * ".length()));
                                        monkey.operation = new Operation("new = old * " + factor,
                                                x -> Math.multiplyExact(x, factor));
                                    }
                                } else if (line.startsWith("Test: divisible by ")) {
                                    monkey.divisor = Integer.parseInt(line.substring("Test: divisible by ".length()));
                                } else if (line.startsWith("If true: throw to monkey ")) {
                                    monkey.trueTarget = Integer.parseInt(line.substring("If true: throw to monkey ".length()));
                                } else if (line.startsWith("If false: throw to monkey ")) {
                                    monkey.falseTarget = Integer.parseInt(line.substring("If false: throw to monkey ".length()));
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            });

            monkey.validate();
            monkeys.add(monkey);
        }

        System.out.println("-".repeat(120));
        monkeys.forEach(System.out::println);
        return monkeys;
    }

    private static void printInspectionsAndResult(List<Monkey> monkeys, Map<Monkey, Integer> inspections) {
        System.out.println();

        for (Monkey m : monkeys) {
            System.out.println("Monkey " + m.number + " inspected items " + inspections.getOrDefault(m, 0) + " times.");
        }

        long result = inspections.values().stream()
                                 .sorted(Comparator.comparingInt((Integer x) -> x).reversed())
                                 .mapToLong(x -> x)
                                 .limit(2)
                                 .reduce(Math::multiplyExact)
                                 .orElse(0);

        System.out.println("Result: " + result);
    }

    private static final String testInput = """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
                        
            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0
                        
            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3
                        
            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1""";

    private static final String realInput = """
            Monkey 0:
              Starting items: 61
              Operation: new = old * 11
              Test: divisible by 5
                If true: throw to monkey 7
                If false: throw to monkey 4
                        
            Monkey 1:
              Starting items: 76, 92, 53, 93, 79, 86, 81
              Operation: new = old + 4
              Test: divisible by 2
                If true: throw to monkey 2
                If false: throw to monkey 6
                        
            Monkey 2:
              Starting items: 91, 99
              Operation: new = old * 19
              Test: divisible by 13
                If true: throw to monkey 5
                If false: throw to monkey 0
                        
            Monkey 3:
              Starting items: 58, 67, 66
              Operation: new = old * old
              Test: divisible by 7
                If true: throw to monkey 6
                If false: throw to monkey 1
                        
            Monkey 4:
              Starting items: 94, 54, 62, 73
              Operation: new = old + 1
              Test: divisible by 19
                If true: throw to monkey 3
                If false: throw to monkey 7
                        
            Monkey 5:
              Starting items: 59, 95, 51, 58, 58
              Operation: new = old + 3
              Test: divisible by 11
                If true: throw to monkey 0
                If false: throw to monkey 4
                        
            Monkey 6:
              Starting items: 87, 69, 92, 56, 91, 93, 88, 73
              Operation: new = old + 8
              Test: divisible by 3
                If true: throw to monkey 5
                If false: throw to monkey 2
                        
            Monkey 7:
              Starting items: 71, 57, 86, 67, 96, 95
              Operation: new = old + 7
              Test: divisible by 17
                If true: throw to monkey 3
                If false: throw to monkey 1""";
}
