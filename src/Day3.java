import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day3 {
    public static void main(String... args) {
        a();
        b();
    }

    private static void a() {
        int sumOfPriorities = Arrays.stream(input.split("\n"))
              .mapToInt(Day3::priority)
              .sum();

        System.out.println(sumOfPriorities);
    }

    private static void b() {
        var lines = Arrays.asList(input.split("\n"));

        int sumOfPriorities = 0;

        for (int i = 0; i < lines.size(); i += 3) {
            String x = lines.get(i);
            String y = lines.get(i+1);
            String z = lines.get(i+2);
            char badge = badge(x,y,z);
            sumOfPriorities += 1 + priorities.indexOf(badge);
        }

        System.out.println(sumOfPriorities);

    }

    private static char badge(String x, String y, String z) {
        Set<Character> sb = new HashSet<>();

        for (int i = 0; i < x.length(); i++) {
            if (y.indexOf(x.charAt(i))!=-1 && z.indexOf(x.charAt(i))!=-1) {
                sb.add(x.charAt(i));
            }
        }

        if (sb.size() == 1) {
            return sb.iterator().next();
        }

        throw new RuntimeException();
    }

    private static int priority(String line) {
        int mid = line.length()/2;
        if (mid+mid != line.length()) {
            throw new IllegalArgumentException();
        }
        String a = line.substring(0,mid);
        String b = line.substring(mid);
        for (int i = 0; i < mid; i++) {
            if (b.indexOf(a.charAt(i)) != -1) {
                return 1 + priorities.indexOf(a.charAt(i));
            }
        }
        throw new RuntimeException();
    }

    private static final String priorities = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String testInput = """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw""";

    private static final String input = """
            FzQrhQpJtJMFzlpplrTWjTnTTrjVsVvvTnTs
            mScqSqqgcfPCqGPZcfGNSvTNsVVNSjNvWSNsNz
            fPcPGqgCcHgFzQpJJtHtJH
            DZDqqlrjplDHrNCmnBcHBMCRcJzb
            RQFLStFvdcBbzdJbJM
            PThQtwftTPFvtTPhvtFtfFtpZZllwjRNlsqNqqZjwpGlrZ
            pPwtqgwJZPJLgQqSFlqhFFlqMd
            DBmCWBBDWTRGvcVRTCCnnfQlFSdlzfhfdMWQfjhhQz
            drmBVVCRgprPtrZp
            HznjQjvmzDMVrQnMLJMMlfWgPSlJGWWJPl
            BdcqqhcdBRpFhhZBthhctdJSJJWfgGFlJCSFgbWPCDJS
            NdRTZdNqBwqtthpRBTTRqdtZrsLQVzrrzjzDwDsnmrQrnsrr
            HZFZCFzZWszqsRTBZTNMhmthVTmhDppmMQVPpm
            wjvSbJddvrvlrvnJSJJvlJmhPlhVPVtGVpQDBVMpphQP
            frbrfrcvvnvjfwbcJgrrCBRsCFsNzRgRCHCqssRH
            dDFNqNqZqPLNqvqTTvCLSPdZssGHClJQJcRHJGHHcHBcsMsQ
            lrjmWgWWrhjgrppQHHMQrsQRJGcBJc
            lVlmnwjmdTTSvVFN
            FWNFHvQPmLGwwwSHtswwln
            RfMJcDdfdcfdddfZjdchrtZmSmCZVtqVnZmrnrtC
            JMmJcfjjphcghpgjhRGzGzBBGPFGNBvPTpFL
            cVPVwStmmcQPBQPpSCppwhHZNNqHszNBhsNRNjqHzj
            MfWdDgvdbnvgMTWgvgZfzmsZJHzNhqjqjRhJ
            MDWMWGndMgFDnFLDwQrPPCSrCSVrlmGS
            QLZmPdRdWmMsMDWZmsLWWrhMHcHGzHvGzFcvrvzNrc
            tplSbLVBlvHHcFNnSr
            VqfgwLlCJWmWQTfW
            nRWvlvRbtLvdMCPFGL
            wrfsJNNGhNzGrTgDMDLgPMLPfq
            wcVhJQhwhrrBpmVblBRGSG
            HHHcggrZLcQQcQll
            GzfzTRTzmmFMwSNSwdSJQtNLNB
            TGbmLMFTzVVVTMzmFMfFPMHPZhnjZCpHnhgnZnPWCPZZ
            MRwwpVMHRspqVqwmccDlDrcHBBZgBl
            jQfQQQjWWFBgmcgDfcZg
            hvvSQzSnQQSWWQWSjTZVTRMshwVCssppwV
            pvrTvCvtFppCHMMZcdDFdcZM
            wLjTQnqljjSnlwjqjRgLcHHHMBDMZhBMHgHcbBDh
            mqjqlSNqRqwSRrWCvzGmtfTfzs
            TWScDCqCQQVBWDqWHsHswwBgRJzRhhHp
            dPttGrvFfGjMjnjvshsJgsJLgghRgH
            rFMlGdtjPffNnnrffSNcVCDqQqCQRqQRRN
            GmBRbVpPbmJcwggBBgWW
            LjsTCNNtddjHqLLgWwccqgfq
            nsjNjntNtjHCsDwZmwZZVmmGSvSD
            bwDDgNFtMMDbFsMbFwWWVcRcSpcgjgQWhWSp
            lfTJJlvdfCffccWppRjRlcSc
            RnzGdJJmsMNnMFtM
            bsBTFsqqTTmFZTsQBWWznWCRshlJNJlCVh
            GjGnDvDjvjPppHwwpwgrPPClJhNVRCzhhzJWlWlhNlvJ
            ffdgLrgdLrDjdfHPbbZbttcBbcbLmntn
            TNTwwvTTHNtTHNLLVqtqTSZBJnrnhhbrFJjZjnVZgghF
            cplWfRlzcWfRCZZhFrGjBfjZjn
            pddzDsRpDcclzCQMWBvNSmTTSqdvPPvqwqtT
            DQTttwwLtQtVSDMJDRmmSS
            ffsWfvrBWrPvwJhPhPSMPMVn
            WsvsggFvwNLgHtNQ
            llBbVDMTlFVdFDTbVggSVsqZqZZZqqvNJZJRNRWgtv
            HhpjcHHvjPsqCsWcNcsq
            GfpvnPvwFDTTFFDw
            GMmFGMGFFgVwQHQwwM
            cJtZNtZTbThcZtcZJJtTZWJPllgNgpPvVgpjHvQpRpHQNg
            hWcJZcnhcJznbcBZLqSLDfCmHqnqCLsD
            zQpjLpnhnsHTnlQLrMCCHPFrvvCMPcHm
            ZfgdSBtNqBwlgSDfZDwtqSFvJCvrPrVvFmwCJFvrmmFV
            dfbRNZBqDtgRNBNNNljLLjhGRGGWGLGTRhjz
            hhrnfBzhtzZgDgDnBfrfDZsRpMNCNNWjwCCfGQGGNGCGQC
            lcdPmHLSPDSdFDpQMLjCQQQCRGpN
            lJSSbmPdVdVvdHbvSDFHHPlZqgBnttzgTsssTrqgbZbsTT
            FsdsShrgggLDdbSDsgrGrlWHTpfRpTjjfFTzRTRjBWWp
            mPvqCmJCqJNnPvPNPCvvLTTVjHjzNWHHTWRBRVTWVz
            wJLvqPZmJtccncvZmJqqrghDGQwbdSGdsgGgQgQr
            zFwtNJGtNFlpnwHccZjZbcpprsmc
            PWQfBWhBgQgTWQRLThBqMSVDSbbDRsVDmsmZsSZDjr
            fvQfWBfLqfTqhLhCvNFttJlCwGrrCC
            fNrGLNrfNrGjllRRRPmWVL
            tbJdcFbSSssZSmmpFcsSbwDWVWBlllVPDnnjBFjDRnBF
            ZZJcvZctgNmmvMGhQm
            HhhjFRhgrcRTFLvWVJVQWJVHDHQJPP
            GwCmwBfGzfSCzCfwtmtzzJVWSVJJZrbWQQQqJJDZVJ
            mtfzpGdststtBmfmCwrGRFcTcvjngjFnRcLnpLLn
            rrwjdwLgVmVwHrfPCJPQBCBGmPtt
            ccNZqbNnMMblNpTlNpnhhBPSJsQhJtJtChPJqS
            vTWvNcWNWTFvnnvcgjzDLVQLgHVwWDrW
            jNPgbNHbfLJgLzfz
            ShvhhFVVDShFVqMSSSvZfffvPLtBBBBJJlpfLJJv
            DqhnShhMnZZwCSDCMhChrRnNrNdNQbHNNPmjmdHN
            VQVZGQFnzFTSsBfgzgfs
            rjlpjtDrtMLZPMtPtpPZPwCsgSHgMHCCmCTWsgBWSBmg
            pjvDqLwrlDtwqtqNLvtjpPPwRNbQRncQVQddZhRhJQbJncbG
            PsBSqnSdQsFhmmmnppFc
            TRhNvrTCvNTHVcfHbJVTpc
            rhtWvGWLrjRqdSqqLLqdld
            vPhfqPJvrMrnffDDhvpMjdzGMLdLLQpllLGQ
            mbmcFSScGbSCcQlzwQQlclsg
            BSGVCmCTZWCGGvnvfZHqqrDhHN
            GSRfrzGRhzsGChjTBBlqBgjgCTCn
            wHQwtDVDHwHHDJcDWJZwzHZBqTnnBFlvjFgBqnljjvBdBZ
            JNmVJpVmNtDHJWHrbfPLhbGhrzRbpr
            WcWcbzNPbDwBNvWBwRMPQmJZQRQZftRZGP
            LhVHFgggTHCFHhfMQQSMMGQRMLLM
            qnrqppFVHphqfDsNbzjrzbrN
            cwgDrdLSrBrvvhDzCljjTW
            VHtVZpspQtMQsVRQppFVQVHtCdPTPTzdjvhTzTTPRvjjvWhn
            QQZpMdJsQFJHtMHdScwLwLJGrSScSwqw
            ZsjNflGfRfRPrZNRFcffLwJdwcLdDBnwzzzDznVn
            CTGvhhTqbtbgTqLJWdDntzWWdnLw
            phCMgmQGvvHCvMhbTQQFsNsNFPZSfZjffmNsll
            CNpCJHLNhhSSHZPgrFlFFWgpFpmzjj
            qQttDVDwQGdQGvqDQfwbcVrrlljjzzmzrVJgrr
            nvMDsqqqQvfvsqDnRSZHJPPZHhLHLS
            RNNrrPfDNRQwQhjscghMqs
            WVZlHvnZqtlLVLvwjwhsggTstMhwTw
            vGHWLJlVWlmLVqRCGCFFNfqqGf
            MNzqCnvqvqvCVLBvvCVCpVcRssncrPSTWGrPSPdGTcrP
            hmHwFmQjFlhtZmHwtZjjddSSGcsdPrrGcQQQRGPW
            fHbbFjlhZwmtwhfjmmwmmLbpLqzqvBzLzCvLNRMbNB
            tQfLrtQPrrfDSSCVlDfLSrmbBjGvWjjLmWWWpWNNppmv
            wdHhRTTndnRThdvnBFGpNBMnpvvp
            JdqTHTHHRdqzsJRRzTRHscJdDSGCfDlqQZqlfZrZZCffqSSQ
            hQMWLsgGJMMhsCHggQWhgspDWFPzZvPvptDvzvmtdtdF
            BrBlrTBrNRbfnjNQlZDztPvpmpppmzvfdd
            jQlQlqQVbVcsMgMgChhJVs
            MtFMCTWRFRRtCRTTRTMGJddjLdstHvBzBHzHVVpL
            lZSDnbDlnZPrbHpzJJsdSVJpBL
            nNghhPrlZlgDTFhCfMFJRMQF
            RGpPFZPRQZPFRGvpPQPpjvpmhnnCMjhmhgBgVgMVWBVgVM
            wLtfNdNHmrNthCBgCbhnngWd
            srSfwHfszsNmtswlrqQDGQFDRPJGDvzRppRJ
            GVFFGvVWZLFsmssFRNfVvmGGJPpJTTqDBvTpqlpDvqbBtTPl
            gQhzzChzrMQhjpzlzWzJpPpBJb
            ghgWjcCjMgCHWdQMhdjChCmfwmRRGZZGVHLZHRfmNwVs
            DnDVhdnrfSfpcGGjQQGdJddJ
            bPWPRbRsRMsHNzDqTZcGBcqZqmmN
            HvwPvvzMPwDCChDVwS
            vTCCvTfWFDTtRPMvfWFlDFHBqGLpLzbwBgWwqzGqbBbB
            cQcSNchSJSZShVJNnZrhSqBpgwGHHtGwqtbwLbqpbr
            JNnJVsJscNstNhQsjnVVNlFfMmTMFfCTfjFvfPRPPF
            VLFBsgffNFNqRvbz
            ChltjTdjDhHpHZvdpjjZhwCpbNrbSzzbrNGMTMMNSMbWWNSN
            vQjpttQhHnLsBQVLsQ
            mbzQgTzRVVbsVdQgzzVRddmztFGWNGNNWnGtFSGBsrCNWCrC
            jfJjvPPwLDcHDPvDDPDppLCWCFBGWntCBnrtFcrFWTGn
            wpJPLjvpTTDpwhfgzmVMbqhdhVRgzl
            PlcqbWClLmnqZVLq
            THwdrrhddhhfJJhwLJhpQnDVnznnmZQQnSpfpD
            vrFdvGsGHhhhwHjFGrFGJHdMCCcNgbWMPccRRccMFLNPPP
            tbppJqcNtJnZzRJbPFsFPHfZrrshFDjj
            GdwgwlLgGCndsDFrhDHHFF
            SSlLnmmvqWNqmcqb
            ZPFPPTZpZSWzCMMSzPBsFvhtlQvJQQtJhsVs
            dmNbmgbrwDNmbcDgwNdcwdLsnhlJlnvtsBJnhVQqqnstLB
            bNGfDGgHHVwbwNwVfgmRMzCzzCSHjSRZSZCTRS
            dDTffQdqQQLBLnVLLQvL
            rrBHZZcgJcrLvNLtLgRLbN
            cjjJhrFlhZwFFzwJzmTBBdmTsDPzDsBP
            ClGrJJMNCrGQqlcPvWgnDP
            ZBvbjHpSwBVVVcWjjjqQ
            BLSbbwsHSTBHwmLHHLbBsSTFdrfvCrtmdzfGJzrdzGJddGfh
            gljWRwmSjtJWjJtJjgjSZfVSTVVHGZSVHcVchZ
            pBzLFQpPsFBGcGBTThfB
            pFpQzFLPLpvQFQnLbsqqGddgjbmwRldwtWmlGWwj
            PDQDMFQBMfWPvjdLLndLjrmsMj
            qZqVzTRRqHtvZGGtVqTTzVjLLsrmJCddnLjrjHsrhdCr
            GzwcZtqNzqvNqwzZVGRwSzbpWfFbWPlWFpNDBfQfFNNf
            dfRszdzVdsjwdhLwCCqwGllHvPGPwG
            SpJtBLFgcGqHQClqZF
            JrttrtcTmSSLrmtBTrNgnBJjbNhhbhzRdsVdMhNjhMMhVd
            MPFSCfSMqVSBGrtzlvccfQctzbzl
            hZNjTHWWTZwshbLvmlWpBzmbmm
            dRTTJNDNhjsJqBBMMgrJPVVr
            WnVzDMjlDVWwwHgwhmgNhNNsJh
            qfvrLNCcbLdvpcvbrPPqCsGhSJGTTBspTshBpTBBms
            ZLvvZfrPfPCLbCFFzjVQzRnNNMVzDQ
            nllbFTTpTFTBcnCjQPqQdZRQZhCb
            tvWszrrztvSmzQQvrDmZRjjjPPDVqPRdZRdCPd
            gfzvSsftgQHQHgQl
            GVbHRRGRLpdmGWTm
            gSPPltPlrlvccFccPlcJNCTpnnmpMCLMMmWfdRmMSS
            FzNJRhhvPFRvQwzqjqzBHZZj
            PhZSpFBPBFsNmjBVllltBj
            JMGLnrrnbfffrdqRqPHnnqLDVTDDjgmRgwtmjDljlDVlwl
            LHMqPqPnnqGLWJPMnndrGfSWppzvvFSChFFFvvzQSQZz
            RSWWssbvnnCqZnWsRCnssWrTggNhgbNHBgQjhhQBgjNT
            mcpzcppzczcDGVcPcDLLGLjmrMNTNtQNHhMHrQBQNTgN
            LVpPfcjjWvsFFnFf
            MpddpdCpJdJlbdMvBHMnnsHqSRvG
            PWvZfFmZrrfmwWwFznBnqRRSGcsBVmVBRG
            zjzzhQPQvzjLPQzwffrwrtlTCDtJDlgJLltpTTJlTl
            TvTWjjzpznGttFFZccrrPrSZllcB
            gNNSqHMqsMHQJHNZCDDCZDqLZdlZBD
            SMQNSRNbRRHwhwhsRmtnvWVmmnbGnjmpGn
            ccSVQjCQddTsFJcH
            gLppBfgfmvCRFdsddTJJgb
            WMLMmWGGBZWZLCtvDhlSSDGlwhSPSzSP
            TpqVGVHFQGmqSqPZdccNCzzhdwCjNG
            fffbbvftMrBMDDcCccCZCjlvhCCd
            RLWMnbftDhnMRtfBftRJMtLMgFgHmmpmPmSmmQFPPLHHVTQS
            nRvwQSDNcpVJJcJR
            qZMjBhjhZMMBzLBGLGrjJbTPVTpbdPPdVbVb
            ZZpmFFZlfGqfmmGMzlfmMmnWQDtHtSvnWWNSHSSstFtS
            bFDGZjGDbbRSgLtN
            CphJVfJWCTBgvfLHNRcwnt
            WVhPWBTzzChzhhhBmrpPPCJZDQtdMlrjFQdrFqsjdrQsFG
            ZBpVQHHVMMWWdmmLWw
            lQhhrjcRttrqbvQLNwdDWzmNSDmStz
            QbGqhcbvcsqvCCHnsCZHCnTn
            tlWtQTTTJjTQtVnmrbnPWVShVC
            MDMGGzsHcwFgGZBqrmmPSnbqVmNVGC
            sZFPwHcMZDBRTlvQQJttTQTR
            FhVRfGptMGMnZhRFBNRBCCNHHNvTNTRC
            zmwrLLSjrbzmNlcvvrHvDPCN
            JLwjQdSbjdbSdqJQFGVqFVMgnGHMfGVV
            fffZWrJqZSHWTWHqSvrgDhggzRjttsDhpDgs
            PGlBLcBBbnnbLLFbGLBjRgjFTFVzshtzpgsppz
            TGCPnMPQlGnPmclPlnnQmbmHJvNvfHdqwddwvvZfCNHCfW
            ClLwpspTPrTFZCdzFbZdbQ
            RRMWfRgWVRMRQBZZScVczVGFbjNb
            MfnvMqWmslvDhQPw
            hdndSdqsTddBhdcmmNHFDcqHttPF
            JjMzzMZQGwZGZJzMzZJQzGJFvPvNPtFmvmNmDvcFtvDHMv
            gZwzQwJfGVJQJbGLBsSTSTdTbCWDBSnd
            ZZCHZRzMZGRMhMMVVFNThrdd
            SgsccSPmmgqssSlqsgcmscSqlhpFdVThjphNrdrhjdwdhFJN
            vmttqTcqvLqqmPccmqSBbRWnWzQZZZZBHnQCzHDH
            GgPnGdSPBpGsLTBL
            rVNJjmwZqtZZshltFTtvRFsL
            mqmWrZVqWjrqZMNwPMQQbsddgdsbsgPz
            LZLVvjZrggHLJggSZDgrnPnQnRnppVRllntRdPFz
            chMCzbqGmhNhhbBCMBdFnpfqFnltRRQnlPpQ
            TChmWcMMTmBswJzZZrWrvzgg
            gngRNBNRBsNFFBgfgbLLLnqdSLvLTcbLbd
            GWtlChlVMllcZSDWSLbdZL
            lljjGlhMGrGJpsFdRJfsfzfz
            jVTdrnGQcQtTTTFQqBqsgHHFgsqf
            ZZLbPLzDzPZCmsgqsBHt
            wDzDlPblRDPLPvhvwtdnnhdrnrMGWMVGMThj
            spjjpjvjpjmQjrpCMfSlfzrPBl
            dHFntHWnnbRVFtnbcqHFzBCCCPzfPMlcCSlgllzc
            RLbVWHnnSWtnHFbdbVRdNNtQsjsQTjDLwmGTmTssQwmLGJ
            JbJJSLMhRMSLhNqqwFDwFNcFqL
            GcpnGnznnpzpzGpffNTNTwTfwdDNNdTFdD
            nllnlPGWQWHcGpzzQGGzGvHGJbVVtJSChQVbmtmVJrmrmbRm
            GFsFrzwrflmtdtbltG
            ggLPDngCJncNLJRDwgnllmJqjWMjhjhjWWmWjj
            nBNRNPgpRgDLTgNwfsSHVBQHVHwsZr
            WwvnvWvcFtwtSFSF
            zBZZZRQSzMBSgSVJGjGTPTGFzCzmmj
            fZDrpZZfRfMgSQDDBhgQghDHsnbrcNlWnnLWHLrHsWnllc
            ZVncdPPwVPdhZngnqHWHNNvTHvlMvn
            fSLjjLSGGBjTTHqvBqrMNT
            RSSSDGRtSGZthTTctmtg
            rtzrfJbgJHRfGRZLPR
            hdVhlllmFlFPLwHmsRGGZP
            nTWhRjTBTWlvNQgnJSSbrJtz
            JgVTpBpfvgpTDDJFJvTgggtlFlNNMRLNNzNNZRNHMRCLlF
            wbPWcSGbGqWDlnNWMMMCLMWZ
            wrsGcbrcbcqwDwbcmGvQBQgTTsdVJgJsVdQf
            mztrhgJtDrhgcrZmnhbnzbhcMTMPlBCPBGVGTMVGslCCPGDs
            FLRQmjjFSQpQwLlPsMsCpvslvPCB
            fNLLwSdSwWSWjwmrtczZhhrJzdzh
            HHwCwJFmHZttZCfCSffSMHcVDMcPBRPcPRDhPghM
            nvQLsTnLslnLvpzGTssnsRPDMhPgVPVgtcVMRPgVQQ
            vnsTGWlTLsWTLLvNsGWlsZrwmZCJddjFmtJJNZFftj
            hbjSTvSJTfcSwcPSPfTbfHszVVFpGnpJpsHFnHVVls
            rtZrcQrRZZQrmZBQlCGppnppHzpVFCGR
            WmLqmgNtcLNQWTbPvfPwbbdb
            HzZgsdHglHlzdHsFtsNNJSlNcSpjcjlrrNVv
            wqqWRPPqwmbcqPjQVvSPJJrVpv
            qqBBqmWRhqRLqcBnhzzztgnTdDHnHsFsHn
            rJPFVwwsrJwmdVrLWJvvRBWBvbzWlb
            nDZcNGNpjTpHncvpZCDnTNZGhlWzQhWbpRRQlQhpWWSWLlQb
            CDNntnCCHnvmqPfwtFdVqd
            gqBwgBjCswwgqNBNCVDDTVdhlSDTDcZc
            HvRRFMzRRRRMpHrtTllfhZHHSShHTf
            PmlGLPrppMrrmFFmLMWRjbsjnsjwQNJWnbQjWgBN
            pDggpFgRghZjBFPPnPPFrt
            cwTfLwBVwCWbLcVTVVvrdndGjMHrnGJtnttdMC
            NTVcWNvcBSpgNqspRQlN
            DLDgFlDmNZfjfnJZSF
            tctvttzvGGzvrHqtVVdwnJGSSnnjjZdWTdwW
            zvpcrbpHpqJJsPbPlLlhmhglPQ
            pvHHvssFCFZQNCftttdQdd
            VgTGTTVGgLjDjlLGzgPVMTNwmcwQmMQfQtmdcmwMJwNm
            TPjTDjfGWTLLljgzrWpZZbsqrFqhqbps
            ppVLcfcwSLgpSLVLgWwtfshDNDqvWvGvlQZvDNHQHjqq
            MPrzmdRrPPrCJFnMnMRRFRPdqqZQNQvjvZDGDlHhQvGNDG
            BmBMBBJTMmPBJMMFCCFJRmrsTlVpVbpwLSVwLsgcwTVlVc
            SSGzmFRzmRGLgSSmGMJFnvfvJnJVnJQnMl
            cBpjHtjwNfcpNZtppHtCMlMPMlJBVlVQlvJPvJ
            dNtNZwqWfqtqZWtHttsqHqrRrrdRTLbmmzSLmTGGmbrg
            RrrddnrgnRbbgWdGrfnwgQwjDjDpvTpBQTwBPP
            MHCStZJzSwvPjWQD
            mcJWVHCCLcGLbdcn
            PlMsdjPdGMjdPSrSjgddbLbmHHTszHZzpHmsTFvmpzZzmN
            ntRJQVRfcQhcQWhnchBJWntTFTTTNTSpFtztmZFDTpDZ
            hQfcfCBSwCccVJhSJnrPPGLqPlbPLCrqldgb
            vgvWDMZvGpcqgqsP
            tSdtjLHLQLHjdFdDddQSQhwlsGqwQlqqqhQsPhGc
            tbRjtTLFRvTZDBrMrV""";
}
