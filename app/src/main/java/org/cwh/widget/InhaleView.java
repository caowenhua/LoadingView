package org.cwh.widget;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import org.cwh.R;

/**
 * Created by caowenhua on 2015/11/2.
 */
public class InhaleView extends View implements View.OnClickListener{

    private Bitmap bitmap;
    private Bitmap scaledBitmap;
    private int WIDTH = 2;
    private int HEIGHT = 2;
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private Path leftPath;
    private Path rightPath;
    private PathMeasure leftPathMeasure;
    private PathMeasure rightPathMeasure;

    private float gone;

    private boolean isNormal;

    public InhaleView(Context context) {
        super(context);
        init();
    }

    public InhaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InhaleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
//        for (int i=0;i<verts.length;i++){
//            System.out.print("--" + verts[i]);
//        }
//        System.out.println("\n\n");

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
//        for (int i=0;i<verts.length;i++){
//            System.out.print("--" + verts[i]);
//        }
//        System.out.println("\n\n");
        isNormal = true;
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftPath = new Path();
        leftPath.lineTo(0, bitmap.getHeight());
        leftPath.quadTo(0, h, w / 2, h);
        rightPath = new Path();
        rightPath.moveTo(bitmap.getWidth(), 0);
        rightPath.lineTo(bitmap.getWidth(), bitmap.getHeight());
        rightPath.quadTo(w, h, w / 2, h);

        leftPathMeasure = new PathMeasure(leftPath, false);
        rightPathMeasure = new PathMeasure(rightPath, false);

        gone = (float)bitmap.getHeight() / (float) h;
    }

    @Override
    public void onClick(View v) {
        if(isNormal){
            startAnimation();
        }
        else{
            spitAnimation();
        }
    }

    private void startAnimation() {
        ValueAnimator anim = ValueAnimator.ofObject(new InhaleEvalutaor(), 0f, 1f);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isNormal = false;
                setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOnClickListener(InhaleView.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    private void spitAnimation(){
        ValueAnimator anim = ValueAnimator.ofObject(new SpitEvalutaor(), 0f, 1f);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isNormal = true;
                setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOnClickListener(InhaleView.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    private class InhaleEvalutaor implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            float[] pos = new float[2];
            leftPathMeasure.getPosTan(fraction * leftPathMeasure.getLength(), pos, null);
            verts[0] = pos[0];
            verts[1] = pos[1];
            rightPathMeasure.getPosTan(fraction * rightPathMeasure.getLength(), pos, null);
            verts[4] = pos[0];
            verts[5] = pos[1];
            verts[2] = (verts[0] + verts[4]) / 2;
            verts[3] = (verts[1] + verts[5]) / 2;

            leftPathMeasure.getPosTan(((fraction + gone / 2)>1? 1:(fraction + gone / 2)) * leftPathMeasure.getLength(), pos, null);
            verts[6] = pos[0];
            verts[7] = pos[1];
            rightPathMeasure.getPosTan(((fraction + gone / 2)>1? 1:(fraction + gone / 2)) * rightPathMeasure.getLength(), pos, null);
            verts[10] = pos[0];
            verts[11] = pos[1];
            verts[8] = (verts[6] + verts[10]) / 2;
            verts[9] = (verts[7] + verts[11]) / 2;

            leftPathMeasure.getPosTan(((fraction + gone)>1? 1:(fraction + gone)) * leftPathMeasure.getLength(), pos, null);
            verts[12] = pos[0];
            verts[13] = pos[1];
            rightPathMeasure.getPosTan(((fraction + gone)>1? 1:(fraction + gone)) * rightPathMeasure.getLength(), pos, null);
            verts[16] = pos[0];
            verts[17] = pos[1];
            verts[14] = (verts[12] + verts[16]) / 2;
            verts[15] = (verts[13] + verts[17]) / 2;

            invalidate();
            return null;
        }
    }

    private class SpitEvalutaor implements TypeEvaluator{
        @Override
        public Object evaluate(float action, Object startValue, Object endValue) {
            float fraction = Math.abs(action - 1);
            float[] pos = new float[2];
            leftPathMeasure.getPosTan(fraction * leftPathMeasure.getLength(), pos, null);
            verts[0] = pos[0];
            verts[1] = pos[1];
            rightPathMeasure.getPosTan(fraction * rightPathMeasure.getLength(), pos, null);
            verts[4] = pos[0];
            verts[5] = pos[1];
            verts[2] = (verts[0] + verts[4]) / 2;
            verts[3] = (verts[1] + verts[5]) / 2;

            leftPathMeasure.getPosTan(((fraction + gone / 2)>1? 1:(fraction + gone / 2)) * leftPathMeasure.getLength(), pos, null);
            verts[6] = pos[0];
            verts[7] = pos[1];
            rightPathMeasure.getPosTan(((fraction + gone / 2)>1? 1:(fraction + gone / 2)) * rightPathMeasure.getLength(), pos, null);
            verts[10] = pos[0];
            verts[11] = pos[1];
            verts[8] = (verts[6] + verts[10]) / 2;
            verts[9] = (verts[7] + verts[11]) / 2;

            leftPathMeasure.getPosTan(((fraction + gone)>1? 1:(fraction + gone)) * leftPathMeasure.getLength(), pos, null);
            verts[12] = pos[0];
            verts[13] = pos[1];
            rightPathMeasure.getPosTan(((fraction + gone)>1? 1:(fraction + gone)) * rightPathMeasure.getLength(), pos, null);
            verts[16] = pos[0];
            verts[17] = pos[1];
            verts[14] = (verts[12] + verts[16]) / 2;
            verts[15] = (verts[13] + verts[17]) / 2;

            invalidate();
            return null;
        }
    }
}
