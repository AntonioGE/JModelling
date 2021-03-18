# JModelling (Under development)
JModelling is a cross platform 3D Modelling Tool that imitates some of the modelling features from Blender. It uses Java and does not depent on external libraries apart from JOGL.

The overall goal of this project is to use it as a learning mechanism for Java, OpenGL, 3D graphics and computational geometry.
The tool does not use the modern OpenGL 4 + GLSL pipeline. Instead, it uses OpenGL 2 with VBOs so that it can be compatible with more computers.

### Features added so far
* **OBJ model import**
* **N-Gon support** (ear clipping triangulation)
* **Object selection** (raytracing)
* **Object outline drawing** (without stencil buffer)
* **Object transformations**:
  * **Translation**: Planar and along axis
  * **Rotation**: Planar, along axis and ball
  * **Scaling**: Along axis and global
* **Arcball camera** (perspective and orthogonal)
* **Mesh editor**:
  * **Vertex selection**

### Screenshot
<p align="center">
  <img src="res/Screenshot.png">
</p>
