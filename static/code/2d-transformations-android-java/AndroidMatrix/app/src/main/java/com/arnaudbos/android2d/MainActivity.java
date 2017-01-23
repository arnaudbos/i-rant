package com.arnaudbos.android2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MatrixRotateExample(this));
    }
    
    public class MatrixRotateExample extends View {
        public MatrixRotateExample(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();

            int left = x / 2 - 200;
            int top = y / 2 - 200;
            int right = left + 400;
            int bottom = top + 400;

            RectF rect = new RectF();
            rect.set(left, top, right, bottom);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawRect(rect, paint);

            Matrix matrix = new Matrix();
            matrix.postRotate(45, x / 2, y / 2);
            canvas.setMatrix(matrix);
            paint.setColor(Color.GREEN);
            canvas.drawRect(rect, paint);

            matrix.preRotate(30, x / 2, y / 2);
            canvas.setMatrix(matrix);
            paint.setColor(Color.BLUE);
            canvas.drawRect(rect, paint);
        }
    }
}
