package DataStructure;

import java.io.IOException;
import java.util.Arrays;

public class suffixtreeonline {

    private Node root = new Node(-1,-1);
    private ActivePoint activePoint = new ActivePoint(root, null, 0);
    private int remainder = 0;
    private int length = 0;
    private char[] chars = new char[10000];

    private void checkActivePoint() {
        if (activePoint.index != null) {
            System.out.println("activePoint.index=" + activePoint.index + ",activePoint.index.length="
                    + activePoint.index.getLength() + ",activePoint.length=" + activePoint.length);
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
        if (activePoint.index != null) System.out.println("current index: " + activePoint.index);
        System.out.println("c = " + c);

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
            /*
            if (activePoint.index != null) {
                System.out.println("activePoint.index=" + activePoint.index + ",activePoint.index.length="
                        + activePoint.index.getLength() + ",activePoint.length=" + activePoint.length);
                if (activePoint.index.end != -1 && activePoint.index.end - activePoint.index.start == activePoint.length) {
                    // 如果在当前子边匹配之后，下一次匹配已经超出当前子边的长度
                    activePoint.point = activePoint.index;
                    activePoint.index = null;
                    activePoint.c = null;
                    activePoint.length = 0;
                }
            }
            */
            checkActivePoint();
        } else { // 此时指向一条子边，继续在子边上进行判断
            System.out.println(activePoint.index.start + " , " + activePoint.length);
            if (chars[activePoint.index.start + activePoint.length] == c) { // 如果在当前子边上可以继续匹配
                activePoint.length ++;
                exist = true;
                /*
                System.out.println("activePoint.index=" + activePoint.index + ",activePoint.index.length="
                        + activePoint.index.getLength() +",activePoint.length="+activePoint.length);
                if (activePoint.index.end != -1 && activePoint.index.end - activePoint.index.start == activePoint.length) {
                    // 如果在当前子边匹配之后，下一次匹配已经超出当前子边的长度
                    activePoint.point = activePoint.index;
                    activePoint.index = null;
                    activePoint.c = null;
                    activePoint.length = 0;
                }
                */
                checkActivePoint();
            }
        }

        return exist;

    }

    private void resetActivePoint() {
        if (root == activePoint.point) {
            activePoint.c = chars[length - remainder];
            activePoint.resetIndex();
            activePoint.length --;
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

    public void build(char c) {

        chars[length] = c;
        length ++; // 新加入一个字符，字符总长度+1

        System.out.println("当前插入后缀：" + c + "========");

        remainder ++;

        if (find(c)) { // 如果可以在activePoint上匹配当前字符
            this.print();// 打印
            System.out.println();
            return ;
        }

        System.out.println(false);
        // 如果在activePoint上无法匹配当前字符
        System.out.println(activePoint);

        completeTree();
        /*
        if (remainder == 1) { // 仅当前字符需要添加，且以当前字符为首的子字符串不在root的子节点中
            Node node = new Node(length - 1, -1);
            Node child = activePoint.point.child; // 此时activePoint.point必为root
            if (null == child) { // 如果root没有子节点
                activePoint.point.child = node;
                node.last = root;
            } else { // 如果root有子节点
                while (child.brother != null) {
                    child = child.brother;
                }
                child.brother = node;
                node.last = child;
            }
            remainder --;
            // activePoint为{root,null,null,0}，无需变化
        } else { // 有多个连续后缀需要添加，需要在activePoint.index上创建子分支

            if (activePoint.index == null) { // 如果当前activePoint.index是null，则需要创建新的子分支
                Node splitNode = new Node(length - 1, -1);
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
                remainder --;
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
                splitNode.brother = newNode;
                newNode.last = splitNode;
                remainder --;
                resetActivePoint();
                // this.print();
                // System.out.println();
                if (remainder > 0) innersplit(node); // 在接下来创建的分支都与当前分支有相同的子分支结构，设置suffixNode便于追踪
            }
        }
        */

        this.print();// 打印
        System.out.println();


    }

    private void completeTree() {
        /*
        if (remainder == 1) { // 仅当前字符需要添加，且以当前字符为首的子字符串不在root的子节点中
            Node node = new Node(length - 1, -1);
            Node child = activePoint.point.child; // 此时activePoint.point必为root
            if (null == child) { // 如果root没有子节点
                activePoint.point.child = node;
                node.last = root;
            } else { // 如果root有子节点
                while (child.brother != null) {
                    child = child.brother;
                }
                child.brother = node;
                node.last = child;
            }
            remainder --;
            // activePoint为{root,null,null,0}，无需变化
        } else { // 有多个连续后缀需要添加，需要在activePoint.index上创建子分支
        */

            if (activePoint.index == null) { // 如果当前activePoint.index是null，则需要创建新的子分支
                Node splitNode = new Node(length - 1, -1);
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
                splitNode.brother = newNode;
                newNode.last = splitNode;
                remainder--;
                resetActivePoint();
                // this.print();
                // System.out.println();
                if (remainder > 0) innersplit(node); // 在接下来创建的分支都与当前分支有相同的子分支结构，设置suffixNode便于追踪
            }
        // }
    }

    private void innersplit(Node prefixNode) {

        System.out.println("inner : " + activePoint + " , prefixNode : " + prefixNode);
        // this.print();
        // System.out.println();

        while (activePoint.index != null && activePoint.index.getLength() <= activePoint.length) {
            // 如果当前activePoint.index的长度无法满足activePoint.length的分裂需求
            activePoint.point = activePoint.index;
            activePoint.length -= activePoint.index.getLength();
            activePoint.c = chars[length - activePoint.length - 1];
            activePoint.resetIndex();
        }

        System.out.println("inner after: " + activePoint + " , prefixNode : " + prefixNode);

        if (activePoint.length == 0) {
            if (activePoint.index == null) { // 以该字符为首的子字符串不在activePoint.point的子节点中
                Node splitNode = new Node(length - 1, -1);
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
                remainder --;
                prefixNode.suffixNode = node;
                resetActivePoint();
                if (remainder > 0) innersplit(node);
            } else {
                // 以该字符为首的子字符串在activePoint.point的字节点中
                // 设置后缀连接，直接返回
                prefixNode.suffixNode = activePoint.point;
                activePoint.length ++;
                checkActivePoint();
                return ;
            }
        } else {
            Node splitNode = activePoint.index;
            Node node = new Node(splitNode.start, splitNode.start + activePoint.length);
            splitNode.start = splitNode.start + activePoint.length;
            // System.out.println("splitNode : " + splitNode);
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
            splitNode.brother = newNode;
            newNode.last = splitNode;

            remainder --;
            prefixNode.suffixNode = node;
            resetActivePoint();
            if (remainder > 0) innersplit(node);
        }

        /*
        if (remainder == 1) { // 此时activePoint.point=root，remainder=1，添加最后一个字符
            if (activePoint.index == null) { // 若该字符在前缀中未曾出现，需要在root新建一个分支
                Node node = new Node(length - 1, -1);
                Node child = activePoint.point.child;
                if (null == child) { // 此时root不可能没有child
                    activePoint.point.child = node;
                    node.last = activePoint.point;
                } else {
                    while (child.brother != null) {
                        child = child.brother;
                    }
                    child.brother = node;
                    node.last = child;
                }
            } else { // 若该字符在前缀中出现过，直接在当前activePoint.index上分裂
                if (activePoint.index.getLength() > 1) {
                    Node splitNode = activePoint.index;
                    Node node = new Node(splitNode.start, splitNode.start + 1);
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
                    splitNode.start = splitNode.start + 1;

                    Node newNode = new Node(length, -1); // start == length，表示终结符号
                    splitNode.brother = newNode;
                    newNode.last = splitNode;
                } else {
                    Node newNode = new Node(length, -1);
                    Node child = activePoint.index.child;
                    if (child == null) {
                        child = newNode;
                        newNode.last = activePoint.index;
                    }
                    else {
                        while (child.brother != null) {
                            child = child.brother;
                        }
                        child.brother = newNode;
                        newNode.last = child;
                    }
                }
            }
            remainder --;
            prefixNode.suffixNode = activePoint.point;
            activePoint.point = root;
            activePoint.index = null;
            activePoint.length = 0;
            return ;
        }
        */

        /*
        if (activePoint.index == null) { // 如果当前activePoint.index是null，则需要创建新的子分支
            Node node = new Node(length - 1, -1);
            Node child = activePoint.point.child;
            if (null == child) {
                activePoint.point.child = node;
                node.last = activePoint.point;
            } else {
                while (child.brother != null) {
                    child = child.brother;
                }
                child.brother = node;
                node.last = child;
            }
            remainder --;
            prefixNode.suffixNode = activePoint.point;
            System.out.println("remainder : " + remainder);
            resetActivePoint();
            if (remainder > 0) innersplit(activePoint.point);
        } else {
            Node splitNode = activePoint.index;
            Node node;
            if (activePoint.length == 0) {
                System.out.println("length == 0 : " + activePoint);
                this.print();
                System.out.println();
                Node newNode = new Node(length, -1);
                Node child = activePoint.index.child;
                if (child == null) {
                    child = newNode;
                    newNode.last = activePoint.index;
                }
                else {
                    while (child.brother != null) {
                        child = child.brother;
                    }
                    child.brother = newNode;
                    newNode.last = child;
                }

                node = activePoint.point;


                // this.print();
                // System.out.println();

            } else {
                node = new Node(splitNode.start, splitNode.start + activePoint.length);
                splitNode.start = splitNode.start + activePoint.length;
                // System.out.println("splitNode : " + splitNode);
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
                splitNode.brother = newNode;
                newNode.last = splitNode;
            }

            remainder --;
            // System.out.println("remainder : " + remainder);
            resetActivePoint();
            // this.print();
            // System.out.println();
            prefixNode.suffixNode = node;
            if (remainder > 0) innersplit(node); // 在接下来创建的分支都与当前分支有相同的子分支结构，设置suffixNode便于追踪
        }
        */
    }

    public void print() {
        Node child = root.child;
        System.out.println(activePoint);
        while (child != null) {
            System.out.print("|——");
            child.print("    ");
            child = child.brother;
        }
    }

    class Node {

        public int start; // inclusive
        public int end; // exclusive
        public Node child;
        public Node brother;
        public Node suffixNode;
        public Node last;

        public Node(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getEnd() {
            if (end == -1) return length;
            return end;
        }

        public int getLength() {
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

        // weqt9]psjkfnsuijjfjdkjfjkdfjkjkjjksja;jk

        public void print(String prefix) {
            String char0;
            if (start == -1) char0 = "root";
            else if (start == length) char0 = "(END)";
            else char0 = String.valueOf(Arrays.copyOfRange(chars, start, getEnd()));
            System.out.print(char0 + " , last : " + last);
            if (null != this.suffixNode) {
                if (this.suffixNode.start == -1) System.out.println("--root");
                else System.out.println("--" + String.valueOf(Arrays.copyOfRange(chars, this.suffixNode.start, this.suffixNode.getEnd())));
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

        public ActivePoint(Node point, Node index, int length) {
            this.point = point;
            this.index = index;
            this.length = length;
        }

        private void resetIndex() {
            index = null;
            Node node = point.child;
            while (node != null) {
                // System.out.println("start : " + node);
                if (chars[node.start] == c) {
                    index = node;
                    break;
                } else {
                    node = node.brother;
                }
            }
            // System.out.println("resetIndex c = " + c + " , index = " + index);

        }

        @Override
        public String toString() {
            return "[activePoint:(" + activePoint.point + "," + activePoint.index + ","
                    + activePoint.length + "," + activePoint.c + ")], [remainder:" + remainder + "]";
        }
    }

    public static void main(String[] args) {
        char c;
        suffixtreeonline suffixtreeonline = new suffixtreeonline();
        try {
            while ((c = (char)System.in.read()) != '$') {
                if (c == '\n') continue;
                suffixtreeonline.build(c);
            }
            suffixtreeonline.build('$');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
