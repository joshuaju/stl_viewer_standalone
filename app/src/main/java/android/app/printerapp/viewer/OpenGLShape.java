package android.app.printerapp.viewer;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public abstract class OpenGLShape {

	//Axis
    public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;
	//Default line width
    public static final float LINE_WIDTH = 2f;

	protected static final float TRANSPARENCY = 0.5f;

	protected static final float[] X_COLOR = { 0.0f, 0.9f, 0.0f, TRANSPARENCY };
	protected static final float[] Y_COLOR = { 1.0f, 0.0f, 0.0f, TRANSPARENCY };
	protected static final float[] Z_COLOR = { 0.0f, 0.0f, 1.0f, TRANSPARENCY };



	abstract protected float[] drawXAxis(Geometry.Point point, float z);
	
	abstract protected float[] drawYAxis(Geometry.Point point, float z);
	
	abstract protected float[] drawZAxis(Geometry.Point point, float z);

	abstract public void draw(DataStorage data, float[] mvpMatrix, int currentAxis);

	final protected void drawSelectedAxis(DataStorage data, float[] mvpMatrix, int currentAxis, float[] mCoordsArray, float[] mCurrentColor, FloatBuffer mVertexBuffer, int mPositionHandle, int coords_per_vertex, int vertexStride, int mColorHandle, int mProgram, int mMVPMatrixHandle, float lineWidth, int vertexCount) {
		switch (currentAxis){

			case X_AXIS:
				mCoordsArray = drawXAxis(data.getLastCenter(), data.getTrueCenter().z);
				mCurrentColor = X_COLOR;
				break;
			case Y_AXIS:
				mCoordsArray = drawYAxis(data.getLastCenter(), data.getTrueCenter().z);
				mCurrentColor = Y_COLOR;
				break;
			case Z_AXIS:
				mCoordsArray = drawZAxis(data.getLastCenter(), data.getTrueCenter().z);
				mCurrentColor = Z_COLOR;
				break;
			default:
				mCoordsArray = null;
				break;

		}


		if (mCoordsArray !=null) {

			mVertexBuffer.put(mCoordsArray);
			mVertexBuffer.position(0);

			// Prepare the triangle coordinate data
			GLES20.glVertexAttribPointer(mPositionHandle, coords_per_vertex,
					GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

			// get handle to fragment shader's vColor member
			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

			// Set color for drawing the triangle
			GLES20.glUniform4fv(mColorHandle, 1, mCurrentColor, 0);

			// get handle to shape's transformation matrix
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			ViewerRenderer.checkGlError("glGetUniformLocation");

			// Apply the projection and view transformation
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			ViewerRenderer.checkGlError("glUniformMatrix4fv");

			GLES20.glLineWidth(lineWidth);
			GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertexCount);


			// Disable vertex array
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}

}
