package Example;

import java.util.Arrays;
import java.util.Scanner;

public class TheLongestPalindrome {

    private static class SuffixTree {

        private Node root = new Node(-1, -1);
        private ActivePoint activePoint = new ActivePoint(root, null, 0);
        private int remainder = 0;
        private int totalLength = 0;
        private int length = 0;
        private char[] chars;
        private int round = 0;
        private Node[] round1;
        private Node[] round2;
        private String string;

        private void checkActivePoint() {
            if (activePoint.index != null) {
                if (activePoint.index.end != -1 && activePoint.index.end - activePoint.index.start == activePoint.length) {
                    // 如果在当前子边匹配之后，下一次匹配已经超出当前子边的长度
                    activePoint.point = activePoint.index;
                    activePoint.index = null;
                    activePoint.c = null;
                    activePoint.length = 0;
                }
            }
        }

        private boolean find(char c) {

            boolean exist = false;

            if (activePoint.index == null) { // 不指向任何子边，则需要对所有子边进行判断，选择一条子边
                Node node = activePoint.point.child;
                activePoint.c = c; // 设置要寻找的边的头子符为当前字符
                while (node != null) {
                    if (chars[node.start] == c) {
                        activePoint.index = node; // 选择子边
                        activePoint.length = 1; // 设置此时在子边上的长度，初始为1
                        exist = true;
                        break;
                    } else {
                        node = node.brother;
                    }
                }
                checkActivePoint();
            } else { // 此时指向一条子边，继续在子边上进行判断
                if (chars[activePoint.index.start + activePoint.length] == c) { // 如果在当前子边上可以继续匹配
                    activePoint.length++;
                    exist = true;
                    checkActivePoint();
                }
            }

            return exist;

        }

        private void resetActivePoint() {
            if (length > totalLength && remainder == 0) return ;
            if (root == activePoint.point) {
                activePoint.c = chars[length - remainder];
                activePoint.resetIndex();
                activePoint.length--;
            } else if (null == activePoint.point.suffixNode) {
                activePoint.point = root;
                activePoint.c = chars[length - remainder];
                activePoint.resetIndex();
            } else {
                activePoint.point = activePoint.point.suffixNode;
                activePoint.resetIndex();
                // 追寻后缀节点，无需改变activePoint.c和activePoint.length
            }
        }

        private void build(char c) {

            chars[length] = c;
            length++; // 新加入一个字符，字符总长度+1

            remainder++;

            if (find(c)) { // 如果可以在activePoint上匹配当前字符
                return;
            }
            completeTree();
        }

        private void storeNode(Node node) {
            if (round == 1) {
                round1[length - remainder] = node;
            } else {
                round2[length - totalLength - 1 - remainder] = node;
            }
        }

        private void completeTree() {
            if (activePoint.index == null) { // 如果当前activePoint.index是null，则需要创建新的子分支
                Node splitNode = new Node(length - 1, -1);
                storeNode(splitNode);
                Node node = activePoint.point;
                Node child = node.child;
                if (null == child) {
                    node.child = splitNode;
                    splitNode.last = node;
                } else {
                    while (child.brother != null) {
                        child = child.brother;
                    }
                    child.brother = splitNode;
                    splitNode.last = child;
                }
                remainder--;
                resetActivePoint();
                if (remainder > 0) innersplit(node);
            } else { // 分裂当前activePoint.index
                Node splitNode = activePoint.index;
                Node node = new Node(splitNode.start, splitNode.start + activePoint.length);
                if (splitNode.last.child == splitNode) {
                    splitNode.last.child = node;
                } else {
                    splitNode.last.brother = node;
                }
                node.last = splitNode.last;
                if (splitNode.brother != null) splitNode.brother.last = node;
                node.brother = splitNode.brother;
                node.child = splitNode;
                splitNode.last = node;
                splitNode.start = splitNode.start + activePoint.length;

                Node newNode = new Node(length - 1, -1);
                storeNode(newNode);
                splitNode.brother = newNode;
                newNode.last = splitNode;
                remainder--;
                resetActivePoint();
                if (remainder > 0) innersplit(node); // 在接下来创建的分支都与当前分支有相同的子分支结构，设置suffixNode便于追踪
            }
        }

        private void innersplit(Node prefixNode) {

            while (activePoint.index != null && activePoint.index.getLength() <= activePoint.length) {
                // 如果当前activePoint.index的长度无法满足activePoint.length的分裂需求
                activePoint.point = activePoint.index;
                activePoint.length -= activePoint.index.getLength();
                activePoint.c = chars[length - activePoint.length - 1];
                activePoint.resetIndex();
            }

            if (activePoint.length == 0) {
                if (activePoint.index == null) { // 以该字符为首的子字符串不在activePoint.point的子节点中
                    Node splitNode = new Node(length - 1, -1);
                    storeNode(splitNode);
                    Node node = activePoint.point;
                    Node child = node.child;
                    if (null == child) {
                        node.child = splitNode;
                        splitNode.last = node;
                    } else {
                        while (child.brother != null) {
                            child = child.brother;
                        }
                        child.brother = splitNode;
                        splitNode.last = child;
                    }
                    remainder--;
                    prefixNode.suffixNode = node;
                    resetActivePoint();
                    if (remainder > 0) innersplit(node);
                } else {
                    // 以该字符为首的子字符串在activePoint.point的字节点中
                    // 设置后缀连接，直接返回
                    prefixNode.suffixNode = activePoint.point;
                    activePoint.length++;
                    checkActivePoint();
                    return;
                }
            } else {
                Node splitNode = activePoint.index;
                Node node = new Node(splitNode.start, splitNode.start + activePoint.length);
                splitNode.start = splitNode.start + activePoint.length;
                if (splitNode.last.child == splitNode) {
                    splitNode.last.child = node;
                } else {
                    splitNode.last.brother = node;
                }
                node.last = splitNode.last;
                if (splitNode.brother != null) splitNode.brother.last = node;
                node.brother = splitNode.brother;
                node.child = splitNode;
                splitNode.last = node;

                Node newNode = new Node(length - 1, -1);
                storeNode(newNode);
                splitNode.brother = newNode;
                newNode.last = splitNode;

                remainder--;
                prefixNode.suffixNode = node;
                resetActivePoint();
                if (remainder > 0) innersplit(node);
            }
        }

        private void print() {
            Node child = root.child;
            System.out.println(activePoint);
            while (child != null) {
                System.out.print("|——");
                child.print("    ");
                child = child.brother;
            }
        }

        private class Node {

            private int start; // inclusive
            private int end; // exclusive
            private Node child;
            private Node brother;
            private Node suffixNode;
            private Node last;

            private Node(int start, int end) {
                this.start = start;
                this.end = end;;
            }

            private int getEnd() {
                if (end == -1) return length;
                return end;
            }

            private int getLength() {
                if (start == -1) return 0;
                return getEnd() - start;
            }

            @Override
            public String toString() {
                String char0;
                if (start == -1) char0 = "root";
                else if (start == length) char0 = "(END)";
                else char0 = String.valueOf(Arrays.copyOfRange(chars, start, getEnd()));
                return "Node [chars=" + char0 + "]";
            }

            private void print(String prefix) {
                String char0;
                if (start == -1) char0 = "root";
                else if (start == length) char0 = "(END)";
                else char0 = String.valueOf(Arrays.copyOfRange(chars, start, getEnd()));
                System.out.print(char0 + " , last : " + last);
                if (null != this.suffixNode) {
                    if (this.suffixNode.start == -1) System.out.println("--root");
                    else
                        System.out.println("--" + String.valueOf(Arrays.copyOfRange(chars, this.suffixNode.start, this.suffixNode.getEnd())));
                } else {
                    System.out.println();
                }
                Node child = this.child;
                while (null != child) {
                    System.out.print(prefix + "|——");
                    child.print(prefix + prefix);
                    child = child.brother;
                }
            }

        }

        private class ActivePoint {
            private Node point;
            private Node index;
            private Character c;
            private int length;

            private ActivePoint(Node point, Node index, int length) {
                this.point = point;
                this.index = index;
                this.length = length;
            }

            private void resetIndex() {
                index = null;
                Node node = point.child;
                while (node != null) {
                    if (chars[node.start] == c) {
                        index = node;
                        break;
                    } else {
                        node = node.brother;
                    }
                }
            }

            @Override
            public String toString() {
                return "[activePoint:(" + activePoint.point + "," + activePoint.index + ","
                        + activePoint.length + "," + activePoint.c + ")], [remainder:" + remainder + "]";
            }
        }

        private void setString(String string) {
            this.string = string;
            totalLength = string.length();
            chars = new char[totalLength + 1 + totalLength + 1];
            round1 = new Node[totalLength + 1];
            round2 = new Node[totalLength + 1];
            round = 1;
            length = 0;
            for (int i = 0; i <= string.length(); i ++) {
                if (i < string.length()) build(string.charAt(i));
                else build('$');
            }
            /*
            this.print();
            System.out.println();
            for (int i = 0; i < totalLength; i ++) {
                System.out.println(round1[i]);
            }
            System.out.println();
            */
            round = 2;
            // length = 0;
            for (int i = string.length() - 1; i >= -1; i --) {
                if (i > -1) build(string.charAt(i));
                else build('#');
            }

            /*
            this.print();
            System.out.println();
            for (int i = 0; i < totalLength; i ++) {
                System.out.println(round2[i]);
            }
            */
        }

        private int commonLength;
        private Node node1, node2, firstancestor, ancestor;
        private int mark1, mark2;
        private void search(Node head) {
            if (head == node1) {
                mark1 = 1;
                return ;
            }
            if (head == node2) {
                mark2 = 1;
                return ;
            }

            int hasFound = 0;
            if (mark1 + mark2 == 1) hasFound = 1;
            Node child = head.child;
            while (child != null) {
                search(child);
                if (mark1 + mark2 == 1 && hasFound == 0) {
                    firstancestor = head;
                }
                if (mark1 + mark2 == 2) {
                    if (head == firstancestor) {
                        ancestor = head;
                    }
                    break;
                }
                child = child.brother;
            }
            if (ancestor != null) {
                // System.out.println(head + " , " + head.getLength());
                commonLength += head.getLength();
            }
            return ;
        }

        private int checkNode() {
            firstancestor = null; ancestor = null;
            commonLength = 0; mark1 = 0; mark2 = 0;
            search(root);
            return 0;
        }

        private String findTheLongestPalindrome() {
            int mark = 0;
            int maxLength = 0;
            int index = -1;
            for (int i = 0; i < totalLength; i ++) {
                node1 = round1[i];
                node2 = round2[totalLength - 1 - i];
                checkNode();
                // System.out.println(i + " , " + ancestor);
                if (commonLength * 2 - 1 > maxLength) {
                    maxLength = commonLength * 2 - 1;
                    index = i;
                }
            }
            // System.out.println(index + " , " + maxLength);
            for (int i = 0; i < totalLength - 1; i ++) {
                if (string.charAt(i) == string.charAt(i + 1)) {
                    node1 = round1[i + 1];
                    node2 = round2[totalLength - 1 - i];
                    checkNode();
                    // System.out.println(i + " , " + ancestor + " , " + commonLength);
                    if (commonLength * 2 > maxLength) {
                        maxLength = commonLength * 2;
                        index = i;
                        mark = 1;
                    }
                }
            }
            // System.out.println(index + " , " + maxLength);
            if (mark == 0) return string.substring(index - (maxLength + 1) / 2 + 1, index + (maxLength + 1) / 2);
            return string.substring(index - maxLength / 2 + 1, index + maxLength / 2 + 1);
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        SuffixTree suffixTree = new SuffixTree();
        suffixTree.setString(string);
        System.out.println(suffixTree.findTheLongestPalindrome());
    }
}
