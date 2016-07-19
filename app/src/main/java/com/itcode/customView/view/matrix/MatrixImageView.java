package com.itcode.customView.view.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.itcode.customView.R;

/**
 *TODO:1.onMeasure  2.在大小刚刚好的地方做矩阵变化
 */
public class MatrixImageView extends ImageView {
    private Bitmap bitmap;
    private Matrix matrix;

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_pic);
        matrix = new Matrix();
    }

    public MatrixImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画出原图像
        canvas.drawBitmap(bitmap, 0, 0, null);
        // 画出变换后的图像
        canvas.drawBitmap(bitmap, matrix, null);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        this.matrix.set(matrix);
        super.setImageMatrix(matrix);
    }

    public Bitmap getImageBitmap() {
        return bitmap;
    }

    /**
     * 查看matrix中的元素
     *
     * @param matrix
     */
    private void printMatrixValue(Matrix matrix) {
        float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);
        for (int i = 0; i < 3; ++i) {
            String temp = new String();
            for (int j = 0; j < 3; ++j) {
                temp += matrixValues[3 * i + j] + "\t\t";
            }
            Log.e("ceshi", temp);
        }
    }


}

