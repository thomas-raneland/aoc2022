import java.util.LinkedList;
import java.util.List;

public class Day25 {
    public static void main(String... args) {
        partI(testInput);
        partI(realInput);
    }

    private static void partI(String input) {
        long sum = 0;

        for (String line : input.lines().toList()) {
            Number number = Number.parse(line);
            sum += number.toDecimal();
        }

        System.out.println("Decimal sum: " + sum);
        System.out.println("SNAFU sum: " + Number.fromDecimal(sum));
    }

    private record Number(List<Digit> digits) {
        static Number parse(String s) {
            return new Number(s.chars().mapToObj(c -> Digit.find((char) c)).toList());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            digits.forEach(d -> sb.append(d.c));
            return sb.toString();
        }

        long toDecimal() {
            long total = 0;

            for (int i = 0; i < digits.size(); i++) {
                total += Math.pow(5, digits.size() - i - 1) * digits.get(i).value;
            }

            return total;
        }

        static Number fromDecimal(final long value) {
            LinkedList<Digit> digits = new LinkedList<>();
            long left = value;

            while (left != 0) {
                Digit digit = switch ((int) (left % 5)) {
                    case 0 -> Digit.ZERO;
                    case 1 -> Digit.ONE;
                    case 2 -> Digit.TWO;
                    case 3 -> Digit.DOUBLE_MINUS;
                    case 4 -> Digit.MINUS;
                    default -> throw new IllegalStateException();
                };

                digits.addFirst(digit);
                left /= 5;

                if (digit.value < 0) {
                    left++;
                }
            }

            return new Number(digits);
        }
    }

    enum Digit {
        DOUBLE_MINUS('=', -2),
        MINUS('-', -1),
        ZERO('0', 0),
        ONE('1', 1),
        TWO('2', 2);

        private final char c;
        private final int value;

        Digit(char c, int value) {
            this.c = c;
            this.value = value;
        }

        public static Digit find(char c) {
            for (Digit d : values()) {
                if (d.c == c) {
                    return d;
                }
            }

            throw new IllegalArgumentException();
        }
    }

    private static final String testInput = """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122""";

    private static final String realInput = """
            2==-1222101=0=
            12200
            1=1
            1=20-212
            100
            1202=0==111--2
            101-0--0-1
            1=0--=2=1--
            10=0=02
            1102-1
            2110
            20-12-
            1110=-12-0=-0-01-0
            1-11===212=102---
            1=22=00201122-1
            10=02=11-212=-0=00
            2020222-211=202
            12=0-00
            1=2--2--01--=-1010
            1=011--0=002
            1-212--=-
            1020==01--1---0
            11==0==2-2=011-=
            20=000-1--=-00
            2202=21-1
            2--10=12--
            1000
            111022
            2-022
            1=20==01=2-=
            1-10=--
            2=2=21-=11-1--===20
            1==02100
            1-=0
            2=021-0122=
            1-0==
            1220-0-0=2
            1=0-0=2-=1
            10=112201=102
            12=0-2-112
            1==22102-2
            112-0=02=100
            1-020-
            1001-20-
            20-2=1=0110
            10=-==-
            1-12
            20-10==-
            1=1111022202=2101
            1==1=1-=
            10100
            2=21--1=11=
            22002-021=02
            22
            1=101-0010111---2===
            1==20
            1=2211001=-2001=-0
            210==-=222-1
            1=2-0=0
            1-=12-2
            20-1-0=---
            220==121=
            1-1110=2=-0-
            1012-01=0002
            102=121-0-002==-
            2200=2-120212
            1=1=22--2
            1=2=220-0-22=01
            2101-10-1-21
            2-0-1=--2==-01-2
            1000-=0210=--
            1==11=-=1=0-1
            1=0020-12-=-
            1-220--1=-10-202-
            1=-1===-2==02==-=21
            21
            2=2==-0-=2=-1-11=
            1-0==20011201
            1-100=
            21=212-022=2-
            2-10110=-0-2=1=-0=
            1012
            1==1==
            1-11=0
            1-20-0=-21111202==
            1=0=012=-=2=10-
            1=2=1
            10-0
            100-22==02
            1===121-0=0===1-2=2
            1-=-1-10222=12-
            1==-0--0=2=2-2
            10=2=11220021-=
            211
            202-0=11-===
            1==0
            1--0221-0
            1=-=000-2=101-0
            1-
            1=02=2
            1==
            1---==1--
            20=0
            22-=1-011=2
            12-112=
            102==-=-021021=-=-2
            1--2=2=100=20
            12--10=0=2==2==2
            1221-=2112-211
            1222=12-110
            2=--11
            2-02
            20222=-1222==
            20-==-
            2=-021
            111-110-1=
            10200=2010110
            21==0=
            101
            1=-=-=1=1
            21002010-20=-
            1-122=02=2=2022=1
            112--=221
            1210
            2=102=01102221-2
            1=-1102111
            2=-
            2110=0==2-11001
            1-00021100=-011
            11
            1=-
            10000=00-=2=1-0
            10=""";
}
