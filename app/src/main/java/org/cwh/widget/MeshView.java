package org.cwh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.cwh.R;

/**
 * Created by caowenhua on 2015/11/2.
 */
public class MeshView extends View {

    private Bitmap bitmap;
    private int WIDTH = 30;
    private int HEIGHT = 30;
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];

    public MeshView(Context context) {
        super(context);
        init();
    }

    public MeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.deer);
//        float bWidth = bitmap.getWidth();
//        float bHeight = bitmap.getHeight();
//        for (int i = 0; i < COUNT ; i+=2){
//            verts[i] = (i % (WIDTH + 1)) * (bWidth / WIDTH);
//            verts[i + 1] = (i / (HEIGHT + 1)) * (bHeight / HEIGHT);
//        }

        int index = 0;
        float bmWidth = bitmap.getWidth();
        float bmHeight = bitmap.getHeight();

        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                verts[index * 2 + 0] = fx;
                verts[index * 2 + 1] = fy;
                index += 1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
    }

//    private double getQuad(double p0, double p1, double p2){
//
//    }
}
