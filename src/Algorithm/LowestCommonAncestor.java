package Algorithm;

public class LowestCommonAncestor {

    // Tarjan算法求LCA,离线算法，每次查询时间复杂度O(n)，使用并查集
    private static class BinaryTree {
        private Node root;
        private Node[] nodes;

        private int[] disjointSet;
        private int x, y;
        private int ancestor;

        private int getAncestor(int x) {
            Node node = nodes[x];
            if (node.value != disjointSet[node.value]) {
                disjointSet[node.value] = getAncestor(disjointSet[node.value]);
            }
            return disjointSet[node.value];
        }

        public BinaryTree(int length) {
            nodes = new Node[length];
            disjointSet = new int[length];
            for (int i = 0; i < length; i ++) {
                Node node;
                if (i == 0) {
                    node = root = new Node(i);
                }
                else {
                    node = new Node(i);
                    node.parent = nodes[(i - 1) / 2];
                    if (i % 2 == 1) node.parent.leftChild = node;
                    else node.parent.rightChild = node;
                }
                nodes[i] = node;
            }
        }

        public int getLowesetCommonAncestor(int value1, int value2) {
            x = value1; y = value2; ancestor = -1;
            for (int i = 0; i < disjointSet.length; i ++) {
                disjointSet[i] = i;
            }
            /*
            for (int i = 0; i < disjointSet.length; i ++) {
                System.out.println(getAncestor(i));
            }
            */
            dfs(root);
            return ancestor;
        }

        private void dfs(Node head) {
            if (ancestor != -1) return ;
            if (head.leftChild != null) {
                dfs(head.leftChild);
            }
            if (head.rightChild != null) {
                dfs(head.rightChild);
            }
            if (ancestor != -1) return ;
            int ancestorx = getAncestor(x), ancestory = getAncestor(y);
            if (ancestorx != ancestory) {
                disjointSet[head.value] = head.parent.value;
            } else {
                ancestor = ancestorx;
            }
            return ;
        }
    }

    private static class Node {
        private int value;
        private Node parent;
        private Node leftChild, rightChild;

        public Node(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {

        BinaryTree binaryTree = new BinaryTree(10);
        System.out.println(binaryTree.getLowesetCommonAncestor(4,5));
        System.out.println(binaryTree.getLowesetCommonAncestor(8,9));
        System.out.println(binaryTree.getLowesetCommonAncestor(7,9));
        System.out.println(binaryTree.getLowesetCommonAncestor(7,8));

    }


}
