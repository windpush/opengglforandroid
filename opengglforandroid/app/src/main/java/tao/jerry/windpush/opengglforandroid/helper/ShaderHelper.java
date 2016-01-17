package tao.jerry.windpush.opengglforandroid.helper;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Windpush on 2016/1/10.
 */
public class ShaderHelper {
    private static final String TAG = "FuckOpenGL";
    private static final int STATUS_FAILED =0;


    public static int compileVertexShader(String shaderCode) {
        return glCompileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return glCompileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int glCompileShader(int type, String shaderCode) {
        //create object for shader
        int shaderObjectId = GLES20.glCreateShader(type);
        //check shaderId
        if (shaderObjectId == STATUS_FAILED) {
            Log.e(TAG, "Shader may be has some problems");
            return STATUS_FAILED;
        }
        //if create success ,you can load the source like oil of cars
        GLES20.glShaderSource(shaderObjectId, shaderCode);

        //after source loaded, start to compile
        GLES20.glCompileShader(shaderObjectId);

        //init int[] for status of opengl check if compile successfully ,the result is retrun to int[0]
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        //if status ==0;we need delete the shader to release resource
        if (compileStatus[0] == STATUS_FAILED) {
            GLES20.glDeleteShader(shaderObjectId);
            return STATUS_FAILED;
        }

        //if all things is pass ,the shaderObjectId is useful
        return shaderObjectId;
    }

    /**
     * this is almost like the function of  complieShader
     *
     * @param vertexShaderId
     * @param fragmentShaderId
     * @return
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programId = GLES20.glCreateProgram();

        if (programId == STATUS_FAILED) {
            Log.e(TAG, "Program may be has some problems");
            return 0;
        }
        //the step is important ,like create the world ,you must create some line point shape first,and give color then
        GLES20.glAttachShader(programId, vertexShaderId);

        GLES20.glAttachShader(programId, fragmentShaderId);
        //must link program after attachShader
        GLES20.glLinkProgram(programId);

        final int[] linkStatus = new int[1];

        GLES20.glGetProgramiv(programId,GLES20.GL_LINK_STATUS,linkStatus,0);

        if (linkStatus[0]==STATUS_FAILED){
            GLES20.glDeleteProgram(programId);
            return STATUS_FAILED;
        }

        return programId;
    }


    public static boolean validateProgram(int programObjectId){
        GLES20.glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId,GLES20.GL_VALIDATE_STATUS,validateStatus,0);
        Log.e(TAG,"Result openglLog" + GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0]!=STATUS_FAILED;
    }
}
