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

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *
 * @author ANTONIO
 */
public class SCircularLinkedList<E> {

    private int size = 0;

    private Node<E> first;
    private Node<E> last;

    public boolean add(E e) {
        final Node<E> newNode;
        if (first == null) {
            newNode = new Node<>(null, e, null);
            first = newNode;
            last = newNode;
            newNode.prev = newNode;
            newNode.next = newNode;
        } else {
            newNode = new Node<>(last, e, first);
            first.prev = newNode;
            last.next = newNode;
            last = newNode;
        }
        size++;
        return true;
    }
    
    private void unlink(Node n){
        if(n == n.prev){
            first = null;
            last = null;
        }else{
            n.prev.next = n.next;
            n.next.prev = n.prev;
            if(n == first){
                first = n.prev;
            }
        }
        n.item = null; //TODO: Does this help in Garbage Collection?
        size--;
    }

    public int size(){
        return size;
    }
    
    public class CircularIterator {

        private final Node<E> start;
        private Node<E> current;

        public CircularIterator() {
            start = first;
            current = first;
        }

        public E get() {
            return current.item;
        }

        public E getRel(int posRel){
            Node<E> node = current;
            if(posRel > 0){
                for(int i = 0; i < posRel; i++){
                    node = node.next;
                }
            }else if(posRel < 0){
                for(int i = 0; i < posRel; i++){
                    node = node.prev;
                }
            }
            return node.item;
        }
        
        public void move() {
            current = current.next;
        }

        public E next() {
            E item = current.item;
            current = current.next;
            return item;
        }

        public boolean hasNext() {
            return current.next != start;
        }
        
        public void remove(){
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current.item = null; 
            current = current.prev;
            size--;
        }

    };

    private static class Node<E> {

        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    };

}
