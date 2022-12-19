import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    public static void main(String... args) {
        time(() -> partI(testInput));
        time(() -> partI(realInput));
        time(() -> partII(testInput));
        time(() -> partII(realInput));
    }

    private static void time(Runnable r) {
        long start = System.currentTimeMillis();
        r.run();
        System.out.println((System.currentTimeMillis() - start) + " ms\n");
    }

    private static void partI(String input) {
        List<Blueprint> blueprints = input.lines().map(Blueprint::parse).toList();
        int sumOfQualityLevels = 0;

        for (Blueprint blueprint : blueprints) {
            System.out.println("== " + blueprint + " ==");
            State start = new State(24, 1, 0, 0, 0, 0, 0, 0, 0);
            State end = findBestEndState(blueprint, start, new HashMap<>(), start);
            System.out.println(end);
            sumOfQualityLevels += blueprint.number * end.geodes;
        }

        System.out.println("Sum of quality levels: " + sumOfQualityLevels);
    }

    private static void partII(String input) {
        List<Blueprint> blueprints = input.lines().map(Blueprint::parse).toList();
        int productOfGeodes = 1;

        for (Blueprint blueprint : blueprints.stream().limit(3).toList()) {
            System.out.println("== " + blueprint + " ==");
            State start = new State(32, 1, 0, 0, 0, 0, 0, 0, 0);
            State end = findBestEndState(blueprint, start, new HashMap<>(), start);
            System.out.println(end);
            productOfGeodes *= end.geodes;
            System.out.println("Geodes: " + end.geodes);
        }

        System.out.println("Product of geodes: " + productOfGeodes);
    }

    private static State findBestEndState(Blueprint blueprint, State now, Map<State, State> cache, State best) {
        if (now.minutesLeft == 0) {
            return now;
        }

        if (now.minutesLeft * now.geodeRobots + now.geodes + now.minutesLeft * now.minutesLeft / 2 < best.geodes) {
            return now;
        }

        if (cache.containsKey(now)) {
            return cache.get(now);
        }

        State next = now.nextMinute();
        int tried = 0;

        for (Robot robot : Robot.values()) {
            if (blueprint.canBuy(robot, now)) {
                State end = findBestEndState(blueprint, blueprint.buy(robot, next), cache, best);
                best = best.max(end);
                tried++;

                if (tried > 1) {
                    break;
                }
            }
        }

        State end = findBestEndState(blueprint, next, cache, best);
        best = best.max(end);
        cache.put(now, best);
        return best;
    }

    private enum Robot {
        GEODE,
        OBSIDIAN,
        CLAY,
        ORE
    }

    private static record Blueprint(int number, int orePerOreRobot, int orePerClayRobot,
                                    int orePerObsidianRobot, int clayPerObsidianRobot,
                                    int orePerGeodeRobot, int obsidianPerGeodeRobot) {

        boolean canBuy(Robot robot, State s) {
            return switch (robot) {
                case ORE -> s.ores >= orePerOreRobot;
                case CLAY -> s.ores >= orePerClayRobot;
                case OBSIDIAN -> s.ores >= orePerObsidianRobot && s.clays >= clayPerObsidianRobot;
                case GEODE -> s.ores >= orePerGeodeRobot && s.obsidians >= obsidianPerGeodeRobot;
            };
        }

        State buy(Robot robot, State s) {
            return switch (robot) {
                case ORE -> new State(s.minutesLeft, s.oreRobots + 1, s.clayRobots, s.obsidianRobots, s.geodeRobots,
                        s.ores - orePerOreRobot, s.clays, s.obsidians, s.geodes);
                case CLAY -> new State(s.minutesLeft, s.oreRobots, s.clayRobots + 1, s.obsidianRobots, s.geodeRobots,
                        s.ores - orePerClayRobot, s.clays, s.obsidians, s.geodes);
                case OBSIDIAN -> new State(s.minutesLeft, s.oreRobots, s.clayRobots, s.obsidianRobots + 1, s.geodeRobots,
                        s.ores - orePerObsidianRobot, s.clays - clayPerObsidianRobot, s.obsidians, s.geodes);
                case GEODE -> new State(s.minutesLeft, s.oreRobots, s.clayRobots, s.obsidianRobots, s.geodeRobots + 1,
                        s.ores - orePerGeodeRobot, s.clays, s.obsidians - obsidianPerGeodeRobot, s.geodes);
            };
        }

        private static Blueprint parse(String line) {
            int number = number(line, "Blueprint ", ":");
            String[] parts = line.split("\\.");
            int orePerOre = number(parts[0], "costs ", " ore");
            int orePerClay = number(parts[1], "costs ", " ore");
            int orePerObsidian = number(parts[2], "costs ", " ore");
            int clayPerObsidian = number(parts[2], "and ", " clay");
            int orePerGeode = number(parts[3], "costs ", " ore");
            int obsidianPerGeode = number(parts[3], "and ", " obsidian");
            return new Blueprint(number, orePerOre, orePerClay, orePerObsidian, clayPerObsidian, orePerGeode, obsidianPerGeode);
        }

        private static int number(String text, String before, String after) {
            int startIx = text.indexOf(before) + before.length();
            int endIx = text.indexOf(after, startIx);
            return Integer.parseInt(text.substring(startIx, endIx));
        }
    }

    private static record State(int minutesLeft,
                                int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots,
                                int ores, int clays, int obsidians, int geodes) {

        State nextMinute() {
            return new State(minutesLeft - 1, oreRobots, clayRobots, obsidianRobots, geodeRobots,
                    ores + oreRobots, clays + clayRobots, obsidians + obsidianRobots, geodes + geodeRobots);
        }

        public State max(State that) {
            int c = Integer.compare(geodes, that.geodes);

            if (c == 0) {
                c = Integer.compare(obsidians, that.obsidians);

                if (c == 0) {
                    c = Integer.compare(clays, that.clays);

                    if (c == 0) {
                        c = Integer.compare(ores, that.ores);
                    }
                }
            }

            return c > 0 ? this : that;
        }
    }

    private static final String testInput = """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.""";

    private static final String realInput = """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 2 ore and 18 obsidian.
            Blueprint 2: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 4 ore and 15 obsidian.
            Blueprint 3: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 4: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 19 clay. Each geode robot costs 2 ore and 12 obsidian.
            Blueprint 5: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 4 ore and 7 obsidian.
            Blueprint 6: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 7 clay. Each geode robot costs 2 ore and 19 obsidian.
            Blueprint 7: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 4 ore and 11 obsidian.
            Blueprint 8: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 3 ore and 10 obsidian.
            Blueprint 9: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 6 clay. Each geode robot costs 2 ore and 16 obsidian.
            Blueprint 10: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 15 obsidian.
            Blueprint 11: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 10 clay. Each geode robot costs 2 ore and 13 obsidian.
            Blueprint 12: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 12 clay. Each geode robot costs 3 ore and 17 obsidian.
            Blueprint 13: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 19 clay. Each geode robot costs 2 ore and 18 obsidian.
            Blueprint 14: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 6 clay. Each geode robot costs 4 ore and 11 obsidian.
            Blueprint 15: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 12 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 16: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 16 clay. Each geode robot costs 4 ore and 16 obsidian.
            Blueprint 17: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 18: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 2 ore and 16 obsidian.
            Blueprint 19: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 2 ore and 8 obsidian.
            Blueprint 20: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 3 ore and 14 obsidian.
            Blueprint 21: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 2 ore and 13 obsidian.
            Blueprint 22: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 9 clay. Each geode robot costs 3 ore and 7 obsidian.
            Blueprint 23: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 4 ore and 8 obsidian.
            Blueprint 24: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 12 obsidian.
            Blueprint 25: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 3 ore and 16 obsidian.
            Blueprint 26: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 3 ore and 10 obsidian.
            Blueprint 27: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 9 clay. Each geode robot costs 3 ore and 7 obsidian.
            Blueprint 28: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 11 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 29: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 16 clay. Each geode robot costs 2 ore and 11 obsidian.
            Blueprint 30: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 2 ore and 13 obsidian.""";
}
