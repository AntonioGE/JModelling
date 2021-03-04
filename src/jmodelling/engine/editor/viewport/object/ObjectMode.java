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
package jmodelling.engine.editor.viewport.object;

import com.jogamp.opengl.GLAutoDrawable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import jmodelling.engine.Engine;
import jmodelling.engine.editor.Tool;
import jmodelling.engine.editor.viewport.Mode;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.object.tools.Grab;
import jmodelling.engine.editor.viewport.object.tools.Navigate;
import jmodelling.engine.editor.viewport.object.tools.ObjectTool;
import jmodelling.gui.display.EditorDisplayGL;

/**
 *
 * @author ANTONIO
 */
public class ObjectMode extends Mode {

    private Tool tool;

    public ObjectMode(View3D editor, Engine engine) {
        super(editor, engine);

        tool = new Navigate(editor, this);
    }

    @Override
    public void init(EditorDisplayGL panel, GLAutoDrawable glad) {
        if (tool != null) {
            tool.init(panel, glad);
        }
    }

    @Override
    public void dispose(EditorDisplayGL panel, GLAutoDrawable glad) {
        if (tool != null) {
            tool.dispose(panel, glad);
        }
    }

    @Override
    public void display(EditorDisplayGL panel, GLAutoDrawable glad) {
        if (tool != null) {
            tool.display(panel, glad);
        }
    }

    @Override
    public void reshape(EditorDisplayGL panel, GLAutoDrawable glad, int x, int y, int width, int height) {
        if (tool != null) {
            tool.reshape(panel, glad, x, y, width, height);
        }
    }

    @Override
    public void mouseClicked(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mouseClicked(panel, e);
        }
    }

    @Override
    public void mousePressed(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mousePressed(panel, e);
        }
    }

    @Override
    public void mouseReleased(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mouseReleased(panel, e);
        }
    }

    @Override
    public void mouseEntered(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mouseEntered(panel, e);
        }
    }

    @Override
    public void mouseExited(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mouseExited(panel, e);
        }
    }

    @Override
    public void mouseDragged(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mouseDragged(panel, e);
        }
    }

    @Override
    public void mouseMoved(EditorDisplayGL panel, MouseEvent e) {
        if (tool != null) {
            tool.mouseMoved(panel, e);
        }
    }

    @Override
    public void keyTyped(EditorDisplayGL panel, KeyEvent e) {
        if (tool != null) {
            tool.keyTyped(panel, e);
        }
    }

    @Override
    public void keyPressed(EditorDisplayGL panel, KeyEvent e) {
        if (tool != null) {
            tool.keyPressed(panel, e);
        } else {
            changeMode(e);
        }
    }

    @Override
    public void keyReleased(EditorDisplayGL panel, KeyEvent e) {
        if (tool != null) {
            tool.keyReleased(panel, e);
        }
    }

    @Override
    public void mouseWheelMoved(EditorDisplayGL panel, MouseWheelEvent e) {
        if (tool != null) {
            tool.mouseWheelMoved(panel, e);
        }
    }

    public void changeMode(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                setDefaultTool();
            }
            break;
            case KeyEvent.VK_G: {
                if (editor.getScene().isAnyObjectSelected()) {
                    tool = new Grab(editor, this);
                }
            }
            break;
        }
    }

    public void setTool(ObjectTool tool) {
        this.tool = tool;
    }

    public void setDefaultTool() {
        this.tool = new Navigate(editor, this);
    }

}
