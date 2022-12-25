import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Day24 {
    public static void main(String... args) {
        partI(testInput);
        partI(realInput);
        partII(testInput);
        partII(realInput);
    }

    private static void partI(String input) {
        Board initialBoard = parseBoard(input);
        Pos entrance = findEntrance(initialBoard);
        Pos exit = findExit(initialBoard);
        int minutes = breadthFirstSearch(initialBoard, entrance, exit).minute;
        System.out.println(minutes);
    }

    private static void partII(String input) {
        Board initialBoard = parseBoard(input);
        Pos entrance = findEntrance(initialBoard);
        Pos exit = findExit(initialBoard);
        Result toExit = breadthFirstSearch(initialBoard, entrance, exit);
        Result toEntrance = breadthFirstSearch(toExit.board, exit, entrance);
        Result toExitAgain = breadthFirstSearch(toEntrance.board, entrance, exit);
        System.out.println(List.of(toExit.minute, toEntrance.minute, toExitAgain.minute) +
                           " = " + (toExit.minute + toEntrance.minute + toExitAgain.minute));
    }

    record Result(int minute, Board board) {}

    private static Result breadthFirstSearch(Board initialBoard, Pos start, Pos goal) {
        Map<Integer, Board> minuteToBoard = new HashMap<>();
        minuteToBoard.put(0, initialBoard);

        State root = new State(start, 0);
        LinkedList<State> q = new LinkedList<>(List.of(root));
        Set<State> explored = new HashSet<>(List.of(root));

        while (!q.isEmpty()) {
            State v = q.removeFirst();

            if (v.pos.equals(goal)) {
                return new Result(v.minute, minuteToBoard.get(v.minute));
            }

            int nextMinute = v.minute + 1;
            Board nextBoard = minuteToBoard.computeIfAbsent(nextMinute, minute -> nextBoard(minuteToBoard.get(minute - 1)));

            for (Direction d : Direction.all) {
                Pos nextPos = d.move(v.pos);

                if (nextPos.y >= 0 && nextPos.y < nextBoard.height && nextBoard.isEmpty(nextPos)) {
                    State nextState = new State(nextPos, nextMinute);

                    if (explored.add(nextState)) {
                        q.addLast(nextState);
                    }
                }
            }

            Pos nextPos = v.pos;

            if (nextBoard.isEmpty(nextPos)) {
                State nextState = new State(nextPos, nextMinute);

                if (explored.add(nextState)) {
                    q.addLast(nextState);
                }
            }
        }

        throw new IllegalStateException();
    }

    record State(Pos pos, int minute) {}

    private static Board nextBoard(Board board) {
        Map<Pos, List<Direction>> movedBlizzards = new HashMap<>();
        board.blizzards.forEach((pos, dirs) ->
                dirs.forEach(dir -> movedBlizzards.computeIfAbsent(board.move(pos, dir), k -> new ArrayList<>()).add(dir)));
        return new Board(board.width, board.height, board.walls, movedBlizzards);
    }

    private static Pos findEntrance(Board board) {
        return IntStream.range(0, board.width)
                        .mapToObj(x -> new Pos(x, 0))
                        .filter(x -> !board.walls.contains(x))
                        .findFirst()
                        .orElseThrow();
    }

    private static Pos findExit(Board board) {
        return IntStream.range(0, board.width)
                        .mapToObj(x -> new Pos(x, board.height - 1))
                        .filter(x -> !board.walls.contains(x))
                        .findFirst()
                        .orElseThrow();
    }

    private static Board parseBoard(String input) {
        List<String> lines = input.lines().toList();
        int width = lines.get(0).length();
        int height = lines.size();
        Set<Pos> walls = new HashSet<>();
        Map<Pos, List<Direction>> blizzards = new HashMap<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pos pos = new Pos(x, y);

                switch (lines.get(y).charAt(x)) {
                case '#' -> walls.add(pos);
                case '>' -> blizzards.put(pos, List.of(Direction.RIGHT));
                case '<' -> blizzards.put(pos, List.of(Direction.LEFT));
                case '^' -> blizzards.put(pos, List.of(Direction.UP));
                case 'v' -> blizzards.put(pos, List.of(Direction.DOWN));
                }

            }
        }

        return new Board(width, height, walls, blizzards);
    }

    private record Pos(int x, int y) {}

    private enum Direction {
        DOWN(0, 1),
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP(0, -1);

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

        public Direction reverse() {
            return switch (this) {
                case DOWN -> UP;
                case UP -> DOWN;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    record Board(int width, int height, Set<Pos> walls, Map<Pos, List<Direction>> blizzards) {
        boolean isEmpty(Pos pos) {
            return !walls.contains(pos) && !blizzards.containsKey(pos);
        }

        public Pos move(Pos pos, Direction dir) {
            Pos nextPos = dir.move(pos);

            if (walls.contains(nextPos)) {
                dir = dir.reverse();
                nextPos = pos;
                pos = dir.move(pos);

                while (!walls.contains(pos)) {
                    nextPos = pos;
                    pos = dir.move(pos);
                }
            }

            return nextPos;
        }
    }

    private static final String testInput = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#""";

    private static final String realInput = """
            #.####################################################################################################
            #<<<..>>><v<><><v^<><^vv^<<v<^<>>.v^vvvv<v.>v^<<v>>.v>><..<v^>v>.v^v>.><>>^><<.vv<^<>>^v<^.<>>>vvv^v<#
            #>><vv.^v>>v>v.v^^^.><^<>>v.^>v^>v><.^>><>>^^.vvv^^^>^>^<vvv.<^.>.><>v.>vv<v.^>>^<><>><>v<v^.>^^^<.<<#
            #>>v<v>vv<^^^^^<<v><<^v>^<vv^.v>>.>vv^v<^^..v^>><<>^>v>v<^v.><^<^^v><^^vv<.v..<^<.>^>^<^^^><^<v<^>>v>#
            #<v<<<v<.^^^<>v^^^v<v^<<>.v<><v.<<<<><v^>>vv>^>v.^v..<><.<><^.<v^>^<^^<^<v.v.^<v><>v>..>>^^>>^^vv>v>>#
            #>^v<><>vvv>>>v^^<^v<v<<<^<.<v^v^^.vvvvv<>vvvv^>v<v^v><<>...>v<^>^.^^^<><^.v<^v>^<v^^<<v^<^<>vv^>^><>#
            #<<<<><>^<>><v<^^^>^>v<.^<^<.v<.><><<v<^^><v><>^^.^>>>v^<v^><^^.^<<<.v^>^^<^>^><^^.^v><v^>^<vv^v^<<><#
            #>^>v><<<>>.v.>v<^^v<.vvv^vv>>v^v^^><^><>>.>^>^v^v><^v.v<^<v<<v<vv^v<^<<><<v><..^^<.v.v><^<.><>^>>>v<#
            #<^^v<.<.vv>>vv<.<.<>><v^^>vv><v<>v^.v>v<.>v^>>>^.v>^<>v<<^vv>^<v^>^><^<v^^<v><>v.>vv^v<<v.vv^>v^...<#
            #>^v>>><>^.^<v^<^<^<>>v<>^>>v><^<<v^^>>^.^<<v>.^>>^^><^^v<v.v<^v>>v><>^<>>v^v^<>^<^<^v<vv<v<vv<>^>><.#
            #<<>^v<^^^<^<<<.vvv><v><^.<>v>^>v^v.v<<<>.<>vv>v>v>..^.^v<<v<>v^>vv<>v<v>^>>^^<>^.<>^v^<>v^^><<<v^<<<#
            #<.>>>><.>^v<^<>.v>^^<v^v.<.^..v^v>^v.<v^.v.vv.>.<v^....>^>v^v>^^^>v<^<^<^^^>><.^>v<<v^<.<v^<.>>>v^>.#
            #<.>v<v><>>^^><v^<>^>>v>>^<v>.v^><..^vv^>><><.<^<><^^v^<^^>>v<>^<^>v^<^>v<<^>.<>v^<^v><>v^^vv<<vv<>><#
            #><^^.^>>^^vv.vv^v<v<.^<.>v^>>v^^<<<>^<<>^v^v>.^<^.><v>>^v>><v<>.<>.>v<^<^v.<v>^^vv^^>>v.>^.<>.^<^.v>#
            #>v<^^^v^^<^v^>^<^^^.>^^<^.>v<<><>vv<>..^^<^>>>><<>v<<v^^^>v.<>>^<v^>^<<v.^..>^>v^^.<<>><v<.>>v^<^v>.#
            #<><.^<^^^^.><<^v.^<><vv<v<<v<^v^>^<^v^><>vv>v<<v><<<<><<.>^vv<v.^v<^>^<>>>v<vvvv^^<<<^><^v><<vv><>v.#
            #>>v>v<>vvv>^>v<^<>><>v<vv.v<^^^vv>v<vv<>>>v..^.<>vv>^v>>^.<>^>v^.<<<v>v^<v><^^v..><^v<^^^v<.^^^v><^<#
            #.>^.>>^><vvv<^vv^>v^v^<..>>^<^>^^vv>^v<^^..^v^^^<<^<v.<>><<>v<v<^^.>^vv><>v^v^v>v^>v^.v^vv>^><^>>^<>#
            #><<v^<^>.>vv^v><^^^<v^<<>>.v<.^<.v^^v^^^v<v^>v><><v><<v>>>^<v^.^v^v^>>^v.v>><>^<.><.^vv<vvv.>v.vv^v<#
            #<<^v><<^>.vv.>v>^>><^>.<<>v><^>v<v>^<>>>^v<<vvvvv^vv^>>.<>>^><>.>>v^>^v.v^^^^.<^.^vvv<>v<<v>^v^v<.>>#
            #<^^>^>^..v<v^^.^>>v^^>v^.^>vv>vv><.>>><v^<><v>.<^<<<><<<<v>v^.v.^v^>^..^<.vv><<<^v^.>.<<<<v>>^<^vvv>#
            #>>^^<.^^v>^^>^>>>^v^<v...<>v^<^>^>^v^><>.^><v^.<<v.^<><^<>^>^.<v>.^^>^<>.^<^<><<>><>v.^.<>>>>vv^>>.<#
            #><v>.>v>vv<>v^^v>^v><^.<^<<<^v.>v^v<>>v><><>^^.><vvv^.^>>^^<^>^>>^vv<<>>^>>>^<>v>.^^^<<.v<<<v>>><v<>#
            #<^v.v<v>^>v.<<.>vv^<><>>>.<^.><>>^^>v<>..>^<^^v^.<^^.><>v<<^.^vv<>v^^<v>^.v<vv>vv.^v^><<<^<<>v<>^v>>#
            #><v^<>>^^><^>v.><>.^>v>vv>v>>v^>.^><^^.^<v><.<<.>v.>.<><^^^^>^v>^<><<v>v^<<>v<<^<v>v<.<<v>.><^^>^>v>#
            #>^<<..^v>.v<>^.<<>v><.^^^><^><>vv>^.<<<^v^^^>^.<.v><v<<.<<^<<^>>v><<vvv.<>.^vv^^>v.>.v>^><>.<^^<vv^<#
            #>><^^>v><^v^<vvv>^.<.v^>>><<>v<^.v>v><<<>..><.^^.>^^>>vv<<.>^<.^^>v^v>^..v>>>v<><>^.v<.>^>^.>.>^<^<<#
            #>^^<.>vv<>vv>^>.^^>v^^<^<v^<<vv>v<<^vv<<v<<>.v^><v<.>v^>>^v<<v.<^.>^>v^>v<vv<^>^<^v^<<vv^<v>^>v<^.^<#
            #><><v<.<^^v<.v<>^^^.>><^.vvv^<>>v>^^<.v>>.>^<v^><^..<..^<><>.v>^v<<>..vv>vvv^.v>v><<v..^^^<>>><^<>><#
            #<v<>>>v>^v><vv^^>.<><<>>^v>^^><.<<<^.v.<^>>v.^v.<v^^.^.<v^><v<<<>v^><^>v<<^v^^>v><.<^.v<.<>>v<^v^>>>#
            #<.v^vv<<^^..<^<>^><>>vv^^^<.<^<<^<.^>>^^.<><>^>^v>vv>v.^>v><vvv^<<>v>>^^.>>>vv<>^^v<.<><>>.<<<^^>>><#
            #<v<^>^vv^><>>v^<^v<v<.^<^^vvvvv>><^><^>v<>v<^v<v..>>^^><v^<^.<v>>^^.>^.>>.v<v>v^^^^<.vvv<v>^<.v..^<.#
            #>><><^^^^>^^<^^v>>.>><.>><^v^>^^>v>^<v.<^v^<<.<v.>^><^^^^v.>>v<^>>>^>><>^.^<^v><>^<.>>^<v<><vv^v^v>>#
            #.>.v<^<>>v.v>^><^<v><v<^>><^vv>v^<^v><^v>.^v>v^.>..^<><<<><<vv.^<>vv<^<^v^v.^><>v<^^^^^.<<vvvv<>^^v<#
            #<>>^>>>v^^vv>><>vv^.v<>^v^^v>^<v^vv<^^vv>^<v<.>>v^v^>v^<>><<<v.^vv<^><>>v<vv^vv^<<^v^^>.>v>v.^^^<<<>#
            #<<^v^.^v<vv.^>v<v^vvv^<v.<.>>^<^<<<<^<<<.><><v>^><<<<vv^v.vv^>>^>^>v<^<^^>v<>^^>vv>^v..v^v^<<v<<<v<.#
            ####################################################################################################.#""";
}
