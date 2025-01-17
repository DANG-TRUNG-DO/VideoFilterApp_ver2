package com.example.videffects;

import android.opengl.GLSurfaceView;

import com.example.videffects.interfaces.ShaderInterface;


/**
 * Converts video to GreyScale.
 *
 * @author sheraz.khilji
 */
public class GreyScaleEffect implements ShaderInterface {

    @Override
    public String getShader(GLSurfaceView mGlSurfaceView) {
        return "#extension GL_OES_EGL_image_external : require\n"
                + "precision mediump float;\n"
                + "uniform samplerExternalOES sTexture;\n"
                + "varying vec2 vTextureCoord;\n" + "void main() {\n"
                + "  vec4 color = texture2D(sTexture, vTextureCoord);\n"
                + "  float y = dot(color, vec4(0.299, 0.587, 0.114, 0));\n"
                + "  gl_FragColor = vec4(y, y, y, color.a);\n" + "}\n";
    }
}
