import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 {
    static record Pos(int x, int y) {
        Pos move(String direction) {
            return switch (direction) {
                case "L" -> new Pos(x - 1, y);
                case "R" -> new Pos(x + 1, y);
                case "U" -> new Pos(x, y - 1);
                case "D" -> new Pos(x, y + 1);
                default -> throw new IllegalArgumentException();
            };
        }
    }

    public static void main(String... args) {
        a();
        b();
    }

    private static void a() {
        Set<Pos> tailPositions = new HashSet<>();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Pos start = new Pos(0, 4);
        Pos head = start;
        Pos tail = head;
        print(pw, start, head, tail);
        tailPositions.add(tail);

        for (String line : marieInput.lines().toList()) {
            pw.println("== " + line + " ==");
            pw.println();
            String direction = line.substring(0, 1);
            int moves = Integer.parseInt(line.substring(2));

            for (int i = 0; i < moves; i++) {
                head = head.move(direction);

                if (Math.abs(tail.x() - head.x()) > 1) {
                    tail = tail.x() > head.x() ? tail.move("L") : tail.move("R");

                    if (tail.y() != head.y()) {
                        tail = new Pos(tail.x(), head.y());
                    }
                } else if (Math.abs(tail.y() - head.y()) > 1) {
                    tail = tail.y() > head.y() ? tail.move("U") : tail.move("D");

                    if (tail.x() != head.x()) {
                        tail = new Pos(head.x(), tail.y());
                    }
                }

                print(pw, start, head, tail);
                tailPositions.add(tail);
            }
        }

        //AtomicInteger lineNbr = new AtomicInteger(1);
        //sw.toString().lines().forEach(line -> System.out.println(lineNbr.getAndIncrement() + "  " + line));
        //compare(sw.toString(), testOutputA);
        System.out.println(tailPositions.size() + " positions");
    }

    private static void b() {
        Set<Pos> tailPositions = new HashSet<>();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Pos start = new Pos(0, 4);
        List<Pos> knots = Stream.generate(() -> start).limit(10).collect(Collectors.toList());
        print(pw, start, knots.toArray(Pos[]::new));
        tailPositions.add(knots.get(knots.size() - 1));

        for (String line : marieInput.lines().toList()) {
            pw.println("== " + line + " ==");
            pw.println();
            String direction = line.substring(0, 1);
            int moves = Integer.parseInt(line.substring(2));

            for (int i = 0; i < moves; i++) {
                knots.set(0, knots.get(0).move(direction));

                for (int knotIx = 1; knotIx < knots.size(); knotIx++) {
                    Pos head = knots.get(knotIx - 1);
                    Pos tail = knots.get(knotIx);

                    if (Math.abs(tail.x() - head.x()) > 1) {
                        tail = tail.x() > head.x() ? tail.move("L") : tail.move("R");

                        if (tail.y() != head.y()) {
                            tail = new Pos(tail.x(), tail.y() > head.y() ? tail.y() - 1 : tail.y() + 1);
                        }
                    } else if (Math.abs(tail.y() - head.y()) > 1) {
                        tail = tail.y() > head.y() ? tail.move("U") : tail.move("D");

                        if (tail.x() != head.x()) {
                            tail = new Pos(tail.x() > head.x() ? tail.x() - 1 : tail.x() + 1, tail.y());
                        }
                    }

                    knots.set(knotIx, tail);
                }

                print(pw, start, knots.toArray(Pos[]::new));
                tailPositions.add(knots.get(knots.size() - 1));
            }
        }

        //AtomicInteger lineNbr = new AtomicInteger(1);
        //sw.toString().lines().forEach(line -> System.out.println(lineNbr.getAndIncrement() + "  " + line));
        //compare(sw.toString(), testOutputB);
        System.out.println(tailPositions.size() + " positions");
    }

    private static void compare(String a, String b) {
        int row = 1;
        int col = 1;
        a = a.replace("\r", "");

        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                System.out.println(
                        "Diff at row " + row + ", column " + col + ": " + (int) a.charAt(i) + " vs " + (int) b.charAt(i));
                return;
            }

            if (a.charAt(i) == '\n') {
                row++;
                col = 1;
            } else {
                col++;
            }
        }
    }

    private static void print(PrintWriter pw, Pos start, Pos... knots) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 6; x++) {
                boolean printed = false;

                for (int ix = 0; ix < knots.length; ix++) {
                    if (knots[ix].equals(new Pos(x, y))) {
                        pw.print(ix == 0 ? "H" : ix == 1 && knots.length == 2 ? "T" : String.valueOf(ix));
                        printed = true;
                        break;
                    }
                }

                if (!printed) {
                    if (start.equals(new Pos(x, y))) {
                        pw.print("s");
                    } else {
                        pw.print(".");
                    }
                }
            }

            pw.println();
        }

        pw.println();
    }

    private static final String input = """
            D 2
            R 1
            U 1
            L 1
            D 2
            L 2
            R 1
            U 1
            D 2
            U 1
            R 1
            U 1
            D 2
            R 2
            L 2
            U 2
            D 1
            L 2
            R 2
            D 2
            U 2
            R 2
            U 1
            D 1
            L 2
            D 2
            L 2
            U 1
            R 2
            L 2
            D 2
            L 1
            D 2
            L 1
            R 2
            L 2
            U 1
            D 2
            L 1
            U 2
            R 1
            D 2
            L 2
            D 1
            L 2
            U 1
            L 1
            R 2
            U 2
            L 2
            D 1
            L 1
            D 1
            L 2
            R 1
            D 2
            U 2
            D 2
            R 1
            L 2
            R 2
            U 1
            R 1
            L 2
            U 2
            D 1
            U 2
            L 1
            R 2
            D 2
            L 2
            U 1
            L 1
            D 2
            R 2
            U 1
            R 2
            U 2
            D 2
            L 1
            U 1
            D 1
            R 1
            U 1
            D 2
            L 1
            D 2
            U 2
            D 1
            U 2
            L 2
            D 2
            R 2
            D 1
            R 2
            L 2
            U 1
            R 1
            L 1
            U 2
            D 2
            U 2
            L 2
            U 2
            R 2
            L 1
            U 1
            D 1
            L 2
            D 2
            R 2
            U 3
            R 3
            U 2
            L 1
            U 1
            L 1
            U 2
            R 2
            U 2
            D 1
            R 3
            D 1
            L 3
            R 2
            D 3
            U 1
            D 2
            L 2
            D 2
            L 1
            R 3
            D 1
            R 1
            L 3
            R 1
            U 2
            D 2
            L 3
            R 1
            U 2
            L 2
            R 3
            D 1
            U 2
            D 2
            L 3
            D 1
            R 3
            U 3
            R 2
            U 3
            D 1
            U 2
            R 2
            U 3
            R 1
            U 2
            L 3
            D 3
            U 1
            D 2
            U 2
            L 2
            R 1
            L 3
            R 1
            L 3
            U 2
            R 1
            U 1
            L 1
            R 2
            U 2
            D 1
            R 3
            D 3
            L 2
            D 2
            R 2
            L 3
            R 1
            L 1
            R 3
            L 3
            D 2
            U 1
            L 2
            U 3
            R 1
            L 1
            R 1
            L 1
            D 1
            R 2
            D 2
            U 1
            L 1
            D 3
            R 2
            D 1
            L 3
            R 3
            U 3
            D 2
            U 1
            D 2
            R 1
            U 1
            R 2
            D 2
            U 1
            D 3
            R 2
            U 3
            D 3
            R 1
            D 1
            U 2
            D 1
            U 2
            D 3
            U 3
            D 2
            R 4
            D 2
            R 1
            L 4
            U 2
            D 1
            U 2
            R 2
            U 1
            R 1
            U 4
            L 4
            U 3
            L 2
            U 3
            D 3
            R 2
            L 2
            U 3
            D 1
            L 3
            U 1
            L 1
            U 2
            L 4
            R 3
            U 3
            D 1
            L 3
            R 4
            D 4
            L 3
            R 4
            D 1
            R 4
            D 1
            L 3
            D 1
            L 1
            U 2
            D 1
            U 4
            R 1
            D 2
            U 2
            D 4
            U 3
            D 2
            L 4
            R 3
            U 3
            R 2
            D 3
            U 4
            R 4
            L 3
            U 2
            D 2
            U 2
            L 1
            R 3
            D 4
            U 1
            D 2
            U 1
            R 2
            L 2
            R 3
            U 4
            D 1
            L 1
            R 1
            D 4
            L 4
            D 4
            L 2
            R 3
            L 1
            U 2
            L 1
            D 3
            U 2
            R 2
            D 1
            R 1
            U 1
            L 2
            D 2
            U 3
            L 3
            U 1
            L 1
            D 1
            L 3
            R 4
            L 2
            R 1
            U 2
            D 4
            U 2
            L 4
            U 4
            R 1
            D 3
            U 2
            L 2
            D 4
            L 2
            U 1
            D 5
            L 5
            R 1
            D 1
            L 4
            D 5
            R 4
            U 2
            R 2
            L 2
            D 3
            U 4
            L 1
            U 1
            R 2
            L 4
            R 3
            U 1
            D 5
            U 4
            D 2
            R 3
            L 1
            D 4
            L 5
            D 1
            L 2
            U 1
            R 2
            L 2
            R 1
            L 2
            U 4
            D 1
            U 5
            L 2
            R 1
            D 5
            L 2
            D 5
            U 5
            R 3
            L 4
            D 2
            L 2
            D 2
            U 5
            R 3
            U 1
            D 3
            R 4
            U 3
            R 2
            U 3
            D 5
            U 4
            L 3
            R 3
            D 3
            R 1
            L 1
            D 4
            R 3
            D 2
            L 2
            R 4
            L 3
            R 2
            D 4
            L 3
            U 1
            D 2
            L 3
            U 4
            D 5
            U 2
            L 2
            R 1
            D 1
            L 3
            U 2
            R 4
            D 4
            L 4
            U 2
            D 1
            R 5
            L 3
            U 3
            L 5
            R 2
            D 1
            L 2
            D 2
            L 2
            R 4
            L 3
            D 3
            R 5
            L 5
            D 3
            R 5
            L 2
            R 1
            D 4
            L 2
            D 1
            U 2
            D 4
            U 5
            L 4
            D 2
            U 2
            D 4
            U 6
            L 3
            U 1
            D 2
            R 1
            U 5
            D 2
            L 4
            R 6
            U 2
            L 2
            D 1
            R 5
            U 2
            D 5
            R 5
            U 4
            D 6
            U 1
            D 2
            U 5
            D 5
            L 6
            U 5
            D 6
            L 4
            U 6
            R 5
            L 6
            D 5
            L 5
            D 6
            U 5
            L 6
            U 5
            L 3
            R 2
            D 1
            R 1
            L 6
            U 5
            D 1
            R 2
            D 2
            L 2
            D 6
            U 6
            L 1
            R 6
            L 3
            D 6
            R 3
            D 2
            U 5
            D 4
            L 2
            R 3
            D 5
            U 1
            D 3
            R 6
            U 4
            L 4
            U 4
            D 1
            R 4
            U 2
            R 6
            L 1
            D 3
            R 3
            U 1
            L 6
            R 4
            D 2
            U 2
            D 6
            L 4
            R 6
            U 3
            R 3
            D 3
            L 1
            R 5
            U 4
            R 6
            L 2
            R 1
            U 1
            L 6
            D 6
            L 3
            R 2
            D 6
            R 6
            L 3
            U 3
            D 1
            L 3
            D 1
            R 4
            D 3
            U 3
            D 2
            U 6
            D 4
            L 4
            R 5
            D 1
            L 7
            R 3
            L 5
            R 3
            U 6
            L 5
            U 3
            D 1
            L 5
            R 4
            L 1
            U 6
            L 1
            R 4
            D 3
            R 7
            D 7
            R 4
            L 4
            D 4
            L 2
            R 1
            U 7
            L 1
            D 7
            L 1
            D 1
            R 5
            D 5
            U 2
            L 6
            R 2
            U 5
            L 7
            D 7
            L 7
            U 1
            L 6
            D 2
            L 6
            U 2
            R 4
            U 7
            D 2
            L 6
            U 7
            R 5
            U 7
            L 7
            D 2
            R 4
            U 6
            R 1
            D 3
            R 2
            L 5
            R 5
            U 3
            L 5
            U 4
            D 4
            L 5
            R 1
            L 4
            D 5
            U 1
            L 4
            D 7
            R 6
            D 7
            U 7
            D 4
            R 1
            D 2
            L 5
            R 4
            D 3
            U 2
            D 4
            R 3
            L 4
            R 4
            U 3
            D 7
            R 2
            L 1
            D 5
            R 3
            D 2
            L 7
            R 3
            D 5
            R 1
            D 2
            R 6
            L 5
            R 7
            L 5
            U 2
            R 3
            D 6
            L 1
            D 4
            R 2
            U 1
            D 1
            R 6
            D 5
            L 3
            R 7
            L 3
            U 5
            R 5
            U 3
            D 8
            L 2
            U 6
            L 8
            U 4
            D 5
            R 7
            U 7
            L 4
            R 3
            L 3
            D 1
            L 6
            R 5
            L 6
            R 6
            L 8
            D 3
            U 2
            L 7
            R 2
            U 8
            D 3
            L 4
            R 3
            U 8
            R 3
            U 6
            D 8
            R 2
            L 8
            U 2
            R 5
            D 1
            U 1
            D 8
            R 4
            L 7
            D 2
            U 3
            D 1
            R 2
            U 1
            L 5
            U 4
            R 5
            D 6
            L 3
            U 1
            D 6
            U 6
            D 6
            U 2
            L 6
            U 6
            D 5
            L 1
            D 7
            U 2
            D 4
            L 5
            R 4
            D 2
            L 6
            U 2
            R 1
            U 4
            L 3
            D 3
            R 6
            D 4
            R 3
            L 2
            U 8
            R 5
            L 2
            U 3
            R 7
            U 7
            R 4
            U 5
            D 1
            L 8
            R 8
            U 3
            L 1
            U 7
            L 7
            D 2
            L 8
            D 7
            R 7
            L 3
            R 4
            L 3
            R 4
            U 6
            L 5
            R 4
            U 7
            L 3
            D 3
            R 2
            L 6
            U 3
            L 1
            R 8
            L 6
            D 7
            L 8
            R 7
            L 6
            D 2
            U 2
            R 3
            L 9
            R 7
            D 1
            R 9
            L 2
            D 5
            U 7
            D 5
            U 4
            L 4
            D 5
            L 6
            R 3
            U 2
            R 6
            D 3
            U 2
            R 1
            D 5
            U 6
            L 8
            D 8
            U 9
            L 2
            U 5
            L 2
            U 3
            L 4
            U 6
            L 3
            D 2
            R 5
            L 3
            R 8
            D 8
            U 8
            R 4
            U 2
            D 3
            L 9
            U 2
            L 4
            U 5
            R 5
            L 7
            U 9
            L 7
            U 5
            L 6
            U 6
            R 5
            L 9
            U 9
            R 3
            D 8
            U 8
            R 9
            D 4
            U 9
            D 9
            L 3
            R 2
            D 3
            U 1
            R 7
            D 5
            U 9
            D 7
            U 3
            D 8
            L 9
            U 1
            R 6
            L 8
            R 2
            L 7
            D 3
            U 4
            D 9
            L 1
            R 4
            D 9
            U 1
            D 1
            L 8
            U 2
            D 8
            U 1
            L 1
            U 2
            D 7
            L 1
            D 2
            L 6
            U 2
            D 8
            R 7
            L 4
            U 5
            R 2
            U 8
            D 6
            R 3
            D 8
            R 4
            L 5
            U 4
            L 1
            U 6
            L 10
            D 7
            R 5
            D 9
            U 6
            D 5
            L 9
            U 2
            R 3
            U 7
            R 7
            D 1
            R 4
            U 10
            D 4
            R 7
            L 7
            D 3
            R 5
            U 5
            D 6
            U 2
            D 2
            U 6
            D 5
            L 6
            U 10
            D 7
            R 6
            L 9
            R 7
            L 7
            U 6
            D 6
            R 8
            D 8
            R 4
            D 4
            L 1
            R 3
            U 5
            R 2
            L 9
            R 8
            L 3
            U 3
            R 6
            U 2
            L 6
            U 4
            D 5
            R 6
            U 5
            R 8
            U 10
            L 5
            U 2
            R 1
            D 9
            U 8
            D 2
            U 9
            L 7
            R 2
            D 10
            L 7
            D 3
            R 4
            L 2
            U 2
            R 7
            U 6
            D 9
            U 8
            L 7
            D 4
            U 4
            R 6
            U 3
            D 9
            L 1
            U 9
            R 9
            D 4
            L 9
            R 6
            L 6
            U 9
            L 10
            R 1
            L 5
            R 1
            L 2
            D 8
            U 2
            L 1
            R 4
            U 9
            L 3
            D 9
            L 6
            R 8
            U 6
            R 8
            L 5
            D 2
            R 5
            U 8
            R 2
            L 9
            R 3
            U 3
            R 8
            U 5
            D 5
            U 8
            D 10
            R 4
            L 5
            U 2
            L 4
            D 2
            L 1
            D 3
            U 8
            D 3
            U 4
            L 6
            D 6
            U 9
            L 5
            D 5
            U 10
            R 9
            D 2
            U 11
            D 4
            L 8
            D 8
            L 4
            U 6
            D 3
            R 9
            U 11
            L 7
            U 7
            D 9
            U 10
            D 9
            U 2
            R 2
            U 7
            R 2
            U 10
            R 7
            L 7
            U 6
            D 10
            L 5
            U 4
            D 4
            L 3
            D 3
            L 2
            R 5
            D 5
            U 10
            R 3
            U 8
            L 10
            U 5
            R 3
            L 9
            R 5
            D 1
            L 8
            R 8
            D 10
            L 6
            D 5
            L 6
            R 7
            D 10
            U 3
            D 9
            L 8
            D 5
            U 10
            L 5
            U 4
            D 7
            U 11
            L 6
            U 6
            L 9
            R 6
            U 8
            R 8
            L 6
            U 8
            L 8
            U 9
            D 4
            L 10
            D 6
            R 5
            D 8
            L 7
            D 6
            R 4
            D 11
            U 5
            D 4
            R 7
            U 11
            L 5
            U 7
            D 7
            L 11
            R 9
            U 1
            D 11
            R 8
            L 4
            R 2
            D 6
            L 4
            R 8
            D 9
            R 11
            U 2
            L 9
            U 6
            R 10
            L 11
            R 9
            D 10
            U 11
            L 6
            U 3
            L 2
            R 11
            L 2
            D 2
            L 5
            D 6
            U 11
            D 10
            U 8
            L 3
            R 9
            D 5
            R 9
            D 6
            R 2
            U 2
            D 11
            R 6
            U 9
            R 12
            U 6
            L 1
            D 2
            L 5
            D 9
            L 5
            D 4
            U 10
            L 12
            U 6
            R 6
            U 4
            L 4
            U 1
            L 4
            R 12
            L 11
            R 3
            U 10
            L 4
            R 11
            D 9
            L 9
            U 11
            D 1
            L 1
            R 3
            D 7
            U 6
            R 4
            D 3
            L 8
            U 5
            R 6
            L 9
            U 4
            L 6
            D 6
            U 6
            L 5
            R 7
            D 10
            R 5
            U 8
            L 5
            R 6
            U 10
            R 2
            U 2
            R 10
            U 12
            D 12
            L 7
            U 3
            R 1
            L 6
            D 6
            L 5
            D 8
            U 8
            D 11
            L 2
            D 10
            L 6
            R 10
            L 9
            D 5
            R 1
            D 7
            R 6
            U 10
            D 8
            R 9
            D 11
            R 8
            U 7
            R 10
            L 1
            U 3
            R 11
            D 11
            L 8
            D 13
            U 6
            D 12
            U 2
            L 2
            U 11
            D 1
            R 3
            U 8
            R 4
            L 3
            U 7
            D 4
            L 3
            R 2
            L 3
            R 6
            D 13
            L 1
            R 8
            L 5
            R 11
            D 7
            L 8
            D 12
            U 12
            D 3
            L 6
            U 1
            R 7
            U 2
            D 9
            L 3
            U 11
            R 2
            U 6
            D 11
            L 10
            D 4
            U 13
            D 1
            R 11
            L 3
            U 2
            D 7
            R 8
            D 10
            R 12
            D 4
            R 1
            U 5
            R 5
            U 2
            R 12
            D 3
            L 8
            D 11
            U 3
            R 1
            U 2
            R 3
            D 5
            U 8
            D 4
            R 6
            L 2
            R 4
            U 6
            D 12
            L 7
            U 2
            L 8
            U 8
            R 13
            U 10
            L 11
            R 1
            D 4
            R 1
            U 5
            L 9
            D 9
            U 11
            D 13
            U 9
            R 2
            D 9
            U 6
            R 1
            D 12
            R 3
            U 1
            R 13
            U 11
            L 6
            R 3
            D 11
            U 13
            R 2
            U 8
            L 11
            R 12
            D 7
            L 11
            U 10
            D 2
            L 9
            R 1
            L 9
            R 10
            D 5
            U 7
            R 6
            D 11
            U 11
            L 7
            R 6
            L 12
            D 12
            L 5
            R 3
            L 12
            R 4
            L 7
            D 4
            U 11
            R 2
            L 9
            R 11
            D 14
            L 7
            R 10
            U 1
            D 2
            L 7
            D 2
            U 12
            R 2
            D 6
            R 12
            L 7
            D 4
            U 13
            L 11
            D 13
            R 11
            D 5
            U 14
            R 1
            L 8
            U 10
            R 3
            D 3
            L 13
            R 6
            U 5
            D 4
            L 12
            D 2
            R 9
            D 1
            R 7
            D 10
            U 14
            L 12
            R 11
            U 6
            D 11
            L 8
            D 2
            U 4
            L 1
            R 14
            D 8
            U 11
            L 13
            U 1
            D 2
            R 14
            D 10
            R 4
            L 1
            R 10
            U 9
            D 8
            L 10
            R 10
            U 6
            D 14
            L 10
            U 13
            R 8
            L 3
            D 6
            L 13
            D 11
            U 14
            R 13
            U 12
            L 2
            D 6
            U 13
            L 13
            U 12
            D 1
            U 10
            L 5
            U 3
            R 4
            D 10
            L 14
            U 8
            R 7
            U 6
            R 13
            D 14
            L 5
            U 6
            L 1
            D 7
            R 2
            D 14
            U 13
            R 3
            U 5
            R 13
            L 2
            D 12
            U 1
            R 11
            L 7
            U 2
            D 11
            L 2
            R 7
            D 7
            L 1
            U 10
            L 12
            R 6
            L 7
            R 4
            D 3
            R 3
            U 15
            D 13
            U 6
            D 6
            U 3
            R 14
            U 10
            L 1
            R 8
            D 7
            R 13
            D 10
            R 9
            U 11
            D 11
            U 4
            D 11
            U 10
            D 3
            R 12
            U 5
            D 1
            L 4
            R 10
            D 11
            U 2
            D 9
            U 12
            D 9
            R 3
            D 6
            U 4
            L 14
            R 15
            U 4
            D 11
            R 13
            L 3
            U 12
            R 5
            U 11
            L 11
            D 6
            U 15
            R 5
            U 5
            R 1
            D 8
            U 4
            L 12
            D 2
            U 9
            L 15
            D 6
            R 12
            L 10
            D 14
            R 14
            D 8
            L 2
            D 11
            U 13
            L 1
            R 8
            D 8
            L 5
            D 14
            R 10
            U 13
            R 10
            L 4
            R 5
            L 6
            U 7
            R 7
            L 5
            D 3
            L 15
            U 10
            D 6
            L 1
            R 15
            D 11
            U 13
            R 12
            U 4
            D 8
            L 4
            D 11
            R 12
            L 5
            D 2
            U 8
            D 8
            L 10
            U 16
            L 15
            D 9
            U 10
            R 16
            L 7
            R 7
            U 9
            D 12
            L 16
            U 12
            R 13
            U 5
            R 5
            U 8
            D 8
            U 15
            L 4
            R 2
            L 2
            U 10
            D 2
            U 6
            L 15
            R 11
            D 2
            L 5
            R 16
            L 13
            R 14
            U 5
            R 2
            L 11
            U 9
            D 4
            L 14
            U 6
            D 6
            U 7
            D 2
            U 5
            D 6
            U 10
            D 5
            L 11
            R 13
            L 9
            U 5
            R 7
            D 13
            U 12
            L 6
            U 14
            R 15
            L 3
            U 4
            D 4
            U 8
            R 1
            U 12
            L 7
            U 2
            L 11
            R 10
            D 6
            R 9
            L 6
            R 2
            L 16
            U 8
            D 16
            R 8
            U 7
            D 7
            U 12
            D 13
            L 14
            R 3
            U 7
            L 14
            R 14
            U 4
            D 15
            R 13
            D 2
            R 9
            D 14
            U 15
            D 8
            L 8
            R 10
            U 4
            D 5
            L 16
            D 17
            R 14
            U 10
            D 15
            L 15
            R 13
            L 17
            U 12
            R 1
            D 8
            U 2
            R 11
            U 8
            D 9
            R 6
            U 10
            D 3
            L 7
            R 4
            L 15
            U 14
            L 9
            R 15
            U 9
            D 5
            U 4
            D 10
            R 13
            U 9
            D 5
            R 12
            D 6
            U 7
            D 8
            L 7
            U 13
            D 5
            R 4
            L 10
            D 16
            L 11
            R 16
            L 16
            U 15
            D 1
            R 3
            L 13
            D 7
            U 1
            R 15
            U 15
            D 2
            U 3
            L 7
            U 4
            D 14
            L 10
            D 8
            U 8
            D 6
            R 16
            U 6
            D 7
            L 13
            U 3
            L 9
            R 10
            D 5
            R 2
            D 16
            U 14
            R 11
            D 10
            R 13
            U 12
            L 4
            U 7
            L 2
            R 5
            D 13
            U 3
            R 3
            U 16
            L 5
            R 13
            L 16
            D 7
            L 4
            R 14
            U 8
            L 9
            U 2
            L 10
            D 9
            R 17
            U 14
            R 2
            D 7
            U 14
            R 3
            U 3
            L 14
            D 16
            U 6
            L 5
            U 2
            R 7
            L 4
            U 3
            D 12
            L 7
            D 15
            U 6
            L 1
            D 5
            U 11
            L 9
            D 15
            L 12
            R 18
            L 4
            D 9
            U 17
            R 1
            D 5
            L 13
            U 16
            R 17
            U 10
            D 5
            L 16
            D 2
            R 10
            L 1
            U 14
            L 16
            U 3
            D 2
            R 5
            U 3
            R 17
            D 17
            L 15
            U 11
            L 16
            R 4
            L 3
            R 7
            L 5
            R 8
            U 15
            R 9
            U 9
            R 18
            L 17
            R 9
            U 18
            L 11
            U 14
            R 7
            L 16
            R 15
            L 1
            U 9
            L 10
            U 9
            L 14
            U 5
            D 1
            R 16
            D 6
            R 1
            D 14
            R 11
            U 10
            D 17
            L 12
            D 5
            U 4
            L 12
            U 1
            L 7
            R 13
            D 2
            U 14
            D 3
            U 6
            R 1
            U 2
            R 11
            U 9
            D 14
            R 9
            U 14
            R 9
            D 14
            R 2
            D 9
            U 8
            D 10
            U 3
            R 8
            D 11
            R 5
            U 4
            D 1
            U 16
            L 17
            U 1
            R 14
            U 10
            R 5
            U 12
            D 4
            R 10
            D 10
            L 5
            R 2
            L 18
            D 3
            L 17
            D 10
            U 4
            L 15
            U 4
            L 9
            U 14
            R 9
            U 16
            L 1
            R 7
            U 10
            D 1
            U 13
            D 15
            R 6
            U 2
            L 17
            U 11
            L 14
            U 12
            R 11
            U 4
            D 11
            R 7
            D 13
            U 16
            L 5
            U 7
            R 4
            L 17
            D 2
            U 18
            R 8
            U 16
            L 15
            R 8
            L 19
            U 17
            D 2
            U 8
            D 9
            U 3
            L 12
            U 16
            L 3
            R 14
            D 16
            U 11
            D 2
            L 16
            U 4
            L 18
            R 15
            U 9
            L 18
            U 11
            R 18
            L 13
            D 8
            U 12
            D 14
            L 12
            U 2
            D 13
            L 12
            U 16
            R 17
            L 14
            U 10
            R 15
            U 19
            L 17
            R 8
            L 17
            R 8
            U 4
            R 8
            U 11
            L 17
            R 8
            D 13
            U 6
            D 7
            R 9
            L 2
            R 4
            D 16
            L 2
            D 10
            U 4
            L 10
            D 14
            U 5
            D 7
            U 4
            D 16
            U 14
            L 4
            U 14
            L 3
            D 16
            L 14
            U 11
            D 3
            L 10
            U 8
            D 6
            L 9
            D 17
            R 16""";

    private static final String marieInput = """
            D 2
            U 1
            D 1
            U 2
            D 2
            U 2
            R 2
            D 1
            L 1
            U 2
            D 1
            R 1
            U 1
            D 1
            U 1
            D 2
            L 1
            D 1
            U 1
            R 1
            D 1
            R 1
            L 2
            D 1
            R 2
            U 2
            L 1
            U 1
            R 1
            D 1
            U 2
            D 1
            U 1
            R 1
            L 1
            U 1
            R 1
            D 2
            U 2
            R 2
            L 1
            R 2
            L 2
            D 1
            L 2
            R 2
            D 2
            R 2
            D 1
            R 2
            D 1
            R 1
            U 2
            L 2
            D 1
            R 2
            U 2
            L 1
            U 1
            R 2
            D 2
            L 1
            U 2
            R 2
            U 1
            R 1
            D 2
            L 1
            R 2
            U 1
            D 2
            U 1
            R 1
            L 2
            D 1
            U 1
            L 1
            R 2
            L 2
            R 1
            D 2
            L 1
            R 2
            L 2
            R 2
            U 2
            L 2
            R 2
            L 2
            R 1
            L 2
            D 1
            L 2
            D 1
            R 2
            D 2
            U 2
            L 2
            R 2
            U 2
            D 1
            R 2
            L 1
            D 2
            U 1
            L 1
            D 2
            L 1
            U 2
            L 2
            U 1
            L 2
            U 2
            D 1
            R 2
            U 3
            R 3
            L 1
            R 2
            D 1
            R 1
            U 2
            L 2
            U 3
            L 1
            U 1
            R 3
            D 1
            U 1
            R 3
            U 2
            L 2
            D 1
            L 2
            D 2
            U 2
            D 2
            L 3
            U 3
            R 3
            D 1
            L 2
            D 3
            R 2
            L 1
            U 3
            D 2
            U 3
            L 1
            R 3
            U 1
            L 2
            R 1
            D 1
            U 2
            D 2
            R 2
            L 1
            U 3
            L 2
            U 1
            L 3
            U 1
            D 3
            R 3
            L 2
            D 3
            R 1
            L 1
            U 2
            D 3
            L 1
            U 3
            R 1
            D 1
            R 2
            U 1
            L 2
            U 1
            L 2
            D 1
            R 2
            L 3
            R 1
            L 3
            R 1
            D 1
            R 3
            U 1
            L 3
            R 2
            L 1
            U 3
            R 1
            U 3
            D 1
            R 1
            D 1
            R 1
            L 1
            U 3
            R 1
            L 1
            U 2
            D 2
            R 3
            L 1
            U 3
            D 1
            L 2
            U 3
            R 2
            L 1
            R 1
            L 3
            D 1
            U 3
            L 2
            U 2
            R 1
            U 3
            L 2
            U 3
            R 4
            D 1
            L 1
            U 1
            D 3
            R 1
            D 2
            R 2
            U 2
            R 2
            L 3
            D 3
            U 2
            L 2
            D 2
            R 1
            D 1
            U 4
            R 2
            L 2
            R 4
            D 2
            U 4
            L 4
            R 4
            L 1
            D 4
            R 3
            U 2
            D 3
            R 2
            L 2
            R 3
            U 2
            D 4
            L 2
            D 3
            R 4
            D 2
            L 4
            D 2
            L 1
            R 1
            L 3
            U 3
            D 2
            L 2
            R 4
            D 1
            U 3
            D 1
            U 3
            R 4
            L 1
            U 3
            L 1
            R 1
            D 1
            R 2
            D 1
            R 3
            D 1
            U 4
            L 2
            U 1
            D 3
            L 3
            D 2
            L 2
            D 3
            R 1
            D 3
            U 2
            D 3
            U 4
            D 2
            L 1
            R 4
            D 1
            L 1
            D 2
            L 1
            U 3
            L 1
            D 4
            L 3
            D 3
            U 4
            L 2
            R 3
            D 3
            R 2
            D 2
            R 2
            D 1
            L 1
            U 4
            D 1
            U 1
            L 1
            U 3
            R 1
            D 3
            L 3
            R 1
            L 3
            D 4
            R 3
            U 2
            L 2
            R 1
            D 5
            R 3
            L 1
            D 1
            R 1
            L 5
            D 4
            L 1
            R 1
            L 4
            D 3
            L 3
            R 2
            U 5
            L 3
            D 2
            U 4
            D 1
            U 4
            L 3
            U 5
            R 2
            U 3
            L 4
            R 1
            U 3
            L 3
            D 3
            R 4
            U 5
            L 2
            R 4
            L 5
            R 2
            D 1
            L 5
            D 5
            R 5
            D 3
            R 5
            D 3
            L 1
            U 3
            L 2
            D 3
            L 4
            D 5
            L 5
            D 5
            R 4
            L 1
            U 5
            L 3
            U 4
            L 4
            D 1
            L 4
            D 3
            R 4
            U 3
            R 3
            L 4
            U 2
            D 3
            U 4
            D 3
            R 1
            U 2
            D 3
            R 5
            L 2
            R 5
            U 3
            L 4
            R 1
            L 3
            U 1
            D 1
            L 3
            R 5
            U 5
            R 1
            U 4
            L 4
            U 3
            R 5
            L 3
            D 4
            L 1
            U 3
            L 3
            U 2
            D 2
            R 5
            L 5
            U 2
            L 2
            R 3
            U 4
            R 5
            L 5
            D 1
            R 3
            U 4
            L 5
            U 1
            R 4
            U 1
            D 5
            R 3
            D 1
            L 4
            U 5
            D 5
            R 1
            L 6
            D 6
            R 2
            U 4
            L 3
            R 5
            L 5
            R 4
            U 5
            D 3
            L 5
            U 2
            L 3
            D 3
            U 3
            D 6
            U 2
            R 5
            D 1
            U 2
            D 2
            U 2
            R 6
            D 4
            U 4
            R 4
            U 6
            R 3
            U 4
            D 4
            L 3
            U 3
            L 5
            U 3
            R 1
            U 5
            L 6
            U 1
            D 3
            U 5
            D 4
            L 5
            R 5
            U 3
            L 6
            R 2
            L 3
            D 4
            L 2
            U 3
            D 5
            U 4
            D 3
            R 2
            U 6
            D 5
            R 2
            U 6
            L 6
            R 2
            U 3
            L 6
            R 5
            D 4
            U 3
            R 5
            L 6
            R 4
            D 6
            L 3
            R 6
            U 6
            R 4
            U 4
            R 6
            U 2
            D 3
            R 1
            U 3
            D 3
            R 5
            D 3
            R 5
            U 4
            R 4
            L 4
            U 2
            R 1
            U 1
            L 2
            D 1
            R 6
            U 2
            D 2
            L 6
            R 1
            U 3
            L 3
            U 5
            D 4
            L 2
            D 1
            U 1
            D 5
            U 5
            L 5
            D 2
            L 5
            U 5
            D 2
            R 7
            U 7
            D 4
            R 4
            D 6
            U 2
            R 5
            L 2
            U 2
            R 2
            D 6
            U 7
            R 7
            D 7
            U 7
            D 2
            R 4
            D 7
            U 6
            D 1
            R 5
            D 7
            R 3
            L 2
            D 2
            U 2
            L 7
            U 2
            D 3
            U 2
            D 5
            L 4
            U 7
            R 7
            U 2
            L 6
            U 7
            R 5
            U 3
            D 1
            L 2
            U 1
            D 5
            U 7
            D 4
            L 6
            R 5
            L 2
            U 2
            R 6
            L 1
            U 7
            R 7
            U 3
            D 3
            L 2
            R 2
            U 6
            R 4
            D 4
            R 2
            L 6
            R 2
            D 3
            R 7
            D 2
            R 7
            L 4
            R 2
            U 4
            L 6
            U 5
            L 4
            U 7
            L 4
            R 2
            U 3
            L 1
            U 4
            R 5
            L 1
            D 2
            L 2
            U 7
            L 3
            R 3
            U 7
            R 2
            U 1
            L 2
            U 3
            R 7
            D 3
            U 2
            L 3
            U 7
            L 2
            U 5
            R 1
            U 4
            R 4
            D 6
            L 4
            R 7
            U 4
            L 7
            D 4
            L 7
            R 6
            D 4
            L 3
            R 3
            D 7
            L 5
            U 3
            D 4
            U 1
            L 8
            D 7
            L 1
            D 8
            L 3
            R 7
            D 8
            L 5
            D 1
            L 8
            R 7
            D 6
            R 8
            U 6
            R 3
            D 2
            U 7
            R 5
            D 3
            R 2
            L 7
            U 2
            D 6
            R 5
            U 4
            D 3
            R 2
            D 4
            U 5
            R 7
            U 4
            R 5
            L 4
            U 3
            L 1
            D 5
            L 7
            U 5
            D 7
            U 1
            D 4
            U 4
            D 2
            U 6
            L 5
            D 1
            R 4
            L 7
            R 3
            U 8
            L 3
            R 7
            U 1
            R 7
            L 7
            R 3
            D 4
            U 8
            R 1
            D 8
            L 7
            R 6
            U 5
            L 8
            R 6
            D 1
            L 4
            U 5
            L 4
            R 2
            U 7
            D 7
            R 2
            U 2
            D 3
            R 7
            D 8
            L 5
            U 2
            R 8
            D 1
            U 4
            D 6
            L 2
            U 6
            L 2
            R 5
            U 6
            L 8
            D 1
            R 1
            L 1
            D 3
            R 2
            L 2
            D 6
            R 6
            L 6
            U 5
            D 4
            L 4
            U 4
            R 7
            L 4
            U 4
            R 8
            U 5
            D 6
            U 6
            R 2
            U 3
            R 3
            U 6
            L 7
            R 5
            D 8
            U 1
            R 2
            D 4
            U 5
            D 3
            L 1
            U 3
            R 7
            D 8
            L 2
            D 2
            R 6
            U 7
            L 2
            R 4
            D 8
            R 2
            L 3
            U 1
            R 4
            D 3
            R 3
            L 2
            U 6
            D 1
            U 9
            D 8
            L 9
            R 7
            U 3
            L 9
            D 6
            U 2
            D 9
            R 7
            D 2
            L 9
            D 9
            L 2
            D 8
            R 7
            L 8
            U 8
            R 8
            D 7
            L 4
            R 8
            U 8
            R 9
            L 7
            D 7
            L 7
            D 9
            L 9
            D 4
            L 4
            U 4
            D 1
            L 5
            U 3
            R 2
            U 3
            D 3
            R 8
            U 2
            R 2
            D 2
            R 3
            U 1
            D 7
            R 4
            L 4
            U 7
            D 7
            R 1
            D 3
            R 2
            U 3
            D 3
            L 6
            D 1
            R 3
            D 5
            R 1
            U 1
            L 6
            D 1
            L 3
            R 6
            D 8
            U 4
            L 6
            U 3
            D 7
            U 7
            L 8
            U 8
            L 9
            U 6
            R 4
            U 6
            L 5
            R 8
            L 5
            U 9
            D 5
            U 2
            L 3
            U 3
            D 10
            R 9
            L 2
            U 5
            R 7
            U 4
            R 2
            L 3
            U 1
            L 2
            R 1
            D 8
            R 3
            U 1
            L 4
            D 6
            R 9
            D 6
            L 9
            U 9
            L 3
            R 5
            L 8
            U 8
            L 2
            R 7
            L 1
            U 7
            R 6
            U 5
            R 6
            L 5
            D 5
            U 4
            L 2
            D 7
            L 6
            U 4
            R 3
            L 1
            R 7
            U 3
            D 4
            U 5
            L 5
            D 10
            R 6
            U 9
            R 10
            D 7
            R 6
            D 10
            U 8
            D 3
            R 8
            L 6
            D 10
            U 1
            L 10
            U 7
            D 1
            U 2
            L 10
            U 7
            R 3
            D 10
            L 4
            R 6
            D 7
            R 5
            U 10
            L 6
            D 9
            R 8
            D 8
            R 1
            L 10
            D 9
            U 6
            R 6
            U 10
            R 3
            U 4
            R 7
            L 5
            U 4
            L 4
            U 6
            R 4
            U 7
            L 1
            R 2
            U 7
            L 1
            R 3
            D 3
            L 9
            U 10
            R 9
            L 3
            U 2
            R 7
            U 11
            R 4
            U 11
            D 4
            R 4
            L 1
            R 5
            U 6
            L 8
            U 4
            D 11
            U 6
            L 3
            R 8
            U 5
            D 2
            U 6
            D 3
            L 6
            U 7
            D 5
            U 7
            R 4
            L 8
            D 1
            L 5
            U 9
            D 2
            L 8
            U 1
            L 6
            R 8
            D 10
            R 7
            U 1
            R 11
            L 7
            R 3
            U 1
            L 1
            U 8
            D 3
            U 1
            D 8
            U 5
            D 10
            U 10
            R 3
            L 1
            D 10
            U 4
            D 11
            L 6
            R 8
            U 8
            L 9
            D 1
            R 8
            U 8
            D 1
            R 10
            L 10
            U 9
            R 6
            D 3
            U 11
            L 11
            R 2
            L 8
            R 6
            U 7
            R 5
            U 3
            D 3
            R 5
            U 2
            L 1
            U 3
            D 8
            U 6
            R 6
            D 3
            U 5
            R 8
            U 8
            D 7
            L 11
            R 2
            L 9
            R 8
            D 5
            L 4
            R 6
            D 1
            U 9
            D 7
            L 1
            D 5
            L 7
            U 11
            D 8
            U 9
            L 11
            D 3
            R 11
            L 6
            U 6
            D 3
            R 3
            L 8
            R 10
            L 9
            R 8
            U 3
            D 10
            U 10
            R 11
            U 10
            D 2
            U 8
            L 2
            D 2
            R 1
            U 2
            D 5
            L 1
            R 8
            U 3
            R 4
            D 6
            R 1
            U 4
            R 2
            L 1
            U 11
            D 8
            L 7
            R 9
            L 11
            R 1
            U 4
            R 11
            D 4
            L 1
            U 4
            D 5
            R 8
            U 8
            R 9
            U 10
            L 8
            R 9
            D 12
            L 6
            U 1
            R 6
            D 1
            U 6
            D 1
            R 11
            U 5
            R 1
            L 5
            U 6
            R 5
            L 5
            R 4
            D 4
            L 11
            R 12
            L 12
            U 12
            L 7
            D 10
            U 12
            D 4
            R 6
            U 9
            R 5
            U 4
            R 5
            L 11
            U 11
            R 3
            D 10
            R 4
            D 7
            R 4
            D 8
            R 6
            D 7
            L 3
            U 4
            L 3
            D 4
            U 6
            R 8
            U 9
            D 3
            R 8
            L 9
            R 2
            L 1
            D 6
            R 8
            L 5
            R 10
            U 5
            D 11
            U 1
            L 2
            U 7
            L 1
            D 3
            U 11
            D 10
            R 12
            D 9
            U 6
            L 13
            U 4
            L 13
            D 3
            R 9
            L 7
            U 8
            R 1
            L 13
            U 5
            R 4
            U 9
            R 6
            U 8
            L 2
            D 7
            R 8
            L 5
            R 9
            D 3
            R 3
            L 5
            U 12
            D 11
            L 7
            D 8
            L 4
            U 11
            R 10
            D 13
            U 2
            D 5
            U 5
            L 12
            D 7
            U 7
            R 7
            U 11
            R 4
            L 1
            U 7
            R 11
            D 11
            R 3
            U 8
            L 12
            D 2
            R 11
            L 1
            D 10
            U 9
            R 1
            U 1
            D 13
            L 3
            U 7
            L 7
            U 13
            D 3
            U 2
            L 10
            R 9
            L 4
            R 8
            L 10
            R 6
            L 9
            R 4
            D 12
            U 5
            D 8
            U 10
            D 7
            R 5
            D 4
            L 7
            D 5
            R 9
            U 1
            L 6
            D 1
            U 2
            D 2
            R 12
            U 10
            R 11
            L 5
            U 3
            D 2
            U 11
            D 9
            L 2
            R 4
            D 13
            L 2
            U 2
            R 6
            D 4
            U 10
            L 12
            U 11
            R 1
            D 10
            U 6
            R 7
            D 2
            U 5
            D 13
            U 7
            L 4
            D 3
            L 4
            U 4
            D 5
            L 13
            D 14
            U 13
            L 2
            R 3
            U 8
            R 9
            L 10
            R 3
            L 5
            U 8
            D 9
            R 8
            L 2
            U 2
            L 1
            D 1
            R 4
            U 11
            D 13
            R 2
            D 11
            R 4
            U 1
            R 14
            L 10
            U 13
            D 1
            R 10
            D 12
            R 7
            U 11
            R 8
            L 5
            R 4
            D 2
            U 10
            R 9
            L 3
            D 12
            R 10
            U 3
            L 3
            U 3
            D 5
            R 1
            D 1
            U 1
            D 8
            U 9
            R 2
            D 12
            U 13
            D 3
            U 11
            D 13
            L 1
            D 14
            R 9
            D 9
            L 12
            U 1
            D 7
            U 10
            R 4
            L 6
            U 5
            D 12
            U 1
            L 14
            R 6
            L 14
            D 12
            U 4
            R 9
            L 1
            R 11
            U 8
            L 1
            R 6
            U 2
            R 8
            D 9
            U 12
            R 2
            U 12
            R 12
            U 5
            D 13
            L 12
            R 11
            L 11
            R 13
            L 3
            R 10
            L 3
            R 10
            L 6
            D 2
            L 11
            R 4
            L 9
            U 8
            L 9
            D 14
            R 8
            D 5
            R 1
            D 5
            L 2
            D 2
            U 14
            R 6
            U 12
            R 10
            L 2
            U 8
            D 5
            U 1
            D 7
            L 1
            D 9
            R 6
            L 4
            R 6
            U 15
            L 4
            R 8
            U 2
            R 6
            D 10
            L 7
            D 11
            U 6
            D 2
            R 14
            L 11
            U 6
            D 2
            L 12
            U 14
            R 8
            D 11
            U 4
            D 8
            L 6
            D 5
            U 12
            D 1
            U 4
            L 1
            R 1
            U 14
            D 15
            L 11
            R 14
            L 13
            R 9
            D 11
            R 5
            L 13
            U 15
            L 5
            R 8
            D 15
            R 13
            U 3
            L 15
            D 9
            U 5
            D 13
            L 8
            R 11
            D 6
            L 12
            R 8
            L 10
            R 9
            D 12
            L 11
            U 12
            L 1
            U 1
            L 7
            R 6
            L 7
            D 13
            L 6
            R 3
            L 4
            U 2
            D 6
            L 2
            D 2
            R 3
            D 6
            U 7
            D 8
            R 4
            U 7
            D 4
            U 12
            D 10
            L 7
            U 12
            D 12
            U 7
            D 4
            L 9
            D 9
            R 11
            L 5
            U 13
            R 5
            U 6
            D 10
            L 9
            U 13
            D 1
            R 10
            L 12
            R 12
            L 16
            D 10
            U 3
            R 1
            L 7
            D 3
            R 16
            U 9
            L 6
            R 12
            U 10
            L 14
            R 12
            U 15
            L 14
            D 10
            U 15
            L 7
            U 8
            D 9
            R 8
            L 13
            U 6
            R 2
            U 3
            R 9
            D 9
            L 2
            D 10
            U 10
            D 15
            U 4
            R 10
            D 3
            L 10
            R 3
            D 11
            R 11
            U 1
            D 4
            R 15
            U 1
            R 4
            D 3
            L 6
            U 10
            L 9
            R 12
            U 8
            R 13
            L 15
            R 11
            D 12
            U 11
            D 3
            U 8
            R 3
            U 6
            L 7
            U 1
            R 8
            U 16
            L 8
            R 8
            U 1
            R 15
            U 5
            R 15
            U 8
            D 13
            R 5
            U 11
            R 11
            L 4
            R 2
            D 9
            R 3
            L 3
            R 10
            D 9
            L 7
            U 11
            D 9
            U 2
            R 15
            U 13
            D 3
            L 11
            D 9
            R 2
            U 8
            R 1
            L 5
            U 13
            D 12
            L 14
            D 14
            U 16
            D 6
            L 13
            U 12
            L 9
            U 15
            R 11
            U 5
            R 6
            D 6
            L 15
            D 1
            R 13
            D 2
            R 7
            U 6
            R 6
            L 4
            R 7
            U 15
            L 4
            R 5
            L 4
            U 16
            R 13
            L 8
            D 1
            R 14
            D 12
            R 3
            D 1
            R 5
            U 6
            R 4
            D 17
            U 8
            L 12
            R 12
            U 6
            R 14
            L 10
            U 15
            L 3
            U 2
            D 5
            R 4
            U 4
            D 10
            U 7
            L 7
            U 4
            R 1
            D 7
            U 1
            D 2
            L 15
            D 6
            L 13
            D 10
            U 1
            R 2
            L 15
            R 8
            D 4
            U 14
            L 13
            D 7
            R 16
            D 8
            U 9
            D 6
            R 14
            D 16
            U 13
            L 14
            D 5
            U 2
            D 14
            U 12
            L 13
            R 3
            L 13
            R 12
            D 8
            R 4
            L 5
            D 14
            L 8
            U 10
            R 15
            U 5
            D 2
            L 2
            U 1
            D 16
            L 14
            U 10
            R 13
            D 6
            L 13
            U 4
            R 12
            L 14
            U 14
            D 7
            R 12
            L 2
            R 11
            D 11
            R 6
            D 1
            R 14
            L 15
            R 12
            U 7
            D 4
            L 4
            D 7
            R 12
            L 5
            R 1
            L 11
            U 7
            L 1
            D 12
            L 11
            U 9
            L 6
            D 4
            R 15
            L 18
            R 14
            U 13
            R 14
            U 10
            R 11
            D 5
            U 4
            L 2
            D 8
            L 7
            U 14
            L 14
            R 4
            D 15
            U 2
            L 12
            R 10
            D 15
            L 7
            R 1
            U 17
            R 16
            U 4
            L 12
            D 2
            R 10
            U 17
            D 13
            L 5
            D 16
            R 14
            L 9
            D 3
            U 5
            D 8
            U 17
            L 6
            D 6
            L 4
            D 12
            R 10
            U 10
            D 10
            L 1
            R 7
            U 9
            D 7
            R 14
            L 4
            U 6
            L 17
            U 15
            D 13
            L 17
            R 2
            L 6
            D 10
            U 14
            R 9
            L 11
            U 12
            D 7
            R 15
            D 9
            U 13
            D 13
            U 9
            D 17
            U 8
            D 11
            L 12
            R 17
            U 17
            D 6
            L 17
            D 5
            L 5
            D 11
            R 9
            U 9
            D 15
            L 6
            R 4
            L 6
            U 10
            L 2
            U 15
            L 15
            R 6
            L 15
            U 13
            R 9
            D 12
            R 4
            U 11
            R 3
            L 10
            R 18
            L 5
            R 14
            L 3
            D 1
            U 12
            R 10
            L 16
            R 11
            U 6
            D 7
            U 6
            R 5
            D 19
            L 12
            R 16
            D 4
            L 15
            D 14
            L 17
            U 18
            D 13
            R 3
            D 9
            L 14
            U 16
            L 6
            U 14
            L 10
            D 2
            U 8
            L 1
            U 7
            D 12
            U 16
            L 15
            U 16
            R 15
            D 5
            R 7
            U 5
            R 13
            L 16
            D 7
            L 14
            R 9
            U 10
            L 16
            D 4
            U 17
            L 8
            D 3
            U 9
            D 2
            R 8
            D 3
            L 17
            D 1
            U 19
            R 16
            D 8
            U 18
            D 14
            U 1
            D 4
            U 12
            D 17
            R 19
            U 8
            D 7
            U 19
            R 15
            U 10
            R 6
            D 9
            L 6
            R 13
            D 16
            L 17
            R 18
            U 10
            R 19
            U 12
            D 19
            R 2
            L 8
            D 1
            L 17
            D 11
            R 7
            U 17
            L 4
            R 16
            U 17
            D 5
            L 8
            U 17
            L 16
            U 8
            L 19
            R 15
            U 5
            D 2
            R 18
            L 14
            D 9
            L 1
            D 5
            U 5
            D 8
            U 3
            R 9
            L 5
            R 6
            U 10
            R 3
            L 9""";

    private static final String testInput = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2""";

    private static final String testOutputA = """
            ......
            ......
            ......
            ......
            H.....
                        
            == R 4 ==
                        
            ......
            ......
            ......
            ......
            TH....
                        
            ......
            ......
            ......
            ......
            sTH...
                        
            ......
            ......
            ......
            ......
            s.TH..
                        
            ......
            ......
            ......
            ......
            s..TH.
                        
            == U 4 ==
                        
            ......
            ......
            ......
            ....H.
            s..T..
                        
            ......
            ......
            ....H.
            ....T.
            s.....
                        
            ......
            ....H.
            ....T.
            ......
            s.....
                        
            ....H.
            ....T.
            ......
            ......
            s.....
                        
            == L 3 ==
                        
            ...H..
            ....T.
            ......
            ......
            s.....
                        
            ..HT..
            ......
            ......
            ......
            s.....
                        
            .HT...
            ......
            ......
            ......
            s.....
                        
            == D 1 ==
                        
            ..T...
            .H....
            ......
            ......
            s.....
                        
            == R 4 ==
                        
            ..T...
            ..H...
            ......
            ......
            s.....
                        
            ..T...
            ...H..
            ......
            ......
            s.....
                        
            ......
            ...TH.
            ......
            ......
            s.....
                        
            ......
            ....TH
            ......
            ......
            s.....
                        
            == D 1 ==
                        
            ......
            ....T.
            .....H
            ......
            s.....
                        
            == L 5 ==
                        
            ......
            ....T.
            ....H.
            ......
            s.....
                        
            ......
            ....T.
            ...H..
            ......
            s.....
                        
            ......
            ......
            ..HT..
            ......
            s.....
                        
            ......
            ......
            .HT...
            ......
            s.....
                        
            ......
            ......
            HT....
            ......
            s.....
                        
            == R 2 ==
                        
            ......
            ......
            .H....
            ......
            s.....
                        
            ......
            ......
            .TH...
            ......
            s.....
            """;

    private static final String testOutputB = """
            ......
            ......
            ......
            ......
            H.....
                        
            == R 4 ==
                        
            ......
            ......
            ......
            ......
            1H....
                        
            ......
            ......
            ......
            ......
            21H...
                        
            ......
            ......
            ......
            ......
            321H..
                        
            ......
            ......
            ......
            ......
            4321H.
                        
            == U 4 ==
                        
            ......
            ......
            ......
            ....H.
            4321..
                        
            ......
            ......
            ....H.
            .4321.
            5.....
                        
            ......
            ....H.
            ....1.
            .432..
            5.....
                        
            ....H.
            ....1.
            ..432.
            .5....
            6.....
                        
            == L 3 ==
                        
            ...H..
            ....1.
            ..432.
            .5....
            6.....
                        
            ..H1..
            ...2..
            ..43..
            .5....
            6.....
                        
            .H1...
            ...2..
            ..43..
            .5....
            6.....
                        
            == D 1 ==
                        
            ..1...
            .H.2..
            ..43..
            .5....
            6.....
                        
            == R 4 ==
                        
            ..1...
            ..H2..
            ..43..
            .5....
            6.....
                        
            ..1...
            ...H..
            ..43..
            .5....
            6.....
                        
            ......
            ...1H.
            ..43..
            .5....
            6.....
                        
            ......
            ...21H
            ..43..
            .5....
            6.....
                        
            == D 1 ==
                        
            ......
            ...21.
            ..43.H
            .5....
            6.....
                        
            == L 5 ==
                        
            ......
            ...21.
            ..43H.
            .5....
            6.....
                        
            ......
            ...21.
            ..4H..
            .5....
            6.....
                        
            ......
            ...2..
            ..H1..
            .5....
            6.....
                        
            ......
            ...2..
            .H13..
            .5....
            6.....
                        
            ......
            ......
            H123..
            .5....
            6.....
                        
            == R 2 ==
                        
            ......
            ......
            .H23..
            .5....
            6.....
                        
            ......
            ......
            .1H3..
            .5....
            6.....
            """;
}
