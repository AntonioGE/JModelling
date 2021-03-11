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
package jmodelling.engine.editor.viewport.edit.tools;

import java.util.IdentityHashMap;
import java.util.Set;
import jmodelling.engine.editor.common.TypedFloat;
import jmodelling.engine.editor.viewport.View3D;
import jmodelling.engine.editor.viewport.common.pivot.Pivot;
import jmodelling.engine.editor.viewport.common.pivot.PivotMean;
import jmodelling.engine.editor.viewport.edit.EditMode;
import jmodelling.math.vec.Vec3f;
import jmodelling.utils.CollectionUtils;

/**
 *
 * @author ANTONIO
 */
public abstract class TransformTool extends EditTool {

    protected final int firstMouseX, firstMouseY;
    protected IdentityHashMap<Vec3f, Vec3f> vtxLocs;
    protected Pivot pivot;
    protected TypedFloat transfAmount;
    

    public TransformTool(View3D editor, EditMode editMode) {
        super(editor, editMode);

        firstMouseX = editor.getMouseX();
        firstMouseY = editor.getMouseY();
        
        Set<Vec3f> selectedVtxs = editMode.obj.emesh.selectedVtxs;
        vtxLocs = CollectionUtils.newIdentityHashMap(selectedVtxs.size());
        for(Vec3f vtx : selectedVtxs){
            vtxLocs.put(vtx, vtx.clone());
        }

        pivot = new PivotMean(selectedVtxs);
        transfAmount = new TypedFloat();
    }

    
}
