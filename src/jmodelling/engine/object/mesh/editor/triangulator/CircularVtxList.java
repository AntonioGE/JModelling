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
package jmodelling.engine.object.mesh.editor.triangulator;

/**
 *
 * @author ANTONIO
 */
class CircularVtxList {

        private CNode first;
        private int size = 0;

        public boolean add(VInd e) {
            final CNode newNode;
            if (first == null) {
                newNode = new CNode(null, e, null);
                first = newNode;
                newNode.prev = newNode;
                newNode.next = newNode;
            } else {
                newNode = new CNode(first.prev, e, first);
                first.prev.next = newNode;
                first.prev = newNode;
            }
            size++;
            return true;
        }

        public void remove(CNode n) {
            unlink(n);
        }

        private void unlink(CNode n) {
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

        public Iterator getIterator() {
            return new Iterator(first);
        }

        public class Iterator {

            private CNode current;
            private int currentNode;

            private Iterator(CNode first) {
                current = first;
                currentNode = 0;
            }

            public VInd getCurrent() {
                return current.item;
            }

            public VInd getNext() {
                return current.next.item;
            }

            public VInd getPrev() {
                return current.prev.item;
            }

            public CNode getCurrentNode() {
                return current;
            }

            public CNode getNextNode() {
                return current.next;
            }

            public CNode getPrevNode() {
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

        }

        public class CNode {

            public VInd item;
            public CNode next;
            public CNode prev;

            CNode(CNode prev, VInd element, CNode next) {
                this.item = element;
                this.next = next;
                this.prev = prev;
            }
        }
    }