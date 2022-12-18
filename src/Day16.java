import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day16 {
    public static void main(String... args) {
        partI(testInput);
        partI(realInput);
        partII(testInput);
        partII(realInput);
    }

    record Valve(String id, int rate, Map<String, List<String>> paths) {
        private static Valve parse(String line) {
            String id = line.substring(6, 8);
            int rate = Integer.parseInt(line.substring(line.indexOf("=") + 1, line.indexOf(";")));
            Map<String, List<String>> paths = Arrays.stream(line.split(","))
                                                    .map(s -> s.substring(s.length() - 2))
                                                    .collect(Collectors.toMap(s -> s, List::of));
            return new Valve(id, rate, paths);
        }

        List<String> destinationsAtDistance(int distance) {
            return paths.keySet()
                        .stream()
                        .filter(dest -> paths.get(dest).size() == distance)
                        .toList();
        }
    }

    interface Step {
        Valve valve();

        default int additionalRate() {
            return 0;
        }

        record MoveTo(Valve valve) implements Step {}

        record Open(Valve valve) implements Step {
            @Override
            public int additionalRate() {
                return valve.rate();
            }
        }
    }

    static class Solution {
        private final LinkedList<Step> mySteps = new LinkedList<>();
        private final LinkedList<Step> elephantSteps = new LinkedList<>();
        private final int duration;
        private long score;

        Solution(int duration) {
            this.duration = duration;
        }

        void addForMe(Step step) {
            long rate = step.additionalRate();

            if (rate > 0) {
                score += (duration - mySteps.size() - 1) * rate;
            }

            mySteps.add(step);
        }

        void removeForMe() {
            Step step = mySteps.removeLast();
            long rate = step.additionalRate();

            if (rate > 0) {
                score -= (duration - mySteps.size() - 1) * rate;
            }
        }

        public String myCurrent() {
            return mySteps.isEmpty() ? "AA" : mySteps.getLast().valve().id;
        }

        public Solution copy() {
            Solution c = new Solution(duration);
            c.mySteps.addAll(mySteps);
            c.elephantSteps.addAll(elephantSteps);
            c.score = score;
            return c;
        }

        public boolean isClosed(String id) {
            return mySteps.stream().noneMatch(s -> s.additionalRate() > 0 && s.valve().id.equals(id)) &&
                   elephantSteps.stream().noneMatch(s -> s.additionalRate() > 0 && s.valve().id.equals(id));
        }

        public String elephantCurrent() {
            return elephantSteps.isEmpty() ? "AA" : elephantSteps.getLast().valve().id;
        }

        public void addForElephant(Step step) {
            long rate = step.additionalRate();

            if (rate > 0) {
                score += (duration - elephantSteps.size() - 1) * rate;
            }

            elephantSteps.add(step);
        }

        public void removeForElephant() {
            Step step = elephantSteps.removeLast();
            long rate = step.additionalRate();

            if (rate > 0) {
                score -= (duration - elephantSteps.size() - 1) * rate;
            }
        }
    }

    private static void partI(String input) {
        Map<String, Valve> valves = input.lines().map(Valve::parse).collect(Collectors.toMap(Valve::id, v -> v));
        Solution best = find(explodePaths(valves), new Solution(30));
        printSolution(best);
        System.out.println(best.score + " total pressure released.");
    }

    private static void partII(String input) {
        Map<String, Valve> valves = input.lines().map(Valve::parse).collect(Collectors.toMap(Valve::id, v -> v));
        Solution best = findII(explodePaths(valves), new Solution(26));
        printSolution(best);
        System.out.println(best.score + " total pressure released.");
    }

    private static Solution find(Map<String, Valve> optimizedValves, Solution soFar) {
        Solution best = soFar.copy();

        int[] distances = optimizedValves.get(soFar.myCurrent()).paths.values()
                                                                      .stream()
                                                                      .mapToInt(List::size)
                                                                      .sorted().distinct().toArray();

        for (int distance : distances) {
            if (distance < soFar.duration - soFar.mySteps.size() - 1) {
                for (String next : optimizedValves.get(soFar.myCurrent()).destinationsAtDistance(distance)) {
                    if (soFar.isClosed(next)) {
                        List<String> path = optimizedValves.get(soFar.myCurrent()).paths.get(next);

                        for (String id : path) {
                            soFar.addForMe(new Step.MoveTo(optimizedValves.get(id)));
                        }

                        soFar.addForMe(new Step.Open(optimizedValves.get(next)));
                        Solution cand = find(optimizedValves, soFar);

                        if (cand.score > best.score) {
                            best = cand;
                        }

                        soFar.removeForMe();

                        for (String ignored : path) {
                            soFar.removeForMe();
                        }
                    }
                }
            }
        }

        return best;
    }

    private static Solution findII(Map<String, Valve> valves, Solution soFar) {
        Solution best = soFar.copy();

        int[] myDistances = valves.get(soFar.myCurrent()).paths.values()
                                                               .stream()
                                                               .mapToInt(List::size)
                                                               .sorted().distinct().toArray();

        for (int myDistance : myDistances) {
            if (myDistance < soFar.duration - soFar.mySteps.size() - 1) {
                for (String myNext : valves.get(soFar.myCurrent()).destinationsAtDistance(myDistance)) {
                    if (soFar.isClosed(myNext)) {
                        List<String> myPath = valves.get(soFar.myCurrent()).paths.get(myNext);

                        for (String id : myPath) {
                            soFar.addForMe(new Step.MoveTo(valves.get(id)));
                        }

                        soFar.addForMe(new Step.Open(valves.get(myNext)));

                        int[] elephantDistances = valves.get(soFar.elephantCurrent()).paths.values()
                                                                                           .stream()
                                                                                           .mapToInt(List::size)
                                                                                           .sorted().distinct()
                                                                                           .toArray();

                        for (int elephantDistance : elephantDistances) {
                            if (elephantDistance < soFar.duration - soFar.elephantSteps.size() - 1) {
                                for (String elephantNext : valves.get(soFar.elephantCurrent()).destinationsAtDistance(elephantDistance)) {
                                    if (soFar.isClosed(elephantNext)) {
                                        List<String> elephantPath = valves.get(soFar.elephantCurrent()).paths.get(elephantNext);

                                        for (String id : elephantPath) {
                                            soFar.addForElephant(new Step.MoveTo(valves.get(id)));
                                        }

                                        soFar.addForElephant(new Step.Open(valves.get(elephantNext)));

                                        Solution cand = findII(valves, soFar);

                                        if (cand.score > best.score) {
                                            best = cand;
                                        }

                                        soFar.removeForElephant();

                                        for (String ignored : elephantPath) {
                                            soFar.removeForElephant();
                                        }
                                    }
                                }
                            }
                        }

                        Solution cand = findII(valves, soFar);

                        if (cand.score > best.score) {
                            best = cand;
                        }

                        soFar.removeForMe();

                        for (String ignored : myPath) {
                            soFar.removeForMe();
                        }
                    }
                }
            }
        }

        return best;
    }

    private static Map<String, Valve> explodePaths(Map<String, Valve> valves) {
        Map<String, Valve> exploded = new HashMap<>();

        for (String from : valves.keySet()) {
            Map<String, List<String>> paths = new HashMap<>();
            Valve valve = new Valve(from, valves.get(from).rate, paths);

            for (String to : valves.keySet()) {
                if (valves.get(to).rate > 0) {
                    List<String> path = shortestPath(valves, from, to);
                    paths.put(to, path.subList(1, path.size()));
                }
            }

            exploded.put(from, valve);
        }

        return exploded;
    }

    static List<String> shortestPath(Map<String, Valve> valves, String start, String end) {
        class Info {
            int dist = Integer.MAX_VALUE;
            String prev = null;
        }

        Set<String> q = new HashSet<>(valves.keySet());
        Map<String, Info> info = new HashMap<>();
        q.forEach(id -> info.put(id, new Info()));
        info.get(start).dist = 0;

        while (!q.isEmpty()) {
            String u = q.stream().min(Comparator.comparingInt(pos -> info.get(pos).dist)).orElseThrow();
            q.remove(u);

            for (String v : valves.get(u).paths().keySet()) {
                int alt = info.get(u).dist + valves.get(u).paths().size();

                if (alt < info.get(v).dist) {
                    info.get(v).dist = alt;
                    info.get(v).prev = u;
                }
            }
        }

        Stack<String> stack = new Stack<>();

        while (end != null) {
            stack.push(end);
            end = info.get(end).prev;
        }

        List<String> path = new ArrayList<>();

        while (!stack.isEmpty()) {
            path.add(stack.pop());
        }

        return path;
    }

    private static void printSolution(Solution solution) {
        List<Valve> open = new ArrayList<>();

        for (int i = 0; i < solution.duration; i++) {
            System.out.println("== Minute " + (i + 1) + " ==");

            if (open.isEmpty()) {
                System.out.println("No valves are open.");
            } else {
                String valvesAsString = open.stream().map(Valve::id).collect(Collectors.joining(", "));
                int rate = open.stream().mapToInt(Valve::rate).sum();
                System.out.println("Valves " + valvesAsString + " are open, releasing " + rate + " pressure.");
            }

            if (solution.mySteps.size() > i) {
                if (solution.mySteps.get(i).additionalRate() > 0) {
                    System.out.println("You open valve " + solution.mySteps.get(i).valve().id + ".");
                    open.add(solution.mySteps.get(i).valve());
                    open.sort(Comparator.comparing(Valve::id));
                } else {
                    System.out.println("You move to valve " + solution.mySteps.get(i).valve().id + ".");
                }
            }

            if (solution.elephantSteps.size() > i) {
                if (solution.elephantSteps.get(i).additionalRate() > 0) {
                    System.out.println("The elephant opens valve " + solution.elephantSteps.get(i).valve().id + ".");
                    open.add(solution.elephantSteps.get(i).valve());
                    open.sort(Comparator.comparing(Valve::id));
                } else {
                    System.out.println("The elephant moves to valve " + solution.elephantSteps.get(i).valve().id + ".");
                }
            }

            System.out.println();
        }
    }

    private static final String testInput = """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II""";

    private static final String realInput = """
            Valve MU has flow rate=0; tunnels lead to valves VT, LA
            Valve TQ has flow rate=0; tunnels lead to valves HU, SU
            Valve YH has flow rate=0; tunnels lead to valves CN, BN
            Valve EO has flow rate=0; tunnels lead to valves IK, CN
            Valve MH has flow rate=0; tunnels lead to valves GG, HG
            Valve RJ has flow rate=0; tunnels lead to valves AA, RI
            Valve XZ has flow rate=0; tunnels lead to valves PX, VT
            Valve UU has flow rate=0; tunnels lead to valves DT, XG
            Valve KV has flow rate=13; tunnels lead to valves HN, CV, PE, XD, TA
            Valve SU has flow rate=19; tunnels lead to valves TQ, HF, OL, SF
            Valve BB has flow rate=0; tunnels lead to valves NS, HR
            Valve RI has flow rate=4; tunnels lead to valves ML, EE, TZ, RJ, PE
            Valve TZ has flow rate=0; tunnels lead to valves VT, RI
            Valve LY has flow rate=0; tunnels lead to valves EE, RP
            Valve PX has flow rate=0; tunnels lead to valves XZ, JQ
            Valve VH has flow rate=0; tunnels lead to valves DT, TA
            Valve HN has flow rate=0; tunnels lead to valves KV, LR
            Valve LR has flow rate=0; tunnels lead to valves HR, HN
            Valve NJ has flow rate=0; tunnels lead to valves QF, JC
            Valve AM has flow rate=0; tunnels lead to valves OJ, AA
            Valve FM has flow rate=0; tunnels lead to valves VT, RP
            Valve VT has flow rate=5; tunnels lead to valves IP, XZ, TZ, FM, MU
            Valve HF has flow rate=0; tunnels lead to valves NR, SU
            Valve HR has flow rate=11; tunnels lead to valves BB, KO, LR
            Valve WX has flow rate=0; tunnels lead to valves CN, IP
            Valve PE has flow rate=0; tunnels lead to valves KV, RI
            Valve QF has flow rate=17; tunnels lead to valves YI, NJ
            Valve EE has flow rate=0; tunnels lead to valves LY, RI
            Valve UH has flow rate=25; tunnel leads to valve YI
            Valve CV has flow rate=0; tunnels lead to valves KV, NS
            Valve SF has flow rate=0; tunnels lead to valves YN, SU
            Valve RP has flow rate=3; tunnels lead to valves HG, FM, OJ, IK, LY
            Valve XD has flow rate=0; tunnels lead to valves IL, KV
            Valve GG has flow rate=12; tunnels lead to valves ML, IL, MH, OL, KA
            Valve XG has flow rate=0; tunnels lead to valves LI, UU
            Valve YA has flow rate=21; tunnels lead to valves UJ, GQ
            Valve OL has flow rate=0; tunnels lead to valves GG, SU
            Valve AN has flow rate=0; tunnels lead to valves AA, IX
            Valve LI has flow rate=15; tunnel leads to valve XG
            Valve GQ has flow rate=0; tunnels lead to valves YA, KO
            Valve HU has flow rate=0; tunnels lead to valves TQ, DT
            Valve OJ has flow rate=0; tunnels lead to valves RP, AM
            Valve YN has flow rate=0; tunnels lead to valves SF, JQ
            Valve ML has flow rate=0; tunnels lead to valves RI, GG
            Valve UJ has flow rate=0; tunnels lead to valves YA, NS
            Valve IX has flow rate=0; tunnels lead to valves AN, JQ
            Valve JC has flow rate=0; tunnels lead to valves JQ, NJ
            Valve TA has flow rate=0; tunnels lead to valves KV, VH
            Valve DT has flow rate=16; tunnels lead to valves UU, HU, KA, VH
            Valve NR has flow rate=0; tunnels lead to valves HF, CN
            Valve YI has flow rate=0; tunnels lead to valves QF, UH
            Valve AA has flow rate=0; tunnels lead to valves AM, AN, BN, LA, RJ
            Valve BN has flow rate=0; tunnels lead to valves AA, YH
            Valve KA has flow rate=0; tunnels lead to valves GG, DT
            Valve IL has flow rate=0; tunnels lead to valves GG, XD
            Valve CN has flow rate=7; tunnels lead to valves YH, EO, WX, NR, OM
            Valve IP has flow rate=0; tunnels lead to valves WX, VT
            Valve OM has flow rate=0; tunnels lead to valves CN, JQ
            Valve KO has flow rate=0; tunnels lead to valves GQ, HR
            Valve LA has flow rate=0; tunnels lead to valves AA, MU
            Valve JQ has flow rate=6; tunnels lead to valves IX, JC, PX, YN, OM
            Valve IK has flow rate=0; tunnels lead to valves EO, RP
            Valve HG has flow rate=0; tunnels lead to valves MH, RP
            Valve NS has flow rate=23; tunnels lead to valves CV, BB, UJ""";
}
