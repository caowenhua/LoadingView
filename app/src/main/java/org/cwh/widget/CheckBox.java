package org.cwh.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
 * Created by caowenhua on 2015/11/1.
 */
public class CheckBox extends View implements View.OnClickListener{

    private Paint paint;

    /**
     * The color of tick, when the checkBox is Selected
     */
    private int tickColor;
    /**
     * The color of the border, the bottom
     */
    private int borderColor;
    /**
     * A default size of the view
     */
    private int defaultSize;
    private float density;
    /**
     * The distance between the center and the outside of circle
     */
    private int radius;
    /**
     * The width of the View
     */
    private int width;
    /**
     * The height of the View
     */
    private int height;
    /**
     * The parameter to judge has been Checked
     */
    private boolean isChecked;
    /**
     * The rect of the arc
     */
    private RectF arcRectf;
    /**
     * The color of the Inner circle
     */
    private int circleColor;

    private Line shortLine;
    private Line longLine;
    private Arc colorArc;
    private Arc startArc;
    private Arc endArc;
    private Point startShortPoint;
    private Point endShortPoint;
    private Point startLongPoint;
    private Point endLongPoint;

    private OnCheckListener onCheckListener;

    public CheckBox(Context context) {
        super(context);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        density = Resources.getSystem().getDisplayMetrics().density;
        defaultSize = Math.round(40 * density);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        borderColor = Color.parseColor("#90a4ae");
        tickColor = Color.parseColor("#259b24");
        circleColor = Color.parseColor("#d5d5d5");

        colorArc = new Arc();
        colorArc.setStartDegree(180f);
        colorArc.setSweepDegree(0f);
        shortLine = new Line();
        shortLine.setStartX(-1);
        longLine = new Line();
        longLine.setStartX(-1);

        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = measureSize(widthMeasureSpec);
        int height = measureSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        radius = ((w > h)? w:h - Math.round(5 * density)) / 2;
        arcRectf = new RectF(w/2 - radius*7/10 , h/2 - radius*7/10 , w/2 + radius*7/10, h/2 + radius*7/10);
        colorArc.setRadius(radius * 7 / 10);
        colorArc.setWidth(radius / 5);

        startArc = new Arc();
        startArc.setRadius(radius * 7 / 10);
        startArc.setWidth(radius / 5);
        startArc.setStartDegree(180f);
        startArc.setSweepDegree(0f);
        endArc = new Arc();
        endArc.setRadius(radius * 7 / 10);
        endArc.setWidth(radius / 5);
        endArc.setStartDegree(180f);
        endArc.setSweepDegree(360f);

//        startShortPoint = new Point(width / 2 - radius * 7 / 10, height / 2);
//        endShortPoint = new Point(width / 2 - radius / 5, height / 2 + radius * 2 / 5);
//        startLongPoint = new Point(width / 2 - radius / 5 - radius / 12, height / 2 + radius * 2 / 5 + radius / 12);
//        endLongPoint = new Point(width / 2 + radius / 2, height / 2 - radius * 2 / 5);

        startShortPoint = new Point(width / 2 - radius / 2, height / 2);
        endShortPoint = new Point(width / 2 - radius / 10, height / 2 + radius * 2 / 5);
        startLongPoint = new Point(width / 2 - radius / 4 , height / 2 + radius * 2 / 5 );
        endLongPoint = new Point(width / 2 + radius / 2, height / 2 - radius * 1 / 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, radius * 3 / 5, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(tickColor);
        paint.setStrokeWidth(colorArc.getWidth());
        canvas.drawArc(arcRectf, colorArc.getStartDegree(), colorArc.getSweepDegree(), false, paint);
        paint.setColor(borderColor);
        canvas.drawArc(arcRectf, colorArc.getStartDegree() + colorArc.getSweepDegree(), 360f - colorArc.getSweepDegree(), false, paint);

//        shortLine.setStartX(startShortPoint.getX());
//        shortLine.setStartY(startShortPoint.getY());
//        shortLine.setEndX(endShortPoint.getX());
//        shortLine.setEndY(endShortPoint.getY());
//
//        longLine.setStartX(startLongPoint.getX());
//        longLine.setStartY(startLongPoint.getY());
//        longLine.setEndX(endLongPoint.getX());
//        longLine.setEndY(endLongPoint.getY());

        paint.setColor(tickColor);
        paint.setStrokeWidth(colorArc.getWidth());
        if(shortLine.getStartX() != -1){
            canvas.drawLine(shortLine.getStartX(), shortLine.getStartY(), shortLine.getEndX(), shortLine.getEndY(), paint);
        }
        if(longLine.getStartX() != -1){
            canvas.drawLine(longLine.getStartX(), longLine.getStartY(), longLine.getEndX(), longLine.getEndY(), paint);
        }

    }

    @Override
    public void onClick(View v) {
        if(!isChecked) {
            startTickAnimation();
        } else {
            discardTickAnimation();
        }
        setOnClickListener(null);
    }

    private void discardTickAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator colorAnim = ValueAnimator.ofObject(new ColorEvalutor(), "#f9f9f9", "#d5d5d5");
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleColor = Color.parseColor((String) animation.getAnimatedValue());
//                invalidate();
            }
        });
        colorAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isChecked = !isChecked;
                if(onCheckListener != null){
                    onCheckListener.onCheck(CheckBox.this, isChecked);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOnClickListener(CheckBox.this);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        invalidate();
//                    }
//                }, 40);

            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        colorAnim.setDuration(300);

        ValueAnimator arcAnim = ValueAnimator.ofObject(new ArcEvalutor(), endArc, startArc);
        arcAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                colorArc = (Arc) animation.getAnimatedValue();
                invalidate();
            }
        });
        arcAnim.setDuration(150);

        ValueAnimator shortLineAnim = ValueAnimator.ofObject(new DiscardLineEvalutor(), endShortPoint, startShortPoint);
        shortLineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shortLine = (Line) animation.getAnimatedValue();
                invalidate();
            }
        });
        shortLineAnim.setDuration(75);

        ValueAnimator longLineAnim = ValueAnimator.ofObject(new DiscardLineEvalutor(), endLongPoint, startLongPoint);
        longLineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                longLine = (Line) animation.getAnimatedValue();
                invalidate();
            }
        });
        longLineAnim.setDuration(75);

        animatorSet.play(colorAnim).with(longLineAnim);
        animatorSet.play(longLineAnim).before(shortLineAnim);
        animatorSet.play(shortLineAnim).before(arcAnim);
        animatorSet.start();
    }

    public void setTickColor(int tickColor) {
        this.tickColor = tickColor;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    private void startTickAnimation(){
        shortLine.setStartX(-1);
        longLine.setStartX(-1);
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator colorAnim = ValueAnimator.ofObject(new ColorEvalutor(), "#d5d5d5", "#f9f9f9");
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleColor = Color.parseColor((String) animation.getAnimatedValue());
//                invalidate();
            }
        });
        colorAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isChecked = !isChecked;
                if(onCheckListener != null){
                    onCheckListener.onCheck(CheckBox.this, isChecked);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOnClickListener(CheckBox.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        colorAnim.setDuration(300);

        ValueAnimator arcAnim = ValueAnimator.ofObject(new ArcEvalutor(), startArc, endArc);
        arcAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                colorArc = (Arc) animation.getAnimatedValue();
                invalidate();
            }
        });
        arcAnim.setDuration(150);

        ValueAnimator shortLineAnim = ValueAnimator.ofObject(new LineEvalutor(), startShortPoint, endShortPoint);
        shortLineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shortLine = (Line) animation.getAnimatedValue();
                invalidate();
            }
        });
        shortLineAnim.setDuration(75);

        ValueAnimator longLineAnim = ValueAnimator.ofObject(new LineEvalutor(), startLongPoint, endLongPoint);
        longLineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                longLine = (Line) animation.getAnimatedValue();
                invalidate();
            }
        });
        longLineAnim.setDuration(75);

        animatorSet.play(colorAnim).with(arcAnim);
        animatorSet.play(arcAnim).before(shortLineAnim);
        animatorSet.play(shortLineAnim).before(longLineAnim);
        animatorSet.start();
    }

    private class ColorEvalutor implements TypeEvaluator {
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

    private class LineEvalutor implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            Line line = new Line();
            line.setStartX(startPoint.getX());
            line.setStartY(startPoint.getY());
            line.setEndX((endPoint.getX() - startPoint.getX()) * fraction + startPoint.getX());
            line.setEndY((endPoint.getY()-startPoint.getY())*fraction + startPoint.getY());
            return line;
        }
    }

    private class DiscardLineEvalutor implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            Line line = new Line();
            line.setStartX((endPoint.getX() - startPoint.getX()) * fraction + startPoint.getX());
            line.setStartY((endPoint.getY() - startPoint.getY()) * fraction + startPoint.getY());
            line.setEndX(endPoint.getX());
            line.setEndY(endPoint.getY());
            return line;
        }
    }

    private class ArcEvalutor implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Arc startArc = (Arc) startValue;
            Arc endArc = (Arc) endValue;
            Arc aimArc = new Arc();
            aimArc.setWidth(startArc.getWidth());
            aimArc.setRadius(startArc.getRadius());
            aimArc.setStartDegree(startArc.getStartDegree());
            aimArc.setSweepDegree(startArc.getSweepDegree() + fraction*(endArc.getSweepDegree()-startArc.getSweepDegree()));
            return aimArc;
        }
    }

    private class Point{
        Point(float x, float y){
            this.x = x;
            this.y = y;
        }
        float x;
        float y;

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    private class Line{
        float startX;
        float startY;
        float endX;
        float endY;

        public float getEndX() {
            return endX;
        }

        public float getEndY() {
            return endY;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        public void setEndX(float endX) {
            this.endX = endX;
        }

        public void setEndY(float endY) {
            this.endY = endY;
        }

        public void setStartX(float startX) {
            this.startX = startX;
        }

        public void setStartY(float startY) {
            this.startY = startY;
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

    public interface OnCheckListener{
        void onCheck(View v, boolean isCheck);
    }
}