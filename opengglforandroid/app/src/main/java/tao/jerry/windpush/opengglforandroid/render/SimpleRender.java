package tao.jerry.windpush.opengglforandroid.render;

import static android.opengl.GLES20.*;

import android.content.Context;
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

/**
 * Created by Administrator on 2016/1/9.
 */
public class SimpleRender implements GLSurfaceView.Renderer {
    float[] tableVertices = {
            0f, 0f,
            0f, 14f,
            9f, 14f,
            9f, 0f
    };
    float[] tableVerticesWithTriangles =

            {
                    //triangles 1
                    0f, 0f,
                    9f, 14f,
                    0f, 14f,
                    //triangles 2
                    0f, 0f,
                    9f, 0f,
                    9f, 14f
            };


    private static final int BYTES_PER_FLOAT =4 ;
    private final FloatBuffer vertexData;
    private final Context mContext;
    public SimpleRender(Context context) {
        this.mContext =context;
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
        String vertexShaderSource =readShaderFromTxt(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource =readShaderFromTxt(context, R.raw.simple_fragment_shader);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static String readShaderFromTxt(Context context,int resourceId){
        StringBuilder shaderBuilder = new StringBuilder();
        try {
            InputStream inputStream =context.getResources().openRawResource(resourceId);
            InputStreamReader inputReader = new  InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputReader);
            String nextLine;
            while ((nextLine = bufferReader.readLine())!=null){
                shaderBuilder.append(nextLine);
                shaderBuilder.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return shaderBuilder.toString();
    }
}
