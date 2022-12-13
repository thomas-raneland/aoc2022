import java.io.StringWriter;

public class Day10 {
    public static void main(String... args) {
        a(testInput);
        a(input);
        b(testInput);
        b(input);
    }

    private static void a(String in) {
        int cycle = 0;
        int x = 1;
        int total = 0;

        for (String line : in.lines().toList()) {
            if (line.equals("noop")) {
                total += checkCycle(++cycle, x);
            } else if (line.startsWith("addx ")) {
                total += checkCycle(++cycle, x);
                total += checkCycle(++cycle, x);
                x += Integer.parseInt(line.substring(5));
            }
        }

        System.out.println("total: " + total);
    }

    private static int checkCycle(int cycle, int x) {
        if ((cycle - 20) % 40 == 0) {
            System.out.println("cycle " + cycle + " x = " + x + " => " + cycle * x);
            return cycle * x;
        }

        return 0;
    }

    private static void b(String in) {
        int cycle = 0;
        int x = 1;
        StringWriter sw = new StringWriter();

        for (String line : in.lines().toList()) {
            if (line.equals("noop")) {
                sw.append(drawPixel(cycle++, x));
            } else if (line.startsWith("addx ")) {
                sw.append(drawPixel(cycle++, x));
                sw.append(drawPixel(cycle++, x));
                x += Integer.parseInt(line.substring(5));
            }
        }

        System.out.print("\n" + sw);

        if (sw.toString().equals(testOutputB)) {
            System.out.println("Same as test output for part 2");
        }
    }

    private static String drawPixel(int cycle, int x) {
        int horizontalPosition = cycle % 40;
        String pixel = Math.abs(horizontalPosition - x) < 2 ? "#" : ".";

        if (horizontalPosition == 39) {
            pixel += "\n";
        }

        return pixel;
    }

    private static final String testOutputB = """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
            """;

    private static final String testInput = """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop""";

    private static final String input = """
            noop
            noop
            addx 5
            addx 3
            addx -2
            noop
            addx 5
            addx 4
            noop
            addx 3
            noop
            addx 2
            addx -17
            addx 18
            addx 3
            addx 1
            noop
            addx 5
            noop
            addx 1
            addx 2
            addx 5
            addx -40
            noop
            addx 5
            addx 2
            addx 3
            noop
            addx 2
            addx 3
            addx -2
            addx 2
            addx 2
            noop
            addx 3
            addx 5
            addx 2
            addx 3
            addx -2
            addx 2
            addx -24
            addx 31
            addx 2
            addx -33
            addx -6
            addx 5
            addx 2
            addx 3
            noop
            addx 2
            addx 3
            noop
            addx 2
            addx -1
            addx 6
            noop
            noop
            addx 1
            addx 4
            noop
            noop
            addx -15
            addx 20
            noop
            addx -23
            addx 27
            noop
            addx -35
            addx 1
            noop
            noop
            addx 5
            addx 11
            addx -10
            addx 4
            addx 1
            noop
            addx 2
            addx 2
            noop
            addx 3
            noop
            addx 3
            addx 2
            noop
            addx 3
            addx 2
            addx 11
            addx -4
            addx 2
            addx -38
            addx -1
            addx 2
            noop
            addx 3
            addx 5
            addx 2
            addx -7
            addx 8
            addx 2
            addx 2
            noop
            addx 3
            addx 5
            addx 2
            addx -25
            addx 26
            addx 2
            addx 8
            addx -1
            addx 2
            addx -2
            addx -37
            addx 5
            addx 3
            addx -1
            addx 5
            noop
            addx 22
            addx -21
            addx 2
            addx 5
            addx 2
            addx 13
            addx -12
            addx 4
            noop
            noop
            addx 5
            addx 1
            noop
            noop
            addx 2
            noop
            addx 3
            noop
            noop""";
}
