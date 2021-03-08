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

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author ANTONIO
 */
public class CircularLinkedHashSet<E> extends CircularLinkedList<E> {

    protected HashMap<E, Node<E>> map;

    public CircularLinkedHashSet() {
        map = new HashMap<>();
    }

    @Override
    public Node<E> add(E e) {
        if (map.containsKey(e)) {
            return null;
        } else {
            Node<E> node = super.add(e);
            map.put(node.item, node);
            return node;
        }
    }

    @Override
    protected boolean unlink(Node<E> node) {
        if (super.unlink(node)) {
            map.remove(node.item);
            return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularIterator(first);
    }
    
    @Override
    public NodeIterator nodeIterator(){
        return new CircularIterator(first);
    }
    
    public Node<E> getNode(E e){
        return map.get(e);
    }
    
    public void removeNode(Node<E> node){
        unlink(node);
    }
    
    public void remove(E e){
        unlink(map.get(e));
    }

    public class CircularIterator extends CircularLinkedList.CircularIterator {

        public CircularIterator(Node first) {
            super(first);
        }

        @Override
        public void remove() {
            unlink(lastReturned);
            nextNode--;
        }

    }

}
