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
package jmodelling.engine.editor.viewport.edit.render;

import com.jogamp.opengl.GL2;
import jmodelling.engine.editor.viewport.edit.EditMode;
import jmodelling.engine.editor.viewport.render.ViewRenderer;
import jmodelling.engine.object.Object3D;
import jmodelling.math.vec.Vec3f;

/**
 *
 * @author ANTONIO
 */
public class EditRenderer extends ViewRenderer {

    private EditMode mode;

    public EditRenderer(EditMode mode) {
        this.mode = mode;
    }

    @Override
    public void render(GL2 gl) {
        if (mode.obj.emesh.edited || mode.obj.emesh.resized) {
            mode.engine.scene.update(mode.obj);
        }

        mode.engine.scene.updateGL(gl);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        /**
         * Render object to edit
         */
        gl.glMultMatrixf(mode.obj.getModelMatrix().toArray(), 0);
        mode.obj.emeshGL.render(gl);

        gl.glPopMatrix();
        mode.obj.renderOpaque(gl);

        /**
         * Render unselected objects
         */
        mode.engine.scene.getUnselectedObjects().forEach((obj) -> {
            obj.renderOpaque(gl);
        });

        /**
         * Render HUD
         */
        gl.glDisable(GL2.GL_LIGHTING);
        mode.engine.scene.getHudObjects().forEach((obj) -> {
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

        final Object3D lastSelected = mode.engine.scene.getLastSelectedObject();
        gl.glColor3f(0.945f, 0.345f, 0.0f);
        for (Object3D obj : mode.engine.scene.getSelectedObjects()) {
            if (obj != lastSelected) {
                obj.renderWireframe(gl);
            }
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
        mode.engine.scene.getSelectedObjects().forEach((obj) -> {
            if (obj != lastSelected) {
                obj.renderOpaque(gl);
            }
        });

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL2.GL_LIGHTING);
    }

}
