package DataStructure;

public class PriorityQueue {


    // use heap to realize it
    // for heap, e.g. big root heap
    // each child's value is less than the root's


    class Node {
        int priority;
        Object value;
        Node(int priority, Object value) {
            this.priority = priority;
            this.value = value;
        }


    }

    private Node[] heap;
    private int count;

    PriorityQueue(int size) {
        heap = new Node[size];
        count = 0;
    }

    public Node pop() {
        if (count == 0) return null;
        Node pop = heap[0];
        swap(0, count-1);
        count --;
        downdate();
        return pop;
    }

    public void push(int priority, Object value) {
        if (count >= heap.length) return ;
        heap[count++] = new Node(priority, value);
        update();
    }

    private void update() {
        int index = count-1;
        while (true) {
            if (index == 0) break;
            int parent = (index+1)/2-1;
            if (heap[parent].priority < heap[index].priority) {
                swap(parent, index);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void downdate() {
        int index = 0;
        while (true) {
            int child0 = (index+1)*2-1;
            if (child0 >= count) break;
            int child1 = (index+1)*2;
            if (child1 >= count) {
                if (heap[index].priority < heap[child0].priority) {
                    swap(index, child0);
                }
                break;
            }
            if (heap[index].priority < heap[child0].priority && heap[child0].priority > heap[child1].priority) {
                swap(index, child0);
                index = child0;
                continue;
            }
            if (heap[index].priority < heap[child1].priority && heap[child0].priority < heap[child1].priority) {
                swap(index, child1);
                index = child1;
                continue;
            }
            break;
        }
    }

    private void swap(int parent, int child) {
        Node swap = heap[parent];
        heap[parent] = heap[child];
        heap[child] = swap;
    }

    public static void main(String[] args) {
        PriorityQueue priorityQueue = new PriorityQueue(100);
        for (int i = 0; i < 10; i ++) {
            priorityQueue.push(i,10-i);
            }
        for (int i = 0; i < 10; i ++) {
            System.out.print(priorityQueue.pop().value + " ");
        }
    }


}
