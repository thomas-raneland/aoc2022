import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day15 {
    public static void main(String... args) {
        partI(testInput, 10);
        partI(realInput, 2_000_000);
        partII(testInput, 20);
        partII(realInput, 4_000_000);
    }

    private static record Pos(int x, int y) {
        long tuningFrequency() {
            return x * 4_000_000L + y;
        }
    }

    private static record Pair(Pos sensor, Pos beacon) {
        int distance() {
            return Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
        }

        void addNoBeaconPositionsTo(Set<Pos> noBeacon, int line) {
            int distance = distance();

            for (int x = sensor.x - distance; x <= sensor.x + distance; x++) {
                Pos pos = new Pos(x, line);

                if (new Pair(sensor, pos).distance() <= distance) {
                    noBeacon.add(pos);
                }
            }
        }

        Pos move(Pos pos) {
            if (new Pair(sensor, pos).distance() <= distance()) {
                return new Pos(pos.x, sensor.y + distance() - Math.abs(pos.x - sensor.x) + 1);
            }

            return pos;
        }
    }

    private static void partI(String input, int line) {
        List<Pair> pairs = input.lines().map(Day15::parsePair).toList();
        Set<Pos> sensors = new HashSet<>();
        Set<Pos> beacons = new HashSet<>();
        Set<Pos> noBeacons = new HashSet<>();

        for (Pair pair : pairs) {
            sensors.add(pair.sensor);
            beacons.add(pair.beacon);
            pair.addNoBeaconPositionsTo(noBeacons, line);
        }

        noBeacons.removeAll(sensors);
        noBeacons.removeAll(beacons);
        System.out.println(noBeacons.size());
    }

    private static void partII(String input, int max) {
        List<Pair> pairs = input.lines().map(Day15::parsePair).toList();
        Pos previousPos = null;
        Pos pos = new Pos(0, 0);

        while (!pos.equals(previousPos)) {
            previousPos = pos;

            for (Pair pair : pairs) {
                pos = pair.move(pos);
            }

            if (pos.y > max) {
                pos = new Pos(pos.x + 1, 0);
            }
        }

        System.out.println(pos + " => " + pos.tuningFrequency());
    }

    private static Pair parsePair(String line) {
        String[] parts = line.split(":");
        Pos sensor = new Pos(parseInt(parts[0], "x="), parseInt(parts[0] + ",", "y="));
        Pos beacon = new Pos(parseInt(parts[1], "x="), parseInt(parts[1] + ",", "y="));
        return new Pair(sensor, beacon);
    }

    private static int parseInt(String line, String s) {
        int ix1 = line.indexOf(s) + s.length();
        int ix2 = line.indexOf(",", ix1);
        return Integer.parseInt(line.substring(ix1, ix2));
    }

    private static final String testInput = """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3""";

    private static final String realInput = """
            Sensor at x=193758, y=2220950: closest beacon is at x=652350, y=2000000
            Sensor at x=3395706, y=3633894: closest beacon is at x=3404471, y=3632467
            Sensor at x=3896022, y=3773818: closest beacon is at x=3404471, y=3632467
            Sensor at x=1442554, y=1608100: closest beacon is at x=652350, y=2000000
            Sensor at x=803094, y=813675: closest beacon is at x=571163, y=397470
            Sensor at x=3491072, y=3408908: closest beacon is at x=3404471, y=3632467
            Sensor at x=1405010, y=486446: closest beacon is at x=571163, y=397470
            Sensor at x=3369963, y=3641076: closest beacon is at x=3404471, y=3632467
            Sensor at x=3778742, y=2914974: closest beacon is at x=4229371, y=3237483
            Sensor at x=1024246, y=3626229: closest beacon is at x=2645627, y=3363491
            Sensor at x=3937091, y=2143160: closest beacon is at x=4229371, y=3237483
            Sensor at x=2546325, y=2012887: closest beacon is at x=2645627, y=3363491
            Sensor at x=3505386, y=3962087: closest beacon is at x=3404471, y=3632467
            Sensor at x=819467, y=239010: closest beacon is at x=571163, y=397470
            Sensor at x=2650614, y=595151: closest beacon is at x=3367919, y=-1258
            Sensor at x=3502942, y=6438: closest beacon is at x=3367919, y=-1258
            Sensor at x=3924022, y=634379: closest beacon is at x=3367919, y=-1258
            Sensor at x=2935977, y=2838245: closest beacon is at x=2645627, y=3363491
            Sensor at x=1897626, y=7510: closest beacon is at x=3367919, y=-1258
            Sensor at x=151527, y=640680: closest beacon is at x=571163, y=397470
            Sensor at x=433246, y=1337298: closest beacon is at x=652350, y=2000000
            Sensor at x=2852855, y=3976978: closest beacon is at x=3282750, y=3686146
            Sensor at x=3328398, y=3645875: closest beacon is at x=3282750, y=3686146
            Sensor at x=3138934, y=3439134: closest beacon is at x=3282750, y=3686146
            Sensor at x=178, y=2765639: closest beacon is at x=652350, y=2000000
            Sensor at x=3386231, y=3635056: closest beacon is at x=3404471, y=3632467
            Sensor at x=3328074, y=1273456: closest beacon is at x=3367919, y=-1258
            Sensor at x=268657, y=162438: closest beacon is at x=571163, y=397470""";
}
