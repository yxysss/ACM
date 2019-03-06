package DataStructure;

public class RedBlackTree {

    /*
    *
    * Properties:
    * 1. node is red or black
    * 2. root is black
    * 3. all leaves are black (NULL node)
    * 4. For every red node, its children are black
    * 5. From every node, the number of the black nodes on each simple route to its leaves is the same
    *    所有节点到达其叶子节点的简单路径上的黑色节点的数目都是相同的
    *
    * 这些约束确保了红黑树的关键特性：从根到叶子的最长的可能路径不多于最短的可能路径的两倍长。
    * 结果是这个树大致上是平衡的。
    * 因为操作比如插入、删除和查找某个值的最坏情况时间都要求与树的高度成比例，
    * 这个在高度上的理论上限允许红黑树在最坏情况下都是高效的，
    * 而不同于普通的二叉查找树。
    *
    * 要知道为什么这些性质确保了这个结果，注意到性质4导致了路径不能有两个毗连的红色节点就足够了。
    * 最短的可能路径都是黑色节点，最长的可能路径有交替的红色和黑色节点。
    * 因为根据性质5所有最长的路径都有相同数目的黑色节点，这就表明了没有路径能多于任何其他路径的两倍长。
    *
    * */

    private static final int RED = 0, BLACK = 1;

    public class Node {
        private Node parent;
        private Node left, right;
        private int color; // 0 means RED, 1 means BLACK
        private Object value;

        public Node(Object value) {
            this.value = value;
            // If node is not null, it has two null children(alseo called leaf)
            if (value != null) {
                left = new Node(null);
                right = new Node(null);
            }
        }
    }

    private Node getGrandParent(Node node) {
        return node.parent.parent;
    }

    private Node getUncle(Node node) {
        if (node.parent == getGrandParent(node).left) {
            return getGrandParent(node).right;
        }
        else {
            return getGrandParent(node).left;
        }
    }

    private void rotateLeft(Node node) {
        node.left = node.parent.right;
        node.left = node.parent;
    }

    private void rotateRight(Node node) {
        node.right = node.parent.left;
        node.right = node.parent;
    }

    /*
    * 插入新节点时，把新节点的颜色初始化为红色，这样插入到路径中时，确保性质5不会被破环
    * 仅需根据性质4调整
    * 而且节点插入后总是作为叶子节点（两个孩子都为null），所以在考虑性质4时，
    * 不用考虑孩子的颜色（总是都为黑色），只需考虑父亲的颜色
    * */

    /*
    * New node is at the root of RBTree, color it BLACK to satisfy property No.2
    * */
    private void insertCase1(Node node) {
        if (node.parent == null) {
            node.color = BLACK;
        } else {
            insertCase2(node);
        }
    }

    /*
    * 父节点是黑色的，树成立，直接返回
    * The parent of new node is BLACK(new node is RED), property No.4 is still valid, the tree is still valid.
    * Now new node has a parent(since it's not a root)
    * */
    private void insertCase2(Node node) {
        if (node.parent.color == BLACK) {
            return ;
        } else {
            insertCase3(node);
        }
    }

    /*
    * Now new node is RED, the parent of new node is RED，叔叔节点也是红的
    * For property 1, the parent is not root, so new node has a grandparent, for property 4, the grandparent is BLACK
    * Because the parent of new node is RED, property No.4 is not valid, the tree is not valid.
    * Consider new node's uncle, if the color of the uncle of new node is RED,
    * then we can simply recolor the parent and the uncle BLACK, the grandparent RED(to keep property 5 valid)
    * 在我们的新节点N有了一个黑色的父节点P，祖父节点为红色。
    * 因为通过父节点P或叔父节点U的任何路径都必定通过祖父节点G，在这些路径上的黑节点数目没有改变。
    * 但是，红色的祖父节点G可能是根节点，这就违反了性质2，也有可能祖父节点G的父节点是红色的，这就违反了性质4。
    * 为了解决这个问题，我们在祖父节点G上递归地进行情形1的整个过程。（把G当成是新加入的节点进行各种情形的检查）
    * */
    private void insertCase3(Node node) {
        // Because the parent is RED, the grandparent is BLACK, so there exists possibility that the uncle is null
        if (getUncle(node) != null && getUncle(node).color == RED) {
            node.parent.color = BLACK;
            getUncle(node).color = BLACK;
            getGrandParent(node).color = RED;
            insertCase1(getGrandParent(node));
        } else {
            insertCase4Step1(node);
        }
    }

    /*
    * 叔叔节点是黑的或为叶子节点（null）
    * 新节点和红色儿子节点不再同一侧
    * Now new node is RED, the parent of new node is RED, the grandparent is BLACK, the uncle is null or BLACK
    * If new node and the parent are not at the same side, apply LeftRotation or RightRotation on the parent to fix it.
    * After doing LeftRotation/RightRotation, the point of node should still be at the previous position of new node.
    * 将新节点和红色儿子节点调整到同一侧
    * */
    private void insertCase4Step1(Node node) {
        // If the parent is left node, new node is right node, do LeftRotation
        if (node == node.parent.right && node.parent == getGrandParent(node).left) {
            rotateLeft(node.parent);
            node = node.left;
        } else {
            // If the parent is right node, new node is left node, do RightRotation
            if (node == node.parent.left && node.parent == getGrandParent(node).right) {
                rotateRight(node.parent);
                node = node.right;
            }
        }
        insertCase4Step2(node);
    }

    /*
    * 调整后，节点和红色儿子节点在同一侧
    * Now new node is RED, the parent of new node is RED, the grandparent is BLACK, the uncle is null or BLACK
    * Most important: new node and the parent are at the same side.
    * Property No.4 is still not valid.
    * Now we recolor the parent BLACK, the grandparent RED, apply LeftRotation or RightRotation
    * Property No.4 is valid.
    * After doing LeftRotation/RightRotation, the grandparent becomes right/left node of the parent,
    * the parent is BLACK, the uncle is BLACK or null, property No.2 and 5 are still valid.
    * */
    private void insertCase4Step2(Node node) {
        node.parent.color = BLACK;
        getGrandParent(node).color = RED;
        // If new node and the parent are at the left side of the grandparent
        if (node == node.parent.left && node.parent == getGrandParent(node).left) {
            rotateRight(getGrandParent(node));
        } else {
            // If new node and the parent are at the right side of the grandparent
            rotateLeft(getGrandParent(node));
        }
    }

    private Node getSibling(Node node) {
        if (node == node.parent.left) {
            return node.parent.right;
        } else {
            return node.parent.left;
        }
    }

    /*
    * If we need to delete a node with two children, we can easily transform it into deleting a node with one child
    * We just need to choose the max node in the node's left subtree or the min node in its right subtree and interchange the value
    * If the node we need to delete is RED, then its parent and child must be BLACK(property No.4),
    * we can easily use the child to replace the node, property No.4 is still valid.
    * Moreover, since a RED node is deleted, property No.5 is still valid.
    * If the node is BLACK, the child is RED, then we can also replace the node with the child, and recolor the child BLACK, property No.4 and 5 are still valid.
    * If the node is BLACK, the child is BLACK, in this situation, the child is null.
    * (If the child is not null, 到达儿子节点所在子树的叶子节点的路径上的黑色节点数目多于到达当前结点的另一个儿子节点，也是叶子结点的路径上的黑色节点数目，
    *  property No.5 is not valid)
    * First we delete the node and replace it with its child(null)
    * Now node is its child.
    * */
    private void deleteNodeWithOneChild(Node node) {

    }

    /*
    * 删除了一个黑色节点X后，它的唯一儿子节点N（黑色）取代了它原来的位置。此时，所有经过N到达叶子节点的路径上的黑色节点数少一。
    * */

    /*
    * If node is new root, the tree is valid.(a null tree)
    * */
    private void deleteCase1(Node node) {
        if (node.parent != null) {
            deleteCase2(node);
        }
    }

    /*
    * If the sibling is RED, then the parent is BLACK(property No.4)
    * We recolor the sibling BLACK, the parent RED and do LeftRotation/RightRotation on the parent if node is left/right node
    * Now node's sibling is BLACK(previous sibling's child, for property No.4, it is BLACK), the parent is RED(previous parent, does not change)
    * Property No.5 is still not valid.
    * This step is to make sure the sibling is BLACK.
    * 此次变换不更改通过原父节点的子树的两条路径上的黑色节点数目，只是将兄弟节点变换为黑色节点
    * */
    private void deleteCase2(Node node) {
        Node s = getSibling(node);
        if (s.color == RED) {
            node.parent.color = RED;
            s.color = BLACK;
            if (node == node.parent.left) {
                rotateLeft(node.parent);
            } else {
                rotateRight(node.parent);
            }
        }
        // 此时条件可以直接转到deleteCase4处理
        deleteCase3(node);
    }

    /*
    * 兄弟节点为黑色，兄弟节点的儿子节点都为黑色，父节点为红色
    * If the sibling and sibling's children are all BLACK, but the parent is RED,
    * In this situation, we simply exchange the color of the sibling and the parent.交换父节点和兄弟节点的颜色
    * This does not affect the number of the BLACK nodes on the route over the sibling, but 它在通过N的路径上对黑色节点数目增加了一，添补了在这些路径上删除的黑色节点。
    * S和S的儿子都是黑色，但是N的父亲是红色。在这种情形下，我们简单的交换N的兄弟和父亲的颜色。
    * 这不影响不通过N的路径的黑色节点的数目，但是它在通过N的路径上对黑色节点数目增加了一，添补了在这些路径上删除的黑色节点。
    * Property No.5 is valid，直接结束
    * */
    private void deleteCase3(Node node) {
        Node s = getSibling(node);
        if (node.parent.color == RED && s.color == BLACK && s.left.color == BLACK && s.right.color == BLACK) {
            s.color = RED;
            node.parent.color = BLACK;
        } else {
            deleteCase4(node);
        }
    }

    /*
    * 兄弟节点为黑色，兄弟节点的儿子节点都为黑色，父节点为黑色
    * If the parent, the sibling and the sibling's children are all black,
    * in this situation, we recolor the sibling RED.
    * For the parent, property No.5 is valid since the number of BLACK nodes on the route over the sibling is reduced by 1 and
    * becomes the same with that on the route over node.
    * However, for the parent's parent, property No.5 is not valid, 通过P的所有路径现在比不通过P的路径少了一个黑色节点，
    * so we need to redo the process over the parent from the beginning.此时，所有经过parent到达叶子节点的路径上黑色节点的数目少一，直接重新对parent进行从头开始的调整
    * */
    private void deleteCase4(Node node) {
        Node s = getSibling(node);
        if (node.parent.color == BLACK && s.color == BLACK && s.left.color == BLACK && s.right.color == BLACK) {
            s.color = RED;
            deleteCase1(node.parent);
        } else {
            deleteCase5(node);
        }
    }

    /*
    * 兄弟节点为黑色，兄弟节点的儿子节点中，一个为黑色，一个为红色，父节点的颜色必为黑色，deleteCase4中已经将父节点为红色的情况排除
    * */

    /*
    *  兄弟节点的红色儿子节点和替换后节点在同一边
    *  If the sibling is BLACK, the left child of the sibling is RED, the right child of the sibling is BLACK, node is the left child of the parent
    *  S是黑色，S的左儿子是红色，S的右儿子是黑色，而N是它父亲的左儿子。
    *  在这种情形下我们在S上做右旋转，这样S的左儿子成为S的父亲和N的新兄弟。我们接着交换S和它的新父亲的颜色。
    *  所有路径仍有同样数目的黑色节点，但是现在N有了一个黑色兄弟，他的右儿子是红色的，所以我们进入了情形6。
    *  N和它的父亲都不受这个变换的影响。
    * */
    private void deleteCase5(Node node) {
        Node s = getSibling(node);
        if (s.color == BLACK) {
            if (node == node.parent.left && s.right.color == BLACK && s.left.color == RED) {
                s.color = RED;
                s.left.color = BLACK;
                rotateRight(s);
            }
        } else {
            if (node == node.parent.right && s.left.color == BLACK && s.right.color == RED) {
                s.color = RED;
                s.right.color = BLACK;
                rotateLeft(s);
            }
        }
        deleteCase6(node);
    }

    /*
    * 兄弟节点的红色儿子节点和替换后节点不在同一边
    * S是黑色，S的右儿子是红色，而N是它父亲的左儿子。
    * 在这种情形下我们在N的父亲上做左旋转，这样S成为N的父亲（P）和S的右儿子的父亲。
    * 我们接着交换N的父亲和S的颜色，并使S的右儿子为黑色。
    * 子树在它的根上的仍是同样的颜色，所以性质3没有被违反。
    * 但是，N现在增加了一个黑色祖先：要么N的父亲变成黑色，要么它是黑色而S被增加为一个黑色祖父。
    * 所以，通过N的路径都增加了一个黑色节点。
    * 此时，如果一个路径不通过N，则有两种可能性：
    * 1. 它通过N的新兄弟。那么它以前和现在都必定通过S和N的父亲，而它们只是交换了颜色。所以路径保持了同样数目的黑色节点。
    * 2. 它通过N的新叔父，S的右儿子。那么它以前通过S、S的父亲和S的右儿子，但是现在只通过S，它被假定为它以前的父亲的颜色，和S的右儿子，它被从红色改变为黑色。
    *    合成效果是这个路径通过了同样数目的黑色节点。
    * 在任何情况下，在这些路径上的黑色节点数目都没有改变。所以我们恢复了性质4。
    * 在示意图中的白色节点可以是红色或黑色，但是在变换前后都必须指定相同的颜色。
    * */
    private void deleteCase6(Node node) {
        Node s = getSibling(node);

        s.color = node.parent.color;
        node.parent.color = BLACK;

        if (node == node.parent.left) {
            s.right.color = BLACK;
            rotateLeft(node.parent);
        } else {
            s.left.color = BLACK;
            rotateRight(node.parent);
        }
    }


}
