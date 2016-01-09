package tao.jerry.windpush.opengglforandroid.activity;

import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import tao.jerry.windpush.opengglforandroid.render.SimpleRender;


public class MainActivity extends ActionBarActivity {
    private boolean mRenderSet = false;
    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(mGLSurfaceView);
    }

    private void init() {
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new SimpleRender());
        mRenderSet = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRenderSet) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRenderSet) {
            mGLSurfaceView.onPause();
        }
    }


}
