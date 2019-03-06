package DataStructure;

public class SuffixArray {


    // 倍增算法
    // 桶排序

    private class Node {
        private Node next;
        private int value;
        private Node(int value) {

            this.value = value;

        }

    }
    private Node[] buckets = new Node[10];
    private Node[] ends = new Node[10];
    private int getPosition(int Number, int number) {
        for (int i = 0; i < number - 1; i ++) {
            Number /= 10;
        }
        return Number % 10;
    }
    private int[] radixSort(int[] array, int number) {

        boolean finished = true;
        for (int i = 0; i < array.length; i ++) {
            int remainder = getPosition(array[i], number);
            if (remainder > 0) finished = false;
            if (buckets[remainder] == null) {
                buckets[remainder] = new Node(array[i]);
                ends[remainder] = buckets[remainder];
            } else {
                Node slot = new Node(array[i]);
                ends[remainder].next = slot;
                ends[remainder] = slot;
            }
        }

        int index = 0;
        for (int i = 0; i < 10; i ++) {
            Node head = buckets[i];
            while (head != null) {
                array[index ++] = head.value;
                head = head.next;
            }
        }

        for (int i = 0; i < 10; i ++) {
            buckets[i] = ends[i] = null;
        }
        if (!finished) return radixSort(array, number + 1);
        return array;
    }

    // 计数排序
    // 待排序序列，最小值不小于0，最大值不超过k
    private int[] C, B, Order;
    private void countingSort(int[] array) {
        int max = 0;
        for (int i = 0; i < array.length; i ++) {
            max = array[i] > max ? array[i] : max;
        }
        C = new int[max + 1];
        B = new int[array.length];
        Order = new int[array.length];
        for (int i = 0; i < array.length; i ++) {
            C[array[i]] ++;
        }
        for (int i = 1; i <= max; i ++) {
            C[i] += C[i-1];
        }
        for (int i = 0; i < array.length; i ++) {
            B[C[array[i]]] = array[i];
            Order[C[array[i]]] = i;
            C[array[i]] --;
        }
    }


    // 使用计数排序实现双关键字基数排序
    private int[] count;
    private int[] order;
    private int[] finalOrder;
    private void radixSortBycountSort(int[] key0, int[] key1) {
        int max = 0;
        for (int i = 0; i < key1.length; i ++) {
            max = key1[i] > max ? key1[i] : max;
        }
        count = new int[max + 1];
        for (int i = 0; i < key1.length; i ++) {
            count[key1[i]] ++;
        }
        for (int i = max - 1; i >= 0; i --) {
            count[i] += count[i + 1];
        }
        order = new int[key1.length];
        for (int i = 0; i < key1.length; i ++) {
            order[count[key1[i]] - 1] = i;
            count[key1[i]] --;
        }
        max = 0;
        for (int i = 0; i < key0.length; i ++) {
            max = key0[i] > max ? key0[i] : max;
        }
        count = new int[max + 1];
        for (int i = 0; i < key0.length; i ++) {
            count[key0[i]] ++;
        }
        for (int i = 1; i <= max; i ++) {
            count[i] += count[i-1];
        }
        finalOrder = new int[key0.length];
        for (int i = 0; i < key1.length; i ++) {
            finalOrder[count[key0[order[i]]] - 1] = order[i];
            count[key0[order[i]]]  --;
        }
        for (int i = 0 ; i < key0.length; i ++) {
            System.out.print(order[i] + " ");
        }
        System.out.println();
        for (int i = 0 ; i < key0.length; i ++) {
            System.out.print(finalOrder[i] + " ");
        }
        System.out.println();
    }


    // 后缀数组
    // SA 是一个一维数组，它保存所有后缀字符串从小到大按字典序排列的顺序
    // rank是一个一维数组，保存以下标i为开头的后缀在SA中的名次
    // SA[rank[i]]表示以下标i为开头的后缀
    // SA[i]表示排名为i的后缀
    // rank[i]表示以下标i为开头的后缀的排名

    // 使用倍增算法排序
    // 由于后缀之间具有连续性，每一次迭代，与i+1, i+2, i+4, ... , i+2^k合并，计算数值

    private int[] SA;
    private int[] rank;
    private Node[] sbuckets;
    private Node[] sends;
    private String string;
    private int[] height;
    public SuffixArray(String string) {
        this.string = string;
        SA = new int[string.length()];
        rank = new int[string.length()];
        sbuckets = new Node[string.length() + 1];
        sends = new Node[string.length() + 1];
        height = new int[string.length()];
        initiate();
        process();
        calcHeight();
    }

    private void calcHeight() {
        
    }

    private void process() {
        int k = 1;
        while (1 != 0) {
            for (int i = 0; i < string.length(); i ++) {
                int remainder = i + k >= string.length() ? 0 : rank[i + k] + 1;
                if (sbuckets[remainder] == null) {
                    sends[remainder] = sbuckets[remainder] = new Node(i);
                } else {
                    sends[remainder].next = new Node(i);
                    sends[remainder] = sends[remainder].next;
                }
            }
            int[] tmp = new int[string.length()];
            int index = 0;
            for (int i = 0; i <= string.length(); i ++) {
                Node head = sbuckets[i];
                while (head != null) {
                    tmp[index ++] = head.value;
                    head = head.next;
                }
                sbuckets[i] = null;
            }
            for (int i = 0; i < string.length(); i ++) {
                int remainder = rank[tmp[i]];
                if (sbuckets[remainder] == null) {
                    sends[remainder] = sbuckets[remainder] = new Node(tmp[i]);
                } else {
                    sends[remainder].next = new Node(tmp[i]);
                    sends[remainder] = sends[remainder].next;
                }
            }
            index = 0;
            int rindex = 0;
            for (int i = 0; i < string.length(); i ++) {
                Node head = sbuckets[i];
                while (head != null) {
                    SA[index] = head.value;
                    if (index == 0) rank[SA[index]] = rindex;
                    else {
                        int prank = SA[index - 1] + k >= string.length()?0:rank[SA[index - 1] + k];
                        int crank = SA[index] + k >= string.length()?0:rank[SA[index] + k];
                        rank[SA[index]] = prank == crank ? rindex : ++rindex;
                    }
                    head = head.next;
                    index ++;
                }
                sbuckets[i] = null;
            }
            k = k * 2;
            if (k >= string.length()) break;
        }
    }

    private void qsort(int l, int r) {
        int i = l, j = r;
        int compare = string.charAt(SA[(i + j) / 2]);
        int tmp;
        while (i <= j) {
            while (string.charAt(SA[i]) < compare) i ++;
            while (string.charAt(SA[j]) > compare) j --;
            if (i <= j) {
                tmp = SA[i];
                SA[i] = SA[j];
                SA[j] = tmp;
                i ++; j --;
            }
        }
        if (i < r) qsort(i, r);
        if (l < j) qsort(l, j);
    }
    private void initiate() {
        for (int i = 0; i < string.length(); i ++) {
            SA[i] = i;
        }
        qsort(0, string.length() - 1);
        int rindex = 0;
        for (int i = 0; i < string.length(); i ++) {
            if (i == 0) rank[SA[i]] = i;
            else {
                if (string.charAt(SA[i]) == string.charAt(SA[i - 1])) rank[SA[i]] = rindex;
                else rank[SA[i]] = ++ rindex;
            }
        }
    }


    public static void main(String[] args) {

        int[] array = new int[]{73, 22, 93, 43, 55, 14, 28, 65, 39, 81};

        SuffixArray suffixArray = new SuffixArray("abcbcadgs");
        /*
        int[] sortedArray = suffixArray.radixSort(array, 1);

        for (int i = 0; i < sortedArray.length; i ++) {

            System.out.println(sortedArray[i]);

        }
        */
        int[] key0 = new int[]{1,2,3,4,5,6,1,2,3,4,5,6};
        int[] key1 = new int[]{0,1,2,3,4,5,1,2,3,4,5,6};
        //                      0 6 1 7 2 8 3 9 4 10 5 11
        suffixArray.radixSortBycountSort(key0, key1);

        for (int i = 0; i < suffixArray.SA.length; i ++) {
            System.out.print(suffixArray.SA[i] + " ");
        }
        System.out.println();

    }


}
