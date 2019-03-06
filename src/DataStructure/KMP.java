package DataStructure;

public class KMP {


    private int[] next;
    private char[] mode, s;

    public KMP(String mode, String s) {
        this.mode = mode.toCharArray();
        this.s = s.toCharArray();
        next = new int[this.s.length+1];
    }

    private void getnext() {

        next[0] = -1;
        int count = -1;
        int index = 0;

        while (1 != 0) {
            if (count == -1 || s[count] == s[index]) {
                next[++index] = ++count;
            } else {
                count = next[count];
            }
            if (index == s.length) break;
        }

        for (int i = 0; i <= s.length; i ++) {
            // System.out.println(next[i]);
        }
    }

    private void match() {

        int index = 0;
        int i = 0;

        while (1 != 0) {

            if (index == -1 || mode[i] == s[index]) {
                i ++;
                index ++;
            } else {
                index = next[index];
            }

            if (index == s.length) {
                System.out.println(i-s.length);
                index = next[index];
            }

            if (i == mode.length) break;
        }


    }


    public static void main(String[] args) {

        KMP kmp = new KMP("abcdefgefgefgabcdefgefg","efgefg");

        kmp.getnext();

        // System.out.println(kmp.next[kmp.s.length]);

        kmp.match();

    }

}
