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

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author ANTONIO
 */
public class CircularLinkedHashSet<E> extends CircularLinkedList<E> {

    protected HashSet<E> set;

    public CircularLinkedHashSet() {
        set = new HashSet<>();
    }

    @Override
    public boolean add(E e) {
        if (set.contains(e)) {
            return false;
        } else {
            super.add(e);
            set.add(e);
            return true;
        }
    }

    @Override
    protected boolean unlink(Node<E> node) {
        if (super.unlink(node)) {
            set.remove(node.item);
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
