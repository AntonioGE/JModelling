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
package jmodelling.utils.collections.nodes;

/**
 *
 * @author ANTONIO
 */
public class NodeLinkList <E>{
    
    private int size = 0;
    private Node<E> first;
    private Node<E> last;
    
    public boolean add(E e){
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        return true;
    }
    
    private void unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
    }
    
    public void remove(Node n) {
        unlink(n);
    }
    
    public class Iterator implements NodeIterator<E>{

        private Node<E> current;
        private int currentNode;
        
        @Override
        public E getCurrent() {
            return current.item;
        }

        @Override
        public E getNext() {
            return current.next.item;
        }

        @Override
        public E getPrev() {
            return current.next.item;
        }

        @Override
        public Node<E> getCurrentNode() {
            return current;
        }

        @Override
        public Node<E> getNextNode() {
            return current.next;
        }

        @Override
        public Node<E> getPrevNode() {
            return current.prev;
        }

        @Override
        public void move() {
            current = current.next;
            currentNode++;
        }

        @Override
        public boolean hasNext() {
            return currentNode < size;
        }

        @Override
        public void remove() {
            unlink(current);
            currentNode--;
        }
        
    }
    
}
