import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Day12 {
    public static void main(String... args) {
        System.out.println("------------------------ Part I ------------------------");
        partI(testInput);
        partI(realInput);
        System.out.println("------------------------ Part II ------------------------");
        partII(testInput);
        partII(realInput);
    }

    static record Pos(int x, int y) {
        public int value(List<List<Integer>> map) {
            return map.get(y).get(x);
        }

        public boolean exists(List<List<Integer>> map) {
            return y >= 0 && y < map.size() && x >= 0 && x < map.get(y).size();
        }
    }

    private static void partI(String input) {
        AtomicReference<Pos> start = new AtomicReference<>();
        AtomicReference<Pos> end = new AtomicReference<>();
        List<List<Integer>> map = readMap(input, start, end);
        int steps = shortestPath(map, Set.of(start.get()), end.get());
        System.out.println(steps);
    }

    private static void partII(String input) {
        AtomicReference<Pos> start = new AtomicReference<>();
        AtomicReference<Pos> end = new AtomicReference<>();
        List<List<Integer>> map = readMap(input, start, end);
        Set<Pos> starts = new HashSet<>();

        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                Pos pos = new Pos(x, y);

                if (pos.value(map) == 0) {
                    starts.add(new Pos(x, y));
                }
            }
        }

        System.out.println(shortestPath(map, starts, end.get()));
    }

    private static List<List<Integer>> readMap(String input, AtomicReference<Pos> start, AtomicReference<Pos> end) {
        List<List<Integer>> map = new ArrayList<>();

        input.lines().forEach(line -> {
            List<Integer> current = new ArrayList<>();

            line.chars().forEach(c -> {
                switch (c) {
                case 'S' -> {
                    start.set(new Pos(current.size(), map.size()));
                    current.add(0);
                }
                case 'E' -> {
                    end.set(new Pos(current.size(), map.size()));
                    current.add('z' - 'a');
                }
                default -> current.add(c - 'a');
                }
            });

            map.add(current);
        });

        if (start.get() == null || end.get() == null) {
            throw new IllegalArgumentException();
        }

        return map;
    }

    static int shortestPath(List<List<Integer>> map, Set<Pos> starts, Pos end) {
        class Info {
            int dist = Integer.MAX_VALUE;
            Pos prev = null;
        }

        Map<Pos, Info> info = new HashMap<>();
        Set<Pos> q = new HashSet<>();

        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                Pos pos = new Pos(x, y);
                info.put(pos, new Info());
                q.add(pos);
            }
        }

        for (Pos start : starts) {
            info.get(start).dist = 0;
        }

        while (!q.isEmpty()) {
            Pos u = q.stream().min(Comparator.comparingInt(pos -> info.get(pos).dist)).orElseThrow();
            q.remove(u);

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    Pos v = new Pos(u.x + x, u.y + y);

                    if (isValidMove(map, u, v)) {
                        int alt = info.get(u).dist + 1;

                        if (alt < info.get(v).dist) {
                            info.get(v).dist = alt;
                            info.get(v).prev = u;
                        }
                    }
                }
            }
        }

        return info.get(end).dist;
    }

    private static boolean isValidMove(List<List<Integer>> map, Pos from, Pos to) {
        return Math.abs(from.x - to.x + from.y - to.y) == 1 && to.exists(map) && to.value(map) < from.value(map) + 2;
    }

    private static final String testInput = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi""";

    private static final String realInput = """
            abcccccccccaaaaaaaaaaccccccccccccaaaaaaaaccaaccccccccccccccccccccccccccccccccccccccccccccaaaaaa
            abccccccccccaaaaaaaaaccccccccccccaaaaaaaaaaaacccccccccccaacccacccccccccccccccccccccccccccaaaaaa
            abcccccccccccaaaaaaacccccccccccccaaaaaaaaaaaaaacccccccccaaacaacccccccccaaaccccccccccccccccaaaaa
            abccccccccccaaaaaaccccccccccccccaaaaaaaaaaaaaaaccccccccccaaaaaccccccccccaaacccccccccccccccccaaa
            abccccccccccaaaaaaaccccccccccccaaaaaaaaaaaaaacccccccccccaaaaaacccccccccaaaacccccccccccccccccaac
            abaaccaaccccaaccaaaccccccccaaaaaaaaaaaaaaacaaccccccccccaaaaaaaacccccccccaaalcccccccccccccccaaac
            abaaaaaacccccccccaaccccccccaaaaaacccaaaacccaaccccccccccaaaaaaaaccccccccalllllllcccccccccccccccc
            abaaaaaacccccccaaacccccccccaaaaccccccaaaccccaaaaacccccccccaacccccccaaaakllllllllcccccccaacccccc
            abaaaaaacccccccaaaacccccccccaacccccccaaaccccaaaaacccccccccaacccccccaakkklllpllllccccacaaacccccc
            abaaaaaaaccccccaaaaccccaaccccccccccccccccccaaaaaaccccccccccccccccccckkkkpppppplllcccaaaaaaacccc
            abaaaaaaacaaaccaaaaccaaaaaaccccccccccccccccaaaaaacccccccaaaccccckkkkkkkpppppppplllcddaaaaaacccc
            abcaaaacccaacccccccccaaaaaacccccaaaccccccccaaaaaacccccccaaaaccjkkkkkkkpppppuppplmmdddddaaaccccc
            abccaaaaaaaaaccccccccaaaaaaccccaaaaaacccccccaaacccccccccaaaajjjkkkkkrpppuuuuupppmmmdddddacccccc
            abccccaaaaaaaacccccccaaaaacccccaaaaaacccccccccccccccccccaaacjjjjrrrrrrppuuuuupqqmmmmmddddaccccc
            abccccaaaaaaaaacccccccaaaacccccaaaaaaccccccccccccccccccccccjjjrrrrrrrrpuuuxuvvqqqmmmmmddddccccc
            abccccaaaaaaaaacccccccccccccccccaaaaaccccaacccaccccccccaaccjjjrrrruuuuuuuxxyvvqqqqqmmmmmdddcccc
            abccccaaaaaaaacccccccccaaaccccccaacaaccccaaacaacccaaacaaaccjjjrrrtuuuuuuuxxyvvvqqqqqmmmmdddcccc
            abccaaaaaaaacccccccccccaaaaaccccccccccccccaaaaacccaaaaaaaccjjjrrttttxxxxxxyyvvvvvqqqqmmmmdeeccc
            abccaaaccaaaccccccccaacaaaaacccccccccccccaaaaaacccaaaaaacccjjjrrtttxxxxxxxyyvvvvvvvqqqmmmeeeccc
            abaaaaaaaaaacccaaaccaaaaaaaaaaaccaaaccccaaaaaaaacccaaaaaaaajjjqqrttxxxxxxxyyyyyyvvvqqqnnneeeccc
            SbaaaaaaaaccccaaaaccaaaaaaaaaaaaaaaaacccaaaaaaaaccaaaaaaaaacjjjqqtttxxxxEzzyyyyvvvvqqqnnneeeccc
            abcaaaaaacccccaaaaccccaaaaaaaccaaaaaaccccccaaccccaaaaaaaaaaciiiqqqtttxxxyyyyyyvvvvrrrnnneeecccc
            abcaaaaaacccccaaaacccaaaaaaaaccaaaaaaccccccaaccccaaacaaacccciiiqqqqttxxyyyyyywvvvrrrnnneeeecccc
            abcaaaaaaccccccccccccaaaaaaaaacaaaaacccccccccccccccccaaaccccciiiqqtttxxyyyyyywwrrrrnnnneeeccccc
            abcaaacaacccccaacccccaaaaaaaaacaaaaacccccccccccccccccaaaccccciiiqqttxxxywwyyywwrrrnnnneeecccccc
            abccccccccaaacaaccccccccccacccccccccccccccccccccccccccccccccciiqqqttxxwwwwwwywwrrrnnneeeccccccc
            abccaacccccaaaaaccccccccccccccccccccccccccccccccccccccccaacaaiiqqqttwwwwsswwwwwrrrnnfffeccccccc
            abaaaaccccccaaaaaacccccccccccccccccccccccccccccaaaccccccaaaaaiiqqqttssssssswwwwrrronfffaccccccc
            abaaaaaacccaaaaaaacccccccccccccccccccccccccccaaaaaacccccaaaaaiiqqqssssssssssswrrrooofffaaaacccc
            abaaaaaaccaaaaaacccccccccccccccccccccccccccccaaaaaacccccaaaaaiiqqqppssspppssssrrrooofffaaaacccc
            abaaaaaaccaacaaacccccccccccccccccccccccccccccaaaaaacccccaaaaaiihpppppppppppossrrooofffaaaaacccc
            abaaaaccccccccaacccccccccccccccccccccccccccccaaaaaccccccccaaahhhhppppppppppoooooooofffaaaaccccc
            abaaaaccccccccccaacccccccccccccccccaaacccccccaaaaacccccccccccchhhhhhhhhhggpoooooooffffaaaaccccc
            abccaacccccccacaaaccccccccccccccccaaaaacccccccccccccccccccccccchhhhhhhhhggggoooooffffaacaaacccc
            abccccccccccaaaaacaaccccccccccccccaaaaaccccccccccccccccccccccccchhhhhhhhggggggggggffcaacccccccc
            abccccccccccaaaaaaaaccccccccccccccaaaacccaacccccccccccaccccccccccccccaaaaaggggggggfcccccccccccc
            abccccccccccccaaaaaccccaacccccccccaaaacaaaaccccccccaaaaccccccccccccccaaaacaaagggggcccccccccaccc
            abcccccccccccaaaaacccccaacccccccccaaaaaaaaaccccccccaaaaaaccccccccccccaaaccaaaacccccccccccccaaac
            abcccccccccccaacaaccaaaaaaaacccaaaaaaaaaaaccccccccccaaaaccccccccccccccaccccaaacccccccccccccaaaa
            abccccccccccccccaaccaaaaaaaaccaaaaaaaaaaaccccccccccaaaaacccccccccccccccccccccacccccccccccccaaaa
            abccccccccccccccccccccaaaaacccaaaaaaaaaaaacccccccccaacaacccccccccccccccccccccccccccccccccaaaaaa""";

}
