package com.arnaudbos.android2d;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private enum MatrixConcatenation {
        PRE, POST
    }

    private static final float THETA = 30;
    private float width;
    private float height;
    private ImageView view;
    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display Grumpy cat
        Drawable d = getDrawable(R.drawable.grumpy);
        view = new ImageView(this);
        view.setImageDrawable(d);
        setContentView(view);

        // Center Grumpy cat
        view.setScaleType(ImageView.ScaleType.MATRIX);
        final float[] dimensions = getSize(this);
        width = dimensions[0];
        height = dimensions[1];
        matrix = center(width, height, d);
        view.setImageMatrix(matrix);
    }

    private static float[] getSize(Activity activity) {
        final Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new float[] {size.x, size.y};
    }

    private static void rotateGrumpyCat(ImageView view, float x, float y,
                                        Matrix matrix, MatrixConcatenation p) {
        switch (p) {
            case PRE:
                matrix.preTranslate(-x, -y);
                matrix.preRotate(THETA);
                matrix.preTranslate(x, y);
                break;
            case POST:
                matrix.postTranslate(-x, -y);
                matrix.postRotate(THETA);
                matrix.postTranslate(x, y);
                break;
        }
        view.setImageMatrix(matrix);
    }

    /**
     * Returns a scaling matrix whose purpose is to center the given
     * drawable inside the view of given width and height.
     * @param width the width of the container
     * @param height the height of the container
     * @param d the drawable
     * @return the scaling matrix
     */
    private static Matrix center(float width, float height, Drawable d) {
        final float drawableWidth = d.getIntrinsicWidth();
        final float drawableHeight = d.getIntrinsicHeight();
        final float widthScale = width / drawableWidth;
        final float heightScale = height / drawableHeight;
        final float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
        Matrix m = new Matrix();
        m.postScale(scale, scale);
        m.postTranslate((width - drawableWidth * scale) / 2F,
                (height - drawableHeight * scale) / 2F);
        return m;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.switchItem);//.getActionView();
        Switch switchButton = (Switch) item.getActionView().findViewById(R.id.switchForActionBar);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setText("Post-concat");
                    rotateGrumpyCat(view, width/2, height/2, new Matrix(matrix),
                            MatrixConcatenation.POST);
                } else {
                    buttonView.setText("Pre-concat");
                    rotateGrumpyCat(view, width/2, height/2, new Matrix(matrix),
                            MatrixConcatenation.PRE);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
