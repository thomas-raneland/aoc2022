import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Day5 {
    public static void main(String... args) {
        String[] lines = input.split("\n");
        a(lines);
        b(lines);
    }

    private static void a(String[] lines) {
        List<Stack<Character>> stacks = readStacks(lines);
        printStacks(stacks);

        for (String line : lines) {
            if (line.startsWith("move")) {
                String[] tokens = line.split(" ");
                int count = Integer.parseInt(tokens[1]);
                int from = Integer.parseInt(tokens[3]);
                int to = Integer.parseInt(tokens[5]);

                for (int i = 0; i < count; i++) {
                    stacks.get(to - 1).push(stacks.get(from - 1).pop());
                }
            }
        }

        printStacks(stacks);
        printTopElements(stacks);
    }

    private static void b(String[] lines) {
        List<Stack<Character>> stacks = readStacks(lines);
        printStacks(stacks);

        for (String line : lines) {
            if (line.startsWith("move")) {
                String[] tokens = line.split(" ");
                int count = Integer.parseInt(tokens[1]);
                int from = Integer.parseInt(tokens[3]);
                int to = Integer.parseInt(tokens[5]);
                System.out.println("move " + count + " from " + from + " to " + to);

                Stack<Character> inbetween = new Stack<>();

                for (int i = 0; i < count; i++) {
                    inbetween.push(stacks.get(from - 1).pop());
                }

                while (!inbetween.isEmpty()) {
                    stacks.get(to - 1).push(inbetween.pop());
                }

                printStacks(stacks);
            }
        }

        printStacks(stacks);
        printTopElements(stacks);
    }

    private static void printTopElements(List<Stack<Character>> stacks) {
        System.out.print("Top elements: ");

        for (Stack<Character> s : stacks) {
            if (!s.isEmpty()) {
                System.out.print(s.peek());
            }
        }

        System.out.println();
    }

    private static List<Stack<Character>> readStacks(String[] lines) {
        List<Stack<Character>> stacks = new ArrayList<>();

        Map<Integer, Stack<Character>> reverseStacks = new HashMap<>();
        int nbrOfStacks = 0;

        for (String line : lines) {
            if (line.trim().startsWith("1")) {
                nbrOfStacks = Arrays.stream(line.split(" ")).filter(n -> !n.isBlank()).mapToInt(Integer::parseInt).max()
                                    .orElseThrow();
                break;
            }

            for (int i = 0; i < line.length(); i += 4) {
                if (line.charAt(i) == '[') {
                    reverseStacks.computeIfAbsent(i / 4, k -> new Stack<>()).push(line.charAt(i + 1));
                }
            }
        }

        for (int i = 0; i < nbrOfStacks; i++) {
            Stack<Character> stack = new Stack<>();
            Stack<Character> reverseStack = reverseStacks.getOrDefault(i, new Stack<>());
            while (!reverseStack.isEmpty()) {
                stack.push(reverseStack.pop());
            }
            stacks.add(stack);
        }
        return stacks;
    }

    private static void printStacks(List<Stack<Character>> stacks) {
        for (Stack<Character> s : stacks) {
            for (int i = 0; i < s.size(); i++) {
                System.out.print(s.get(s.size() - i - 1));
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    @SuppressWarnings("TrailingWhitespacesInTextBlock")
    private static final String testInput = """
                [D]   
            [N] [C]   
            [Z] [M] [P]
             1   2   3
                        
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2""";

    private static final String input = """            
            [S]                 [T] [Q]
            [L]             [B] [M] [P]     [T]
            [F]     [S]     [Z] [N] [S]     [R]
            [Z] [R] [N]     [R] [D] [F]     [V]
            [D] [Z] [H] [J] [W] [G] [W]     [G]
            [B] [M] [C] [F] [H] [Z] [N] [R] [L]
            [R] [B] [L] [C] [G] [J] [L] [Z] [C]
            [H] [T] [Z] [S] [P] [V] [G] [M] [M]
             1   2   3   4   5   6   7   8   9
                        
            move 6 from 1 to 7
            move 2 from 2 to 4
            move 2 from 7 to 4
            move 6 from 4 to 3
            move 1 from 5 to 1
            move 3 from 8 to 3
            move 15 from 3 to 4
            move 6 from 5 to 9
            move 14 from 4 to 2
            move 3 from 2 to 7
            move 1 from 2 to 7
            move 9 from 9 to 1
            move 3 from 2 to 1
            move 7 from 6 to 7
            move 1 from 6 to 8
            move 2 from 9 to 1
            move 9 from 2 to 3
            move 8 from 3 to 9
            move 1 from 1 to 4
            move 1 from 8 to 6
            move 1 from 6 to 2
            move 5 from 9 to 8
            move 2 from 9 to 1
            move 1 from 4 to 2
            move 17 from 1 to 9
            move 1 from 3 to 1
            move 3 from 2 to 3
            move 2 from 4 to 5
            move 12 from 7 to 3
            move 16 from 9 to 2
            move 5 from 7 to 5
            move 2 from 1 to 2
            move 1 from 3 to 6
            move 1 from 4 to 6
            move 1 from 7 to 3
            move 1 from 6 to 3
            move 7 from 3 to 4
            move 5 from 8 to 3
            move 1 from 6 to 7
            move 7 from 3 to 4
            move 6 from 3 to 1
            move 2 from 4 to 8
            move 1 from 5 to 2
            move 10 from 4 to 5
            move 3 from 5 to 2
            move 2 from 8 to 9
            move 5 from 2 to 8
            move 1 from 3 to 5
            move 2 from 5 to 8
            move 12 from 5 to 7
            move 1 from 4 to 2
            move 5 from 9 to 4
            move 1 from 2 to 5
            move 6 from 1 to 3
            move 6 from 3 to 5
            move 10 from 7 to 4
            move 2 from 7 to 3
            move 4 from 7 to 6
            move 1 from 9 to 5
            move 12 from 2 to 1
            move 1 from 8 to 7
            move 3 from 7 to 4
            move 4 from 4 to 8
            move 7 from 5 to 3
            move 1 from 2 to 4
            move 10 from 1 to 5
            move 2 from 1 to 2
            move 4 from 6 to 7
            move 8 from 8 to 3
            move 5 from 4 to 9
            move 12 from 3 to 8
            move 4 from 3 to 8
            move 2 from 9 to 2
            move 3 from 5 to 4
            move 1 from 3 to 5
            move 1 from 7 to 6
            move 14 from 4 to 6
            move 6 from 5 to 9
            move 8 from 2 to 8
            move 3 from 5 to 7
            move 21 from 8 to 4
            move 16 from 4 to 9
            move 8 from 6 to 2
            move 4 from 6 to 1
            move 1 from 4 to 6
            move 2 from 4 to 8
            move 3 from 1 to 8
            move 2 from 4 to 6
            move 1 from 6 to 2
            move 3 from 8 to 4
            move 2 from 2 to 5
            move 2 from 5 to 7
            move 1 from 8 to 9
            move 1 from 4 to 9
            move 1 from 1 to 6
            move 3 from 6 to 3
            move 3 from 2 to 3
            move 1 from 4 to 6
            move 3 from 6 to 7
            move 10 from 9 to 7
            move 1 from 4 to 7
            move 6 from 8 to 3
            move 1 from 6 to 8
            move 2 from 2 to 5
            move 1 from 2 to 1
            move 1 from 8 to 9
            move 1 from 2 to 8
            move 1 from 1 to 9
            move 7 from 9 to 1
            move 1 from 8 to 5
            move 7 from 1 to 7
            move 3 from 5 to 8
            move 3 from 7 to 2
            move 1 from 8 to 4
            move 1 from 2 to 4
            move 2 from 4 to 6
            move 5 from 3 to 1
            move 9 from 7 to 2
            move 6 from 3 to 8
            move 8 from 2 to 7
            move 2 from 6 to 4
            move 2 from 1 to 7
            move 2 from 1 to 4
            move 24 from 7 to 4
            move 4 from 8 to 9
            move 2 from 7 to 5
            move 1 from 5 to 2
            move 1 from 3 to 8
            move 4 from 2 to 8
            move 13 from 9 to 2
            move 2 from 8 to 6
            move 3 from 9 to 6
            move 26 from 4 to 2
            move 1 from 5 to 7
            move 2 from 6 to 2
            move 2 from 4 to 1
            move 7 from 2 to 1
            move 15 from 2 to 6
            move 8 from 2 to 8
            move 4 from 6 to 8
            move 9 from 2 to 9
            move 13 from 6 to 7
            move 6 from 1 to 9
            move 2 from 2 to 4
            move 4 from 1 to 6
            move 3 from 8 to 3
            move 1 from 4 to 9
            move 2 from 6 to 7
            move 1 from 4 to 3
            move 3 from 3 to 2
            move 14 from 7 to 4
            move 5 from 9 to 5
            move 9 from 8 to 5
            move 7 from 9 to 6
            move 2 from 5 to 6
            move 2 from 9 to 2
            move 10 from 5 to 1
            move 1 from 3 to 1
            move 2 from 8 to 1
            move 1 from 9 to 2
            move 1 from 7 to 5
            move 4 from 2 to 1
            move 1 from 9 to 8
            move 3 from 4 to 1
            move 1 from 8 to 6
            move 12 from 1 to 5
            move 1 from 1 to 6
            move 1 from 7 to 5
            move 4 from 6 to 9
            move 2 from 2 to 4
            move 1 from 9 to 6
            move 1 from 1 to 5
            move 2 from 9 to 7
            move 10 from 6 to 5
            move 1 from 6 to 7
            move 20 from 5 to 1
            move 1 from 7 to 9
            move 2 from 9 to 1
            move 3 from 5 to 1
            move 2 from 8 to 4
            move 2 from 8 to 7
            move 1 from 5 to 9
            move 1 from 8 to 4
            move 22 from 1 to 7
            move 5 from 4 to 8
            move 1 from 5 to 9
            move 19 from 7 to 4
            move 2 from 9 to 1
            move 1 from 5 to 9
            move 10 from 1 to 8
            move 1 from 9 to 1
            move 1 from 8 to 3
            move 8 from 4 to 7
            move 1 from 5 to 6
            move 3 from 4 to 5
            move 1 from 5 to 9
            move 11 from 7 to 4
            move 4 from 4 to 9
            move 1 from 6 to 2
            move 1 from 3 to 9
            move 5 from 9 to 4
            move 5 from 7 to 9
            move 23 from 4 to 2
            move 17 from 2 to 7
            move 2 from 2 to 8
            move 4 from 4 to 7
            move 1 from 4 to 5
            move 2 from 5 to 2
            move 5 from 8 to 9
            move 5 from 2 to 7
            move 9 from 7 to 5
            move 11 from 9 to 2
            move 1 from 4 to 3
            move 5 from 8 to 7
            move 3 from 8 to 5
            move 2 from 1 to 3
            move 2 from 3 to 9
            move 1 from 5 to 8
            move 5 from 7 to 5
            move 15 from 5 to 4
            move 2 from 8 to 1
            move 2 from 5 to 1
            move 4 from 4 to 1
            move 1 from 8 to 7
            move 8 from 2 to 1
            move 4 from 2 to 8
            move 2 from 7 to 4
            move 5 from 8 to 6
            move 5 from 7 to 9
            move 4 from 6 to 5
            move 7 from 4 to 8
            move 1 from 6 to 1
            move 1 from 3 to 1
            move 2 from 5 to 1
            move 7 from 1 to 5
            move 5 from 1 to 3
            move 4 from 7 to 9
            move 4 from 3 to 9
            move 2 from 9 to 7
            move 6 from 9 to 2
            move 1 from 4 to 1
            move 1 from 3 to 5
            move 1 from 2 to 5
            move 5 from 9 to 4
            move 4 from 4 to 6
            move 1 from 8 to 9
            move 8 from 4 to 3
            move 7 from 7 to 3
            move 5 from 1 to 3
            move 11 from 5 to 9
            move 1 from 7 to 6
            move 2 from 3 to 5
            move 1 from 3 to 1
            move 3 from 6 to 2
            move 2 from 5 to 1
            move 2 from 1 to 2
            move 3 from 1 to 5
            move 5 from 9 to 2
            move 2 from 6 to 8
            move 2 from 3 to 8
            move 4 from 9 to 7
            move 3 from 5 to 2
            move 2 from 1 to 8
            move 1 from 9 to 8
            move 1 from 9 to 2
            move 4 from 7 to 9
            move 11 from 8 to 7
            move 1 from 8 to 2
            move 6 from 9 to 7
            move 3 from 7 to 1
            move 13 from 2 to 7
            move 24 from 7 to 1
            move 2 from 2 to 6
            move 1 from 8 to 3
            move 1 from 9 to 3
            move 5 from 2 to 4
            move 1 from 2 to 5
            move 1 from 6 to 2
            move 1 from 6 to 3
            move 1 from 2 to 4
            move 3 from 7 to 3
            move 2 from 1 to 7
            move 2 from 3 to 8
            move 2 from 7 to 8
            move 9 from 3 to 2
            move 3 from 4 to 8
            move 1 from 5 to 1
            move 9 from 2 to 1
            move 3 from 4 to 9
            move 1 from 7 to 8
            move 6 from 3 to 9
            move 2 from 1 to 5
            move 15 from 1 to 3
            move 13 from 3 to 9
            move 11 from 1 to 4
            move 5 from 4 to 1
            move 6 from 3 to 6
            move 4 from 4 to 8
            move 6 from 1 to 4
            move 1 from 5 to 2
            move 1 from 2 to 1
            move 3 from 4 to 2
            move 2 from 8 to 5
            move 2 from 4 to 2
            move 9 from 9 to 3
            move 9 from 3 to 5
            move 2 from 9 to 4
            move 5 from 2 to 6
            move 1 from 1 to 8
            move 1 from 4 to 1
            move 10 from 9 to 2
            move 9 from 2 to 4
            move 10 from 4 to 1
            move 3 from 1 to 3
            move 4 from 1 to 2
            move 5 from 2 to 4
            move 2 from 5 to 2
            move 4 from 1 to 7
            move 10 from 5 to 4
            move 2 from 2 to 4
            move 1 from 9 to 2
            move 2 from 3 to 5
            move 1 from 3 to 5
            move 3 from 6 to 7
            move 8 from 4 to 9
            move 6 from 6 to 1
            move 4 from 9 to 5
            move 2 from 9 to 1
            move 1 from 2 to 6
            move 6 from 5 to 2
            move 3 from 7 to 9
            move 4 from 8 to 2
            move 1 from 7 to 9
            move 1 from 5 to 3
            move 2 from 7 to 4
            move 1 from 7 to 1
            move 14 from 1 to 9
            move 1 from 1 to 9
            move 1 from 3 to 8
            move 3 from 2 to 5
            move 2 from 4 to 2
            move 6 from 8 to 1
            move 1 from 2 to 1
            move 5 from 1 to 9
            move 1 from 1 to 7
            move 2 from 8 to 5
            move 1 from 5 to 4
            move 1 from 6 to 1
            move 8 from 2 to 7
            move 2 from 6 to 1
            move 9 from 9 to 5
            move 11 from 4 to 8
            move 4 from 7 to 4
            move 6 from 4 to 6
            move 1 from 7 to 4
            move 6 from 6 to 7
            move 1 from 5 to 9
            move 6 from 8 to 9
            move 8 from 9 to 5
            move 1 from 4 to 5
            move 15 from 9 to 3
            move 3 from 1 to 4
            move 6 from 7 to 2
            move 3 from 4 to 9
            move 2 from 7 to 3
            move 1 from 7 to 3
            move 1 from 7 to 2
            move 2 from 8 to 1
            move 3 from 8 to 5
            move 2 from 1 to 7
            move 8 from 3 to 6
            move 3 from 6 to 5
            move 1 from 6 to 1
            move 10 from 5 to 7
            move 6 from 5 to 4
            move 4 from 2 to 4
            move 6 from 5 to 1
            move 6 from 1 to 8
            move 2 from 9 to 2
            move 2 from 9 to 7
            move 6 from 3 to 7
            move 1 from 3 to 5
            move 1 from 1 to 9
            move 2 from 8 to 1
            move 2 from 5 to 4
            move 3 from 3 to 7
            move 10 from 4 to 6
            move 1 from 9 to 7
            move 12 from 7 to 3
            move 12 from 3 to 8
            move 2 from 1 to 5
            move 1 from 1 to 3
            move 13 from 8 to 1
            move 7 from 7 to 1
            move 13 from 6 to 9
            move 1 from 7 to 4
            move 6 from 5 to 3
            move 3 from 4 to 3
            move 6 from 3 to 1
            move 10 from 9 to 4
            move 2 from 7 to 6
            move 8 from 1 to 9
            move 3 from 2 to 9
            move 1 from 3 to 5
            move 1 from 3 to 5
            move 1 from 1 to 4
            move 6 from 9 to 3
            move 2 from 6 to 7
            move 4 from 9 to 5
            move 4 from 1 to 6
            move 1 from 2 to 4
            move 6 from 1 to 4
            move 3 from 9 to 3
            move 3 from 6 to 8
            move 3 from 8 to 7
            move 5 from 5 to 1
            move 1 from 3 to 9
            move 1 from 9 to 5
            move 1 from 3 to 2
            move 2 from 5 to 1
            move 1 from 6 to 9
            move 1 from 6 to 3
            move 2 from 9 to 7
            move 2 from 8 to 1
            move 1 from 3 to 2
            move 1 from 2 to 5
            move 1 from 7 to 1
            move 7 from 7 to 9
            move 12 from 1 to 9
            move 1 from 5 to 2
            move 1 from 7 to 1
            move 13 from 4 to 7
            move 1 from 9 to 4
            move 5 from 7 to 3
            move 4 from 9 to 1
            move 8 from 7 to 9
            move 3 from 2 to 3
            move 4 from 3 to 7
            move 5 from 4 to 6
            move 3 from 9 to 4
            move 10 from 1 to 5
            move 3 from 4 to 7
            move 16 from 9 to 2
            move 3 from 9 to 2
            move 6 from 5 to 3
            move 4 from 6 to 2
            move 1 from 4 to 6
            move 2 from 6 to 8
            move 1 from 5 to 2
            move 1 from 5 to 8
            move 7 from 7 to 2
            move 16 from 2 to 1
            move 1 from 5 to 1
            move 10 from 2 to 8
            move 14 from 8 to 5
            move 2 from 2 to 6
            move 1 from 2 to 5
            move 2 from 2 to 1
            move 8 from 1 to 7
            move 4 from 1 to 7
            move 2 from 1 to 7
            move 5 from 3 to 2
            move 1 from 1 to 6
            move 2 from 2 to 5
            move 4 from 1 to 7
            move 1 from 2 to 8
            move 1 from 2 to 8
            move 3 from 6 to 7
            move 10 from 7 to 5
            move 1 from 2 to 8
            move 27 from 5 to 9
            move 1 from 5 to 6
            move 1 from 6 to 4
            move 1 from 4 to 3
            move 3 from 3 to 7
            move 4 from 3 to 6
            move 2 from 6 to 4
            move 3 from 8 to 1
            move 2 from 6 to 1
            move 12 from 7 to 8
            move 2 from 3 to 9
            move 1 from 9 to 2
            move 1 from 2 to 8
            move 2 from 1 to 2
            move 6 from 3 to 8
            move 1 from 7 to 4
            move 15 from 9 to 5
            move 7 from 9 to 4
            move 1 from 2 to 1
            move 16 from 8 to 2
            move 8 from 5 to 2
            move 24 from 2 to 9
            move 3 from 1 to 2
            move 24 from 9 to 1
            move 5 from 5 to 9
            move 3 from 4 to 1
            move 1 from 7 to 6
            move 1 from 6 to 3
            move 1 from 3 to 2
            move 3 from 2 to 3
            move 1 from 5 to 6
            move 1 from 2 to 7""";
}
