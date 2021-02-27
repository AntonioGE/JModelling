/*
 * MIT License
 * 
 * Copyright (c) 2021 Antonio GE
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmodelling.utils.collections;

/**
 *
 * @author ANTONIO
 */
public class CircularLinkedList<E> {

    private int size = 0;

    private Node<E> first;

    public boolean add(E e) {
        final Node<E> newNode;
        if (first == null) {
            newNode = new Node<>(null, e, null);
            first = newNode;
            newNode.prev = newNode;
            newNode.next = newNode;
        } else {
            newNode = new Node<>(first.prev, e, first);
            first.prev.next = newNode;
            first.prev = newNode;
        }
        size++;
        return true;
    }
    
    public void remove(Node n){
        unlink(n);
    }

    private void unlink(Node n) {
        if (first == null) {
            return;
        }

        if (n == n.prev) {
            first = null;
        } else {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            if (n == first) {
                first = n.prev;
            }
        }
        n.item = null; //TODO: Does this help in Garbage Collection?
        size--;
    }

    public int size() {
        return size;
    }

    public CircularLinkedList<E>.CircularIterator<E> getIterator() {
        return new CircularIterator(first);
    }

    public class CircularIterator<E> {

        private Node<E> current;
        private int currentNode;

        private CircularIterator(Node<E> first) {
            current = first;
            currentNode = 0;
        }

        public E getRel(int posRel) {
            Node<E> node = current;
            if (posRel > 0) {
                for (int i = 0; i < posRel; i++) {
                    node = node.next;
                }
            } else if (posRel < 0) {
                for (int i = 0; i < posRel; i++) {
                    node = node.prev;
                }
            }
            return node.item;
        }

        public E getCurrent(){
            return current.item;
        }
        
        public E getNext() {
            return current.next.item;
        }

        public E getPrev() {
            return current.prev.item;
        }
        
        public Node<E> getCurrentNode(){
            return current;
        }

        public Node<E> getNextNode(){
            return current.next;
        }
        
        public Node<E> getPrevNode(){
            return current.prev;
        }
        
        public void move() {
            current = current.next;
            currentNode++;
        }

        public boolean hasNext() {
            return currentNode < size;
        }

        public void remove() {
            unlink(current);
            currentNode--;
        }

    };

    public class Node<E> {

        public E item;
        public Node<E> next;
        public Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    };
}