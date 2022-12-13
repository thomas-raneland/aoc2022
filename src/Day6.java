import java.util.List;

public class Day6 {
    public static void main(String... args) {
        a();
        System.out.println("------------------");
        b();
    }

    private static void a() {
        for (String in : List.of(input)) {
            findSequenceOfUniqueCharacters(in, 4);
        }
    }

    private static void b() {
        for (String in : List.of(input)) {
            findSequenceOfUniqueCharacters(in, 14);
        }
    }

    private static void findSequenceOfUniqueCharacters(String in, int i2) {
        System.out.println(in);

        for (int i = i2; i < in.length(); i++) {
            if (in.substring(i - i2, i).chars().distinct().count() == i2) {
                System.out.println(i);
                break;
            }
        }
    }

    private static final String[] testInputA = {
            "bvwbjplbgvbhsrlpgdmjqwftvncz",
            "nppdvjthqldpwncqszvftbrmjlhg",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
    };

    private static final String[] testInputB = {
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
            "bvwbjplbgvbhsrlpgdmjqwftvncz",
            "nppdvjthqldpwncqszvftbrmjlhg",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
    };

    private static final String input =
            "rhghwwsmsgmgsmmmlzljllrddvsvhvhhnvvrcccwhhvgghchwhvwvcvrrrgtgrgdggfdgffshsllvvslsppglgvgwgswwcbcwbwrrbjbwjjtgjjdzdfdhfddrmmhqhpqhhqghhzssqzqccbwwffzvzffvtftddrbrtrrcjrrmmbmrrrlrppplmplmppfzpfpvpqvpqqdndpndnmnwmwjmwmwbbrhhmzmffwfqwfqwffppvfpvffnwwwwcbwbhbccvmmplmplmmlmplpprbpbllmhlhzhffngfnntrrsrprrvlllvjvmjvvppsrrfnrfnrfrfwrffrsfrflfltfllpfprrthhhnhqnnfwwcttvzzddqsddwpddnrnttvjttjjdjvdjvjnvnrvvchhschhlffjggtqgqnnvrnrsnrsnsvspsnncppvwvvtmmcqmcmscmsmwmvmpvvlqqmbqqlwwtgtztgttpjjbfjjzhzthhzssffwqqdmdcdlclrclrccbmmqjqmqcmmnnsvnntpphhdpdhhtbtdtffhzfhhpwhppwbppfspphdhggzqgzzffdsdfsszcscpscsqcssffhjhnhhlqqdbqqdmdsmddrfddndldgglvglvlggqlggjngjnnbffthhjbjpjnjmnmgmqqbjqjzjqzzzjffwttcnnzffmllcncmnnthnncttpgpnnjdjfftmmvqmqsqggfrfjjqbjbvvshvhphfhhljlfjjvcclzclctcmmfwfvwfvwwjvwvjjplpbpqqdvqqbhbdbnnsndssgffgsglssmfmjjfrjfrfttljldjdffjjdnjjlbbnrntrtfrttmhhndnsdsffgqqrpplvvfwvfvsfszzfzhfzzlttlctllrzrggvwvhhgnnlmnlmlpppdldqqdpdlplrpppjspspzzhphccgjjpmmwvwbwssnpssrzrwrhhmzhmhsmhmttrqrvqvcclggptpjpzpccltctppflfjffrtrjrhjhffhjfjbfjfqfqzffdtthwwjwzjzggsgzgqqjdqjddvgvsvnnqwqgwgtttfvfrfwrfwfwmwmqwmmmbzmzvzfvfrfvvgvtvptpbpwbwrbwrwffzzfgzfgfcfzcffjdjjvrvnnfmmtztffsggjcjzcjcssscnscnssmmwgwbbljlnjndnrrqwqjjjbnnmbmcmbcmbbvvmtvmmfcmcjcdcnnzfzfzqfzfvvmbvmmndnbnsnnlddqvvsnngvnnrttfbtbqqnwqqzfzfzccfmmgrrsbbhvvgjjhfhssjmmsjjzzbqblqlfqllwhlwwvvstvtfvttnncjjzcjcjrrsvrssmfsfczbrzrvscvmcmrjpzwhcqfrrzbljnmqlzbzqtmhrshlrjjpvhnsvtlhqggqwppsjpszmqwfqmlwbqzwcrggrvfbvztnflwvbrqcrqbcllswvsvhwjzpldgphwptfdnlgdlnbttjfzrdcfvpdhlssfsljvdjmwddbnrpqnnqlfdfdspbnjwqjwrgtnrftsqcfjpmqwgwhttggjwzvgbwlhmtmmjlwhssrgshzpbcnstzlqdshdhjfgqlsmqqhpwbscsjhfbhprvhmftqngjgdbcvfldqgqsjqjfdmcvsflwzflsjfssnjpbwffnsfcnrdsphbjpgghmcthgnzmpgppqjdvbztvhnwqzndntcpjdtwwhvsmgdcthpssszrqcbntgsznpghmbqddpqscntjprlwzhbzhjtwzbwwcldwdgsttzmtnstjnngzrgvhncdbgqnllfzbthldztsdwsngjzprbvrnbzrsghlgssbqfnbvhnhzwmmmtncvsdngdtwcbjlnnzbnlrnmrvnvsjnvzdqnggmsvljlvjznwdszcmblhrsjvczpnlhsmqjsmwhbjbplbtqsgqjdllhncwdgbvzwnmqvndcbfhnvtjnzmvjhwzvdldhgfwbqqzcnbflfnwntlhmqgdhmrgwqcpmsvfbmwhbtsbdlhcnbcbswvdfffgjvddrbpzpcwsrsjnfvmzhlvbdnwttnqrmzbnnntbfvptrlhjhwjcsbnhvtwtwzvgfnzjplthqjbsjjwzdtqqvblnbvgcmvrmnvmwfrhcqgvrcjlfzdlpbfvncbtfgvnsflbjzqqhczcmtbwqmrppmptfgzvfbmcslwlfrfpvvnvvnwfvvmmdzmmtjsgqdfhngtphtlfjqrtljgnthgnbbqfrnpfmpwhpzdvzmtswwdvcnpsdcqwjdwlvbsbmlwdsjbcbgcrfljshlvpngfmsrzlfhtfqgwbcctnzzhnqhdmqzdwthftwtmpbcmqvdcdtgvltbzmszzwwmhzlfvbdqnhjqgdmstsnhftcwzvvbmnhwvgqzscwcdjbdgfmvpjdzctwqwltbwjlgcblnnhpnmggbmvqpqtgqjzspgqzvcvsdbvjgjfzdzhfpbzjqljjcgldzgnlmtjcmfgdbqgglvjqrppwmhccvqzvsrjjvfhjprwdsqsnszfprznljtcsrtqhcrpljfrccflmbpvqtzgmzhjrlbnrmmsmmjbtzwpglqgdvvvjvnfzmplsmvlvcnjshvjwntclwgpznnzwhjssgdcjbzrmsgnfgcgphrhfvrfhzwdcvsplhbmqwhpmjvqlmschznbqblvhtqfgtdggmncndhhplnzjphccjmlmtdqnmnlnpnfqdstljqnsqbrjrtspvrwvdmwzlgdmsfvctzgtmgqhqqrzpbgplzcfdqnzhsqrbcvhsccshnnpvvrpvqzqsgzgmpzfvvrrcvhdtntnsqnjrbzlbzmpgwdqzbhtlrrhbwdqjlsfgdhmvgmgbqhwvljmmqfllqvvrznrlftgzjdcgtstjffqmgvffpvtctzpdqjfnmlcdzscntctmqhrtmhrlhbjzttrcvcnlhsrvtpbmdchhntnnpnzlvqqnsrjcmblcvphqgwshnjmplgvnbsmmdzgqcpqztjhhgjvtlbpdpdwlwmmfrdgcvzfbgvbgpbjnwsssvhszwplcgvpgjwdrwngbcdjwvlsfhqlrqzgrzpfgjstqfdbrpqdvrlgdwqcsrgvhctznjjlzsmzctsqtfnhhlpjgnltssglmlwshfbrgmjqbvsmqwvszdfsvhmtrfjgwjctpsmgzzjbpwsztnnvzrhwvvmhdpgdmwzsjprhlgzcdvhznlfgjqvcwqrplcfvzmthsdsnrtfvnlrmvwplmbdvdggmlvpgdgzhvzmvzwmptzsnfrcrjspccmqjpjmhjqgrjbdcdjbzjmphmcdvjqtmdshhjrqgjgsnpzfbfgpjpczwzvmclgzgztlvzmdbwgncnndjwhhhjnhtjdmcnrmnqbmjdrdcmtvcsmftqcfhsvhsfjmtzjpnwffggpfqlqmzlbhnnhbtgzfgnjvdzmvthqjrhzbwvhcjzcsmsvsctrqbltpcrpjjnsbjdbfjqfcbpcgcwtqsflmlwprjcwlmcjjgsfdpwcqvhjpsgvdgsfnscnbzsrmrbbvdrlltzplbvgqsdnplcvbhddbtmwnfmvqhqdlrtrmrmzmhlccgwgmbdppjqdjtwmvdzfsbsggrfstjwjpjnljffwffmqncfnthnhglwvsgvzmgbzhtdfpfmdwmcldthvsnqnptpmhqctblgfsszhcfbvcrggjdhthqvvvlldshvqwmvdtfslrhzvgdfwztrczdjgcfcgtmwnphqthlgpfnrqwcgpzwnlgdvsnvzftlnlfflfsmjzhrhqjctsbvtccwbfsdrnbhszzjhqndvwcsmffnstnfdfwpbgfztjmjngdczzlgpscjtshpmmmzlnqndsttbdgfjqcvbqlphwhlhgcvjbhjmtrfzlgpwdnvzrllndbhvhlngvhlszzdcrdgvrmjwcvhhtbhnjmdzgctqnpdlrnqjzbchjtcsggsczlgmvtqvzmsqvtrhtvdmzlcdddfnbvbsnrzvgzfqjtbhjqhdznrhbfbqwtnwvrfqsznbqfzfzfgmhvjjsgbbdbdtzswwlnfrq";

}
