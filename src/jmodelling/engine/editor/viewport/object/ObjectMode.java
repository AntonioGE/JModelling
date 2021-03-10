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

import com.jogamp.opengl.GL2;
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
import jmodelling.engine.editor.viewport.object.tools.Rotate;
import jmodelling.engine.editor.viewport.object.tools.Scale;
import jmodelling.engine.object.Object3D;
import jmodelling.gui.display.EditorDisplayGL;

/**
 *
 * @author ANTONIO
 */
public class ObjectMode extends Mode {

    public static final String NAME = "OBJECT MODE";
    private Tool tool;

    public ObjectMode(View3D editor, Engine engine) {
        super(editor, engine);

        tool = new Navigate(editor, this);
    }

    @Override
    public void init(GLAutoDrawable glad) {
        if (tool != null) {
            tool.init(glad);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        if (tool != null) {
            tool.dispose(glad);
        }
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        engine.scene.updateGL(gl);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        /**
         * Render unselected objects
         */
        engine.scene.getUnselectedObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        /**
         * Render HUD
         */
        gl.glDisable(GL2.GL_LIGHTING);
        engine.scene.getHudObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        /**
         * Render selected objects outline
         */
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDepthMask(false);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glLineWidth(2);

        final Object3D lastSelected = engine.scene.getLastSelectedObject();
        gl.glColor3f(0.945f, 0.345f, 0.0f);
        for (Object3D obj : engine.scene.getSelectedObjects()) {
            if (obj != lastSelected) {
                obj.renderWireframe(gl);
            }
        }
        if (lastSelected != null) {
            gl.glColor3f(1.0f, 0.66f, 0.251f);
            lastSelected.renderWireframe(gl);
        }

        /**
         * Render selected objects
         */
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDepthMask(true);
        gl.glLineWidth(1);
        engine.scene.getSelectedObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL2.GL_LIGHTING);

        if (tool != null) {
            tool.display(glad);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        if (tool != null) {
            tool.reshape(glad, x, y, width, height);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (tool != null) {
            tool.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (tool != null) {
            tool.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (tool != null) {
            tool.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (tool != null) {
            tool.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (tool != null) {
            tool.mouseExited(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (tool != null) {
            tool.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (tool != null) {
            tool.mouseMoved(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (tool != null) {
            tool.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setDefaultTool();
            editor.panel.repaint();
        } else if (tool != null) {
            tool.keyPressed(e);
        } else {
            changeMode(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (tool != null) {
            tool.keyReleased(e);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (tool != null) {
            tool.mouseWheelMoved(e);
        }
    }

    @Override
    public void destroy() {
        if (this.tool != null) {
            tool.destroy();
        }
    }
    
    public void changeMode(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                setDefaultTool();
                break;
            }

            case KeyEvent.VK_G: {
                if (editor.getScene().isAnyObjectSelected()) {
                    tool = new Grab(editor, this);
                }
                break;
            }

            case KeyEvent.VK_R: {
                if (editor.getScene().isAnyObjectSelected()) {
                    tool = new Rotate(editor, this);
                }
                break;
            }
            case KeyEvent.VK_S: {
                if (editor.getScene().isAnyObjectSelected()) {
                    tool = new Scale(editor, this);
                }
                break;
            }
        }
    }

    public void setTool(ObjectTool tool) {
        if (this.tool != null) {
            tool.destroy();
        }
        this.tool = tool;
    }

    public void setDefaultTool() {
        setTool(new Navigate(editor, this));
    }

    @Override
    public String getName() {
        return NAME;
    }

    
    
}
