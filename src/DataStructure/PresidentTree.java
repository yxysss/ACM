package DataStructure;

public class PresidentTree {

    private class SegmentTree {

        private class Node {
            private Node leftChild, rightChild;
            private int l, r;
            private int number;

            public Node(int l, int r) {
                this.l = l;
                this.r = r;
                leftChild = rightChild = null;
                number = 0;
            }

            public void setLeftChild() {
                if (l < r) {
                    leftChild = new Node(l, (l + r) / 2);
                    leftChild.setLeftChild();
                    leftChild.setRightChild();
                    leftChild.updateNumber();
                }
            }

            public void setRightChild() {
                if (l < r) {
                    rightChild = new Node((l + r) / 2 + 1, r);
                    rightChild.setLeftChild();
                    rightChild.setRightChild();
                    rightChild.updateNumber();
                }
            }

            private void updateNumber() {
                if (l < r) {
                    number = leftChild.number + rightChild.number;
                }
            }
        }

        private Node root;
        private int no;

        public SegmentTree(int no, SegmentTree previousTree) {
            root = new Node(0, values.length-1);
            if (previousTree != null) {
                if (values[no] > size / 2) {
                    root.leftChild = previousTree.root.leftChild;
                    root.setRightChild();
                    root.updateNumber();
                } else {
                    root.rightChild = previousTree.root.rightChild;
                    root.setLeftChild();
                    root.updateNumber();
                }
            } else {
                root.setLeftChild();
                root.setRightChild();
                root.updateNumber();
            }
            this.no = no;
            for (int i = 0; i <= no; i ++) {
                if ((values[no] > size/2) == (values[i] > size/2)) {
                    insert(root, values[i]);
                }
            }
        }

        public void insert(Node head, int x) {
            if (head.l == head.r && head.r == x) {
                head.number ++;
            } else {
                if (head.leftChild.r >= x) {
                    insert(head.leftChild, x);
                } else {
                    insert(head.rightChild, x);
                }
                head.number = head.leftChild.number + head.rightChild.number;
            }
        }

    }

    private int[] values;
    private int size;
    private SegmentTree[] trees;


    public PresidentTree(int[] values, int size) {
        this.values = values;
        this.size = size;
        trees = new SegmentTree[values.length];
        for (int i = 0; i < values.length; i ++) {
            if (i == 0) {
                trees[i] = new SegmentTree(i, null);
            } else {
                trees[i] = new SegmentTree(i, trees[i-1]);
            }
        }
    }

    public int query(int l, int r, int k) {
        SegmentTree.Node rootLeft, rootRight;
        if (l == 0) rootLeft = null;
        else rootLeft = trees[l-1].root;
        rootRight = trees[r].root;
        System.out.println(rootLeft.number + "," + rootRight.number);
        return queryBetweenTwoTrees(rootLeft, rootRight, k);
    }

    private int queryBetweenTwoTrees(SegmentTree.Node rootLeft, SegmentTree.Node rootRight, int k) {
        System.out.println(rootLeft.l + "..." + rootLeft.r + "..." + k);
        if (rootLeft.leftChild == null) {
            return rootLeft.l;
        }
        int diff = rootRight.leftChild.number - rootLeft.leftChild.number;
        if (k <= diff) {
            return queryBetweenTwoTrees(rootLeft.leftChild, rootRight.leftChild, k);
        } else {
            return queryBetweenTwoTrees(rootLeft.rightChild, rootRight.rightChild, k - diff);
        }
    }

    public static void main(String[] args) {

        int[] values = {5,2,4,3,9,0,8,1,2,6};
        int size = 9;
        PresidentTree presidentTree = new PresidentTree(values, size);
        int ans1 = presidentTree.query(2, 5, 2);
        System.out.println(ans1);
    }

}
