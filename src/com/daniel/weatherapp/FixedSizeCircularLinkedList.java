package com.daniel.weatherapp;

public class FixedSizeCircularLinkedList {
    private Node head = null;
    private Node tail = null;

    public class Node {
        long time;
        Node next;

        public Node(long time) {
            this.time = time;
        }
    }

    public FixedSizeCircularLinkedList(int numOfNodes, int timeInMinutes) { // linked list initialization with fixed size
        long initTime = (System.currentTimeMillis() / 1000) - (timeInMinutes * 60); // set the data of each node with
                                                                                    // the current time minus the limit
                                                                                    // time, so the first X request will
                                                                                    // automatically inserted to the list.
        for (int i = 0; i < numOfNodes; i++) {
            addNode(initTime);
        }
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void addNode(long time) {
        Node newNode = new Node(time);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        tail.next = head;
    }

    public void printList() {
        Node currentNode = head;

        if (head != null) {
            do {
                System.out.print(currentNode.time + " ");
                currentNode = currentNode.next;
            } while (currentNode != head);
        }
    }

}
