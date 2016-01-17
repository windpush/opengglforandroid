package tao.jerry.windpush.opengglforandroid.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import tao.jerry.windpush.opengglforandroid.R;
import tao.jerry.windpush.opengglforandroid.helper.ShaderHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glViewport;


/**
 * Created by Administrator on 2016/1/9.
 */
public class SimpleRender implements GLSurfaceView.Renderer {
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR ="u_Color";

    private int uColorLocation;
    private int getApositionLocation=0;
    private int mVertexShader = 0;
    private int mFragmentShader = 0;
    private int mProgram = 0;
    private int apositionLocation = 0;
    float[] tableVertices = {
            -0.5f, 0f,
            0f, 0.5f,
            0f,-0.5f,
    };
    float[] tableVerticesWithTriangles =

            {
                    //triangles 1
                    -0.5f, -0.5f,0.7f,0.7f,0.7f,
                    0.5f, -0.5f,0.7f,0.7f,0.7f,
                    0.5f, 0.5f,0.7f,0.7f,0.7f,
                    //triangles 2
                    -0.5f, -0.5f,0.7f,0.7f,0.7f,
                    -0.5f, 0.5f,0.7f,0.7f,0.7f,
                    0.5f, 0.5f,0.7f,0.7f,0.7f,

                                        //line 1
                    -0.5f,0f,0.7f,0.7f,0.7f,
                    0.5f,0f,0.7f,0.7f,0.7f,

                    //Mallets
                    0f, -0.25f,0f,0f,1f,
                    0f,0.25f,1f,0f,0f,
            };


    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context mContext;

    public SimpleRender(Context context) {
        this.mContext = context;
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);



    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = readShaderFromTxt(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = readShaderFromTxt(mContext, R.raw.simple_fragment_shader);
        //compileShader and FragmentShader  must be called after surfaceCreated
        mVertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        mFragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        mProgram = ShaderHelper.linkProgram(mVertexShader, mFragmentShader);
        if (ShaderHelper.validateProgram(mProgram)) {
            GLES20.glUseProgram(mProgram);
        }
        uColorLocation = GLES20.glGetUniformLocation(mProgram, U_COLOR);
        apositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        vertexData.position(0);
        //// TODO: 16/1/17 something need to deeply study
        GLES20.glVertexAttribPointer(apositionLocation, 5, GL_FLOAT,false,0,vertexData);
        glEnableVertexAttribArray(apositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        /**
         * first objetc is Mode, second is start postion three is endpoistion
         */
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 0.0f);
        glDrawArrays(GLES20.GL_LINES,6,2);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 0.0f);
        glDrawArrays(GLES20.GL_POINTS,8,1);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 0.0f);
        glDrawArrays(GLES20.GL_POINTS,9,1);


    }

    public static String readShaderFromTxt(Context context, int resourceId) {
        StringBuilder shaderBuilder = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputReader);
            String nextLine;
            while ((nextLine = bufferReader.readLine()) != null) {
                shaderBuilder.append(nextLine);
                shaderBuilder.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shaderBuilder.toString();
    }
}
