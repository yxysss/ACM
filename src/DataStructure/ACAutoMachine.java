package DataStructure;

public class ACAutoMachine {

    private Node root = new Node();

    public void build(String s) {

        char[] chars = s.toCharArray();

        Node node = root;

        for (int i = 0; i < chars.length; i ++) {
            int c = chars[i] - 'a';
            if (node.child[c] == null) {
                node.child[c] = new Node();
            }
            node = node.child[c];
        }

        node.sum ++;

    }

    public void setfail() {
        Node[] queue = new Node[10001];
        int h = 0, r = 0;
        queue[r++] = root;

        while (h < r) {

            Node head = queue[h++];

            for (int i = 0; i < 26; i ++) {
                if (head.child[i] != null) {
                    queue[r++] = head.child[i];
                    if (head == root) head.child[i].fail = root;
                    else {
                        if (head.fail != null) head.child[i].fail = head.fail.child[i];
                    }
                }
            }
        }
    }

    public void match(String mode) {

        char[] chars = mode.toCharArray();
        Node node = root;

        int sum = 0;

        for (int i = 0; i < chars.length; i ++) {
            int c = chars[i] - 'a';
            if (node.child[c] != null) node = node.child[c];
            else {
                while (1 != 0) {
                    node = node.fail;
                    if (node == null) {
                        node = root;
                        break;
                    }
                    if (node.child[c] != null) {
                        node = node.child[c];
                        break;
                    }
                }
            }
            sum += node.sum;
            Node tmp = node;
            while (tmp.fail != null) {
                tmp = tmp.fail;
                sum += tmp.sum;
            }
        }

        System.out.println(sum);
    }

    class Node {

        private Node[] child;
        private Node fail;
        private int sum = 0;

        public Node() {
            child = new Node[26];
        }


    }

    public static void main(String[] args) {

        ACAutoMachine acAutoMachine = new ACAutoMachine();
        acAutoMachine.build("abc");
        acAutoMachine.build("abcd");
        acAutoMachine.build("bcd");
        acAutoMachine.build("bcde");
        acAutoMachine.setfail();

        acAutoMachine.match("abcde");
        acAutoMachine.match("bcde");
        acAutoMachine.match("acbdabcebdbcde");
    }
}
