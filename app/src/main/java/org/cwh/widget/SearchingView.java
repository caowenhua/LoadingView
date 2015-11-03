package org.cwh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import org.cwh.R;

public class SearchingView extends View {

	private final int REDRAW = 50;

	private int width;
	private int height;
	private int edgeSize;
	private int arc;
	private Paint paint;
	private Bitmap centerBitmap;
	private RectF rectf;
	private RectF bitmapRectf;
	private int circleColor;
	private int arcColor;
	
	public SearchingView(Context context) {
		super(context);
		setupPaint();
	}

	public SearchingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setupPaint();
	}

	public SearchingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupPaint();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		if(w < h){
			edgeSize = h - h*5/480;
			rectf = new RectF((w-h)/2+ h*5/960, h*5/960, (w+h)/2- h*5/960, h- h*5/960);
		}
		else{
			edgeSize = w - w*5/480;
			rectf = new RectF(w*5/960, (h-w)/2+ w*5/960, w- w*5/960, (h+w)/2- w*5/960);
		}
		bitmapRectf = new RectF(width / 2 - edgeSize / 10, height / 2 - edgeSize / 10, width / 2 + edgeSize / 10, height / 2 + edgeSize/10);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCircleBackground(canvas);
		
		canvas.drawPath(getPath(arc), paint);
		paint.setColor(arcColor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPath(getPath(arc), paint);

		paint.setAlpha(255);
		canvas.drawBitmap(centerBitmap, null, bitmapRectf, paint);
		handler.sendEmptyMessageDelayed(REDRAW, 20);
	}

	private void drawCircleBackground(Canvas canvas) {
		paint.setColor(circleColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(width /240);
		canvas.drawCircle(width / 2, height / 2, edgeSize / 2, paint);
		canvas.drawCircle(width / 2, height / 2, edgeSize / 2 - width / 60, paint);
		canvas.drawCircle(width / 2, height / 2, edgeSize / 3, paint);
		canvas.drawCircle(width / 2, height / 2, edgeSize / 3 - width / 80, paint);
		canvas.drawCircle(width / 2, height / 2, edgeSize / 6, paint);
	}

	private Path getPath(int arc){
		Path path = new Path();
		path.moveTo(width/2, height/2);
		path.quadTo(edgeSize/2*(float)Math.sin(25*Math.PI/18 - arc*Math.PI/180)+width/2,
				edgeSize/2*(float)Math.cos(25*Math.PI/18 - arc*Math.PI/180)+height/2,
				edgeSize/2*(float)Math.sin(23*Math.PI/18 - arc*Math.PI/180)+width/2,
				edgeSize/2*(float)Math.cos(23*Math.PI/18 - arc*Math.PI/180)+height/2);
		path.arcTo(rectf, -140 + arc, 100);
		path.quadTo(edgeSize/2*(float)Math.sin(11*Math.PI/18 - arc*Math.PI/180)+width/2,
				edgeSize/2*(float)Math.cos(11*Math.PI/18 - arc*Math.PI/180)+height/2,
				width/2, height/2);
		return path;
	}

	private void setupPaint() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		
		Drawable d = getResources().getDrawable(R.mipmap.matching_testhead);
		centerBitmap = ((BitmapDrawable)d).getBitmap();
		arc = 0;
		arcColor = Color.parseColor("#80349ee8");
		circleColor = Color.parseColor("#80ffffff");
	}
	
	public void setHeadDrawableByID(int id){
		Drawable d = getResources().getDrawable(id);
		centerBitmap = ((BitmapDrawable)d).getBitmap();
	}

	private android.os.Handler handler = new android.os.Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == REDRAW){
				if(arc > 360){
					arc -= 360;
				}
				arc += 3;
				invalidate();
			}
		}
	};
}
