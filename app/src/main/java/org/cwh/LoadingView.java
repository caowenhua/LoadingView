package org.cwh;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by caowenhua on 2015/10/27.
 */
public class LoadingView extends View{

    /**
     * The density of the phone screen
     * Depend on the device of yours
     * It may be different for different devices
     */
    private float density;
    /**
     * A default size of the view
     */
    private int defaultSize;

    /**
     * The width of this view
     */
    private int width;

    /**
     * The height of this view
     */
    private int height;

    /**
     * The real width of the circle , including the circle ring
     * devWidth = width - padding, this padding is setting at onSizeChanged, setting padding doesn't have sense
     */
    private int devWidth;

    private Paint paint;

    /**
     * The object to the circle
     */
    private Circle scaleCircle;
    /**
     * The object to the Arc
     */
    private Arc turningArc;
    /**
     * The rect of the arc, used at onDraw()
     */
    private RectF arcRectf;

    private int currentColor;


    public LoadingView(Context context){
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * prepare something we need to use
     */
    private void init(){
        density = Resources.getSystem().getDisplayMetrics().density;
        defaultSize = Math.round(50 * density);
        currentColor = Color.parseColor("#068bcf");
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = measureSize(widthMeasureSpec);
        int height = measureSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        devWidth = ((w > h)? w:h - Math.round(5 * density)) / 2;
        arcRectf = new RectF(width / 2 - devWidth/3, height / 2 - devWidth/3, width / 2 + devWidth/3, height / 2 + devWidth/3);
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureSize(int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(scaleCircle == null || turningArc == null){
            scaleCircle = new Circle();
            scaleCircle.setRadius(devWidth * 9 / 10);
            scaleCircle.setWidth(devWidth / 5);
            turningArc = new Arc();
            turningArc.setStartDegree(0.0f);
            turningArc.setSweepDegree(0.0f);
            turningArc.setWidth(devWidth / 5);
            turningArc.setRadius(devWidth * 9 / 10);
            drawView(canvas);
            startAnimation();
        }
        else{
            drawView(canvas);
        }
    }

    /**
     * Draw the circle and arc
     * @param canvas
     */
    private void drawView(Canvas canvas){
//        paint.setColor(currentColor);
        paint.setStrokeWidth(scaleCircle.getWidth());
        canvas.drawCircle(width / 2, height / 2, scaleCircle.getRadius(), paint);

        paint.setStrokeWidth(turningArc.getWidth());
        canvas.drawArc(arcRectf, turningArc.getStartDegree(), turningArc.getSweepDegree(), false, paint);
    }

    /**
     * Animation Evalutor of the circle, if you want to change something, change it
     */
    private class CircleEvalutor implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Circle startCircle = (Circle) startValue;
            Circle aimCircle = new Circle();
            aimCircle.setWidth(Math.abs(0.5f - fraction) * startCircle.getWidth() * 2);
            aimCircle.setRadius(startCircle.getRadius());
            return aimCircle;
        }
    }

    private class ArcEvalutor implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Arc startArc = (Arc)startValue;
            Arc aimArc = new Arc();
            aimArc.setRadius(startArc.getRadius());
            aimArc.setWidth(startArc.getWidth());
            aimArc.setStartDegree(fraction * 360f - 90f);
            aimArc.setSweepDegree((float) (360f * Math.sin(Math.PI * fraction)));
//            aimArc.setDegree((float) (360f * Math.sin(Math.PI * fraction)));
//            aimArc.setDegree(Math.abs(0.0f - fraction) * 360f);
            return aimArc;
        }
    }

    private class ColorEvalutor2 implements TypeEvaluator{
        private int mCurrentRed = -1;
        private int mCurrentGreen = -1;
        private int mCurrentBlue = -1;
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            String startColor = (String) startValue;
            String endColor = (String) endValue;
            int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
            int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
            int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
            int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
            int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
            int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);
            if (mCurrentRed == -1) {
                mCurrentRed = startRed;
            }
            if (mCurrentGreen == -1) {
                mCurrentGreen = startGreen;
            }
            if (mCurrentBlue == -1) {
                mCurrentBlue = startBlue;
            }
            // the diff of color
            int redDiff = Math.abs(startRed - endRed);
            int greenDiff = Math.abs(startGreen - endGreen);
            int blueDiff = Math.abs(startBlue - endBlue);
            int colorDiff = redDiff + greenDiff + blueDiff;
            if (mCurrentRed != endRed) {
                mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0,
                        fraction);
            } else if (mCurrentGreen != endGreen) {
                mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff,
                        redDiff, fraction);
            } else if (mCurrentBlue != endBlue) {
                mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff,
                        redDiff + greenDiff, fraction);
            }

            // compute the color and return String
            String currentColor = "#" + getHexString(mCurrentRed)
                    + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
            return currentColor;
        }

        private int getCurrentColor(int startColor, int endColor, int colorDiff,
                                    int offset, float fraction) {
            int currentColor;
            if (startColor > endColor) {
                currentColor = (int) (startColor - (fraction * colorDiff - offset));
                if (currentColor < endColor) {
                    currentColor = endColor;
                }
            } else {
                currentColor = (int) (startColor + (fraction * colorDiff - offset));
                if (currentColor > endColor) {
                    currentColor = endColor;
                }
            }
            return currentColor;
        }

        /**
         * 10 -> 16
         */
        private String getHexString(int value) {
            String hexString = Integer.toHexString(value);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            return hexString;
        }
    }

    private void startAnimation() {
        Circle startCircle = new Circle();
        startCircle.setRadius(devWidth * 9 / 10);
        startCircle.setWidth(devWidth / 5);
        ValueAnimator circleAnim = ValueAnimator.ofObject(new CircleEvalutor(), startCircle, startCircle);
        circleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleCircle = (Circle) animation.getAnimatedValue();
            }
        });
        circleAnim.setDuration(1000);
        circleAnim.setRepeatCount(-1);
        circleAnim.start();

        Arc startArc = new Arc();
        startArc.setStartDegree(0.0f);
        startArc.setSweepDegree(0.0f);
        startArc.setWidth(devWidth / 5);
        startArc.setRadius(devWidth * 5 / 10);
        ValueAnimator arcAnim = ValueAnimator.ofObject(new ArcEvalutor(), startArc, startArc);
        arcAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                turningArc = (Arc) animation.getAnimatedValue();
                invalidate();
            }
        });
        arcAnim.setDuration(1000);
        arcAnim.setRepeatCount(-1);
        arcAnim.start();

        ValueAnimator colorAnim = ValueAnimator.ofObject(new ColorEvalutor(), "#068bcf", "#02c069");
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentColor = Color.parseColor((String) animation.getAnimatedValue());
                invalidate();
            }
        });
        colorAnim.setDuration(1000);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();
    }

    //red uncorrent
    private class ColorEvalutor implements TypeEvaluator{
        private int mCurrentRed = -1;
        private int mCurrentGreen = -1;
        private int mCurrentBlue = -1;
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            String startColor = (String) startValue;
            String endColor = (String) endValue;
            int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
            int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
            int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
            int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
            int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
            int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);
            if (mCurrentRed == -1) {
                mCurrentRed = startRed;
            }
            if (mCurrentGreen == -1) {
                mCurrentGreen = startGreen;
            }
            if (mCurrentBlue == -1) {
                mCurrentBlue = startBlue;
            }
            // the diff of color
            int redDiff = Math.abs(startRed - endRed);
            int greenDiff = Math.abs(startGreen - endGreen);
            int blueDiff = Math.abs(startBlue - endBlue);
            int colorDiff = redDiff + greenDiff + blueDiff;
            if(fraction < 0.5f){
                if (mCurrentRed != endRed) {
                    mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0,
                            fraction*2f);
                } else if (mCurrentGreen != endGreen) {
                    mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff,
                            redDiff, fraction*2f);
                } else if (mCurrentBlue != endBlue) {
                    mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff,
                            redDiff + greenDiff, fraction*2f);
                }
            }
            else if(fraction < 1.0f && fraction > 0.5f){
                if (mCurrentRed != startRed) {
                    mCurrentRed = getCurrentColor(endRed, startRed, colorDiff, 0,
                            fraction*2f-1f);
                } else if (mCurrentGreen != startGreen) {
                    mCurrentGreen = getCurrentColor(endGreen, startGreen, colorDiff,
                            redDiff, fraction*2f-1f);
                } else if (mCurrentBlue != startBlue) {
                    mCurrentBlue = getCurrentColor(endBlue, startBlue, colorDiff,
                            redDiff + greenDiff, fraction*2f-1f);
                }
            }


            // compute the color and return String
            String currentColor = "#" + getHexString(mCurrentRed)
                    + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
//            Log.e("aaa", currentColor);
            return currentColor;
        }

        private int getCurrentColor(int startColor, int endColor, int colorDiff,
                                    int offset, float fraction) {
            int currentColor;
            if (startColor > endColor) {
                currentColor = (int) (startColor - (fraction * colorDiff - offset));
                if (currentColor < endColor) {
                    currentColor = endColor;
                }
                else if(currentColor > startColor){
                    currentColor = startColor;
                }
            }
            else {
                currentColor = (int) (startColor + (fraction * colorDiff - offset));
                if (currentColor > endColor) {
                    currentColor = endColor;
                }
                else if(currentColor < startColor){
                    currentColor = startColor;
                }
            }
            return currentColor;
        }

        /**
         * 10 -> 16
         */
        private String getHexString(int value) {
            String hexString = Integer.toHexString(value);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            return hexString;
        }
    }

    private class Circle{
        float radius;
        float width;
        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }
    }

    private class Arc{
        float radius;
        float width;
        float startDegree;
        float sweepDegree;
        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getStartDegree() {
            return startDegree;
        }

        public float getSweepDegree() {
            return sweepDegree;
        }

        public void setStartDegree(float startDegree) {
            this.startDegree = startDegree;
        }

        public void setSweepDegree(float sweepDegree) {
            this.sweepDegree = sweepDegree;
        }
    }
}
