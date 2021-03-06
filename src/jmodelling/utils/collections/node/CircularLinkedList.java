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
package jmodelling.utils.collections.node;

import java.util.Iterator;

/**
 *
 * @author ANTONIO
 */
public class CircularLinkedList<E> implements Iterable<E> {

    protected Node<E> first;
    protected int size;

    public Node<E> add(E e) {
        final Node<E> newNode;
        if (first == null) {
            newNode = new Node(null, e, null);
            first = newNode;
            newNode.prev = newNode;
            newNode.next = newNode;
        } else {
            newNode = new Node(first.prev, e, first);
            first.prev.next = newNode;
            first.prev = newNode;
        }
        size++;
        return newNode;
    }

    protected boolean unlink(Node<E> n) {
        if (first == null || n == null) {
            return false;
        }

        if (n == n.prev) {
            first = null;
        } else {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            if (n == first) {
                first = n.next;//first = n.prev;
            }
        }
        //n.item = null; //TODO: Does this help in Garbage Collection?
        size--;
        return true;
    }
    
    public int size(){
        return size;
    }
    
    public Node<E> getFirstNode(){
        return first;
    }
    
    public Node<E> getLastNode(){
        return first.prev;
    }
    
    public E getFirst(){
        return first.item;
    }
    
    public E getLast(){
        return first.prev.item;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularIterator(first);
    }
    
    public NodeIterator<E> nodeIterator(){
        return new CircularIterator(first);
    }
    
    public class CircularIterator implements NodeIterator<E> {

        protected Node<E> next;
        protected Node<E> lastReturned;
        protected int nextNode;

        public CircularIterator(Node<E> first) {
            next = first;
            lastReturned = next.prev;
            nextNode = 0;
        }

        @Override
        public boolean hasNext() {
            return nextNode < size;
        }

        @Override
        public E next() {
            lastReturned = next;
            next = lastReturned.next;
            nextNode++;
            return lastReturned.item;
        }

        @Override
        public Node<E> getCurrentNode() {
            return lastReturned;
        }

        @Override
        public Node<E> getPrevNode() {
            return lastReturned.prev;
        }

        @Override
        public Node<E> getNextNode() {
            return lastReturned.next;
        }

        @Override
        public E getCurrent() {
            return lastReturned.item;
        }

        @Override
        public E getPrev() {
            return lastReturned.prev.item;
        }

        @Override
        public E getNext() {
            return lastReturned.next.item;
        }

        @Override
        public void remove() {
            unlink(lastReturned);
            nextNode--;
        }

    }

}
