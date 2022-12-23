import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day23 {
    public static void main(String... args) {
        partI(testInput);
        partI(realInput);
        partII(testInput);
        partII(realInput);
    }

    private static void partI(String input) {
        Set<Pos> elves = parse(input);
        List<GeneralDirection> orderToCheck = new ArrayList<>(List.of(GeneralDirection.values()));

        for (int round = 1; round <= 10; round++) {
            elves = moveElves(elves, orderToCheck);
            orderToCheck.add(orderToCheck.remove(0));
        }

        int minX = elves.stream().mapToInt(p -> p.x).min().orElseThrow();
        int maxX = elves.stream().mapToInt(p -> p.x).max().orElseThrow();
        int minY = elves.stream().mapToInt(p -> p.y).min().orElseThrow();
        int maxY = elves.stream().mapToInt(p -> p.y).max().orElseThrow();
        System.out.println("Empty slots: " + ((maxX - minX + 1) * (maxY - minY + 1) - elves.size()));
    }

    private static void partII(String input) {
        Set<Pos> elves = parse(input);
        List<GeneralDirection> orderToCheck = new ArrayList<>(List.of(GeneralDirection.values()));

        for (int round = 1; round < Integer.MAX_VALUE; round++) {
            Set<Pos> elvesLastRound = elves;
            elves = moveElves(elves, orderToCheck);

            if (elves.equals(elvesLastRound)) {
                System.out.println("Rounds: " + round);
                return;
            }

            orderToCheck.add(orderToCheck.remove(0));
        }
    }

    private static Set<Pos> moveElves(Set<Pos> elves, List<GeneralDirection> orderToCheck) {
        Map<Pos, List<Pos>> proposedElves = new HashMap<>();

        for (Pos elf : elves) {
            boolean proposedMove = false;

            if (Direction.all.stream().anyMatch(d -> elves.contains(d.move(elf)))) {
                for (GeneralDirection gd : orderToCheck) {
                    if (gd.canMove(elf, elves)) {
                        proposedElves.computeIfAbsent(gd.move(elf), k -> new ArrayList<>()).add(elf);
                        proposedMove = true;
                        break;
                    }
                }
            }

            if (!proposedMove) {
                proposedElves.computeIfAbsent(elf, k -> new ArrayList<>()).add(elf);
            }
        }

        Set<Pos> newElves = new HashSet<>();

        for (Pos elf : proposedElves.keySet()) {
            if (proposedElves.get(elf).size() == 1) {
                newElves.add(elf);
            } else {
                newElves.addAll(proposedElves.get(elf));
            }
        }

        return newElves;
    }

    private static Set<Pos> parse(String input) {
        Set<Pos> elves = new HashSet<>();
        AtomicInteger y = new AtomicInteger(0);
        input.lines().forEach(line -> {
            AtomicInteger x = new AtomicInteger(0);
            line.chars().forEach(c -> {
                if (c == '#') {
                    elves.add(new Pos(x.get(), y.get()));
                }

                x.getAndIncrement();
            });
            y.getAndIncrement();
        });
        return elves;
    }

    record Pos(int x, int y) {
    }

    enum Direction {
        NW(-1, -1),
        N(0, -1),
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0);

        static final List<Direction> all = List.of(Direction.values());

        private final int deltaX;
        private final int deltaY;

        Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        public Pos move(Pos pos) {
            return new Pos(pos.x + deltaX, pos.y + deltaY);
        }
    }

    enum GeneralDirection {
        NORTH(Direction.N, Direction.NE, Direction.NW),
        SOUTH(Direction.S, Direction.SE, Direction.SW),
        WEST(Direction.W, Direction.NW, Direction.SW),
        EAST(Direction.E, Direction.NE, Direction.SE);

        private final Direction[] directions;

        GeneralDirection(Direction... directions) {
            this.directions = directions;
        }

        public Pos move(Pos elf) {
            return directions[0].move(elf);
        }

        public boolean canMove(Pos elf, Set<Pos> elves) {
            return Stream.of(directions).noneMatch(d -> elves.contains(d.move(elf)));
        }
    }

    private static final String testInput = """
            ..............
            ..............
            .......#......
            .....###.#....
            ...#...#.#....
            ....#...##....
            ...#.###......
            ...##.#.##....
            ....#..#......
            ..............
            ..............
            ..............""";

    private static final String realInput = """
            ###..#####..#....##.#..#...#.#.#..#.##..##...#.#.###.#.###...###.#....
            #..#..#.#....#.#.####.#.#.....#..####...####.##.#.#....####...#..#.#..
            ...##...#.#####..###......#.#.#..##.#..#..#####.#..###.#.####.#.####..
            #.#...#...#.#..##.#.#..#.####...#.#####.###.##....##.###.#.##..####.#.
            ...##.##..#.##.###..##.####.##.#...##...#.#...#.##.#..##...###.###....
            .......#.#..#.#....#....#.....##...#.#...###..#..#.##...#....##...#..#
            #.##..#.#..#.#####.#.#....##..#..##..###...#.###.####.#.....#..#.#.##.
            .#####.......###.##.#....#...##.#.#...#.######...##.#.#.###...###..#..
            .#.###..#####.##.##...#..##..#.#..#.####.##..###.##..##..##..##..##.#.
            ..##..###.###.....#...###.#.#...#####.##..######.#.#.#...#..####....#.
            .#.....#..#........#####.###.##..#.####..#.#.#.###.###.#.....###.....#
            .#.##....#..#..#..#..#.#.#.....###.##.####.###.#..#..###..##.......###
            ##.##..###...##....#....#........#.#######....#..#.##..#.##...#.#####.
            .#####.#.#.######.##....#..###.##......####..#...#.##.######...####.##
            #..###..#...##.##..#.#.....##...#.##.#.####.###.##....#.###.###...#.##
            ..##.#...###...#...###.#.#.###.#.####..##..#...#..##...#..####...#.###
            ###.....#..#....#.#......###..##.#...####...#..########..#.##.....####
            ###..##....#.......#..###.#.#..##..#...###.......###....###.##..#..##.
            #..#.#..#.###....#.##.#.....#.##.##..#.##.###..##..###.##.###..#.##...
            #..#....#..##......###.######..#..#####.##...#.#..#..##..#.#....####.#
            .....#...#..####..##..#.#.##..##.###..#.###..#....##.##.##.####.......
            ...#..#.####....###.#......#..###.##.#.#..##.#..#####.###..###..##.###
            #.###...#.#####..##.##..#.#....###.####.##.....####..#...#.###.#####..
            ..#####.#...#.#..#..#.#.#.#.#......#.##.#.....##....#.##..###..####..#
            ...#####.#.#..#..###..##.#.##.##...#.##.#.##.#.#.....#.###..####.#.#..
            ####....##.##....#.#.###.##..#.#......#....#.###..#.#######.#..##...#.
            #.#..#..####..#.#...##.#...###.....##.###...#.#.#####.#....###.###.##.
            #.####.....#........##.#..##..##.###.#.##.#.#.#...##..#########.###..#
            #.#.#..#########.###.##..###.#...#.##.#..#....###...###.#..##.##.##...
            .#.#..##.......###.##.#.##......###..#....###.#...###.#.....#.#####...
            ##.##..#.#..#....####.##..######.#.#..##...#...##.#.##..#.#....#.#.#.#
            .#..#...#.###....#..#.#.#...#####...#.#####.#.....#.#.##.#...##..##.##
            .##.#.######.#.####.#..##.#.####.##.##..##.######..#.#######..#.#..##.
            .#....#.#.###.##.....#..##.#.#####..##..###.##.#...#.#.#....#....###.#
            .#.#.#..#.....##.#....##.#....###.###......#.#..#.#...##...#..#.###..#
            ..#..#....##.##...#.#...#......#.#####....###.#..#.#..#..#.####..#####
            .#.#..#....####..#....##..####.#.#.#...#.##.#..........###.....#..#..#
            .#.##.#.#.....##.#...###.....#####.##.##......###.#.##..####.##....#..
            ...#..##.#..#....#.....##..#.#.##.###.##.#.#..##.#.#...##.#...####...#
            ##.#.########....#.#..##..#.##...###..#...#.##.###..####.###.......#.#
            #.##...##..#####..##..###.#..######..###..######..#.#.#..########.#...
            ##..##..##.##.##...###..#.#.#..#.#.###.#..#...#..#.#....#..##....#..#.
            ..#####..#.#.##.##.####.#..#.####.#.#.....#....#.##...##.....###....##
            ##..####..#.###.##..######.#..#.##.##..####..#.##.#.##.#.#.###.#.##...
            #.#...#..##.##...##....#..#..#.#.#####....#.#.#.###..#..#.#..#....#...
            #.###....##.........###...###..##.#..#.##.#.....#..#.######..#.###....
            #######..###.#..###...#..#####.....#..#...#..##..#....#.#####..###....
            .#...###..###....##.##.....#.##.#.###.#.####...#...#.#.##........#.#.#
            ##..#..#......#.#.###.#.#.#.#....#.#...####.###.##.#.#..###........##.
            .#..#..#.#.###..#...#..###.##....#........#.#.#.####.#..#.#.####..#.##
            ..########..#..#..#.####.####.###.#####..#..######..#.#.#....#......#.
            ###.##..##.####.#.......###.....####.#.#..#.#.#..#.#..##.###...#.##.##
            .....##..#..#.#.#.####...##..######.#.####..#..##.###......###.#....#.
            #..###...#..##........#.#.###.##.##..#..#..##.##.#.#..##.#..##.##...#.
            ..#....#.#.#####.##...#.##.#..##....##.##.#....##..#.######..#.####.#.
            #...##.#.##.#.#...#.###.#.#.##.....##.###.#...#.#.#####.######.##.#.##
            #.#.###..#.#..####..###..#####.#.........#..####.##.###.###.#....#....
            .#.#...#.#.###..##....#..#.###.#...#.#..###..###..#.##.#.##.#.#...##..
            ...##..#.#.#.###..##...#####..##.#.#.####.#...####.#.#..#..##...##.###
            ...####..###.###.#.#...#.##...#.###...#.#.#.#.#.....#..#...##..##.#...
            ##..###...##.#.###.##.##..#.#####.###.......##.####..##.....#.#.##..#.
            ......##.....#...#.###.##.#..#####.#.###....#.#..###.#....##...##.#.##
            ...#.#.###.##...##.####..##.##..#...#.#.......##..#..#....##...#.#.#..
            ..##.....###.....#...#.#.##..###..####.###..###...#..#...#..###...##..
            .#.#.#.#..####..#...###.#.#..##.##.#.#..#..##.......#.#..#.####.###.##
            #..###..###.#.......#.#...##.###.#....#..#.##.#.##.##.####.####.#####.
            ###.#...#....#.#..##.#.##.##.#.#..##..##..#####.#.#.#.##.....##.##..##
            ##.##..#....##...###.##..#...#.....#.#..##......#...#.###....#.#..#..#
            ##..#..###..####.#.#.#.##.#.###.###.##.#.##....##.#.#..#.#..#.#...###.
            #.##..##.#.###..#.#.####.#...#...##..#..##.###.#.#.......#....##.##...""";
}
