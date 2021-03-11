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
package jmodelling.engine.editor.common;

import java.awt.event.KeyEvent;

/**
 *
 * @author ANTONIO
 */
public class TypedFloat {

    private String typedString;
    private boolean negate;

    public TypedFloat() {
        typedString = "";
        negate = false;
    }

    public static boolean isTypingAmount(KeyEvent e) {
        return ("0123456789.-".contains(Character.toString(e.getKeyChar()))
                || e.getKeyCode() == KeyEvent.VK_DELETE)
                || (e.getKeyCode() == KeyEvent.VK_BACK_SLASH);
    }

    private void parseKeyEvent(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isDigit(c)) {
            typedString += c;
        } else if (c == '.') {
            typedString += c;
        } else if (c == '-') {
            negate = !negate;
        } else if ((e.getKeyCode() == KeyEvent.VK_DELETE) || (e.getKeyCode() == KeyEvent.VK_BACK_SLASH)) {
            if (typedString.length() > 0) {
                typedString = typedString.substring(0, typedString.length() - 1);
            }
        }
    }
    
    public void processInput(KeyEvent e){
        if(isTypingAmount(e)){
            parseKeyEvent(e);
        }
    }

    public float getValue() throws NumberFormatException {
        float value = Float.valueOf(typedString);
        if (negate) {
            value = -value;
        }
        return value;
    }

    public boolean isEmpty(){
        return typedString.isEmpty();
    }
    
}
