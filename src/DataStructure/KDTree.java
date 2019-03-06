package DataStructure;

public class KDTree {

    private Vector[] vectors;
    private Node root;

    public KDTree(Vector[] vectors) {
        this.vectors = vectors;
        root = buildTree(0, vectors.length);
    }

    public Vector searchClosest(Vector vector) {
        return search(vector, root);
    }

    private Vector search(Vector vector, Node node) {
        if (node.dim == -1) {
            if (node.vector2 != null) {
                return vector.calDis(node.vector1) < vector.calDis(node.vector2) ? node.vector1 : node.vector2;
            }
            return node.vector1;
        }
        Vector resVector;
        if (vector.getValue(node.dim) <= node.value) {
            resVector = search(vector, node.leftChild);
            double bestDis = vector.calDis(resVector);
            if (bestDis > vector.getValue(node.dim) - node.value) {
                Vector aVector = search(vector, node.rightChild);
                return vector.calDis(aVector) < bestDis ? aVector : resVector;
            }
            return resVector;
        } else {
            resVector = search(vector, node.rightChild);
            double bestDis = vector.calDis(resVector);
            if (bestDis > vector.getValue(node.dim) - node.value) {
                Vector aVector = search(vector, node.leftChild);
                return vector.calDis(aVector) < bestDis ? aVector : resVector;
            }
            return resVector;
        }
    }


    private Node buildTree(int l, int r) {
        if (r - l <= 2) {
            Node node = new Node();
            node.vector1 = vectors[l];
            if (r - 1 > l) {
                node.vector2 = vectors[r - 1];
            }
            return node;
        }
        double maxInvariance = 0.0;
        int dim = -1;
        double variance;
        for (int i = 0; i < vectors[l].values.length; i ++) {
            variance = calVariance(i, l, r);
            System.out.println(variance);
            if (variance > maxInvariance) {
                maxInvariance = variance;
                dim = i;
            }
        }
        qsort(l, r - 1, dim);
        int value = vectors[(l + r - 1) / 2].getValue(dim);
        Node node = new Node(dim, value);
        node.leftChild = buildTree(l, (l + r - 1) / 2 + 1);
        node.rightChild = buildTree((l + r - 1) / 2 + 1, r);
        return node;
    }

    private double calVariance(int dim, int l, int r) {
        double average = 0.0;
        for (int i = l; i < r; i ++) {
            average += vectors[i].getValue(dim);
        }
        average /= ( r - l );
        double variance = 0.0;
        for (int i = l; i < r; i ++) {
            double diff = vectors[i].getValue(dim) - average;
            variance += diff * diff;
        }
        return variance / ( r - l );
    }

    private void qsort(int l, int r, int dim) {
        int i = l, j = r;
        int compare = vectors[(i + j) / 2].getValue(dim);
        Vector tmpVector;
        while (i <= j) {
            while (vectors[i].getValue(dim) < compare) i ++;
            while (vectors[j].getValue(dim) > compare) j --;
            if (i <= j) {
                tmpVector = vectors[i];
                vectors[i] = vectors[j];
                vectors[j] = tmpVector;
                i ++; j --;
            }
        }
        if (i < r) qsort(i, r, dim);
        if (l < j) qsort(l, j, dim);
    }

    private StringBuilder traverse(Node node) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(node);
        if (node != null && node.dim != -1) {
            stringBuilder.append("{");
            stringBuilder.append(traverse(node.leftChild));
            stringBuilder.append(",");
            stringBuilder.append(traverse(node.rightChild));
            stringBuilder.append("}");
        }
        return stringBuilder;
    }

    @Override
    public String toString() {
        return traverse(root).toString();
    }

    private class Node {

        private Node leftChild, rightChild;
        private int dim;
        private int value;
        private Vector vector1, vector2;

        public Node() {
            dim = -1;
        }

        public Node(int dim, int value) {
            this.dim = dim;
            this.value = value;
        }

       @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (dim == -1) {
                stringBuilder.append(vector1.toString());
                if (vector2 != null) {
                    stringBuilder.append("+");
                    stringBuilder.append(vector2.toString());
                }
            } else {
                stringBuilder.append("<");
                stringBuilder.append(dim);
                stringBuilder.append(",");
                stringBuilder.append(value);
                stringBuilder.append(">");
            }
            return stringBuilder.toString();
        }
    }


    private static class Vector {

        private int[] values;

        public Vector(int[] values) {
            this.values = values;
        }

        public int getValue(int dim) {
            return values[dim];
        }

        public double calDis(Vector vector) {
            double sum = 0.0;
            for (int i = 0; i < values.length; i ++) {
                double diff = (double) (getValue(i) - vector.getValue(i));
                sum += diff*diff;
            }
            return Math.sqrt(sum);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (int i = 0; i < values.length - 1; i ++) {
                stringBuilder.append(getValue(i));
                stringBuilder.append(",");
            }
            stringBuilder.append(getValue(values.length - 1));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }

    }

    public static void main(String[] args) {

        Vector[] vectors = new Vector[6];
        vectors[0] = new Vector(new int[]{2,3});
        vectors[1] = new Vector(new int[]{5,4});
        vectors[2] = new Vector(new int[]{4,7});
        vectors[3] = new Vector(new int[]{8,1});
        vectors[4] = new Vector(new int[]{7,2});
        vectors[5] = new Vector(new int[]{9,6});

        KDTree kdTree = new KDTree(vectors);
        System.out.println(kdTree.toString());

        Vector vector = new Vector(new int[]{8,3});
        Vector resVector = kdTree.searchClosest(vector);
        System.out.println(resVector.toString());
    }
}
