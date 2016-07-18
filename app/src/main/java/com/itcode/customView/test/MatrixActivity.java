package com.itcode.customView.test;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.itcode.customView.R;
import com.itcode.customView.matrix.MatrixImageView;

/**
 * 矩阵变换:
 * 平移、旋转、缩放、错切、对称
 */
public class MatrixActivity extends Activity {

    private MatrixImageView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_test);
        view = (MatrixImageView) findViewById(R.id.ivTestMatrix);
    }

    public void translateView(View v) {
        Matrix matrix = new Matrix();
        // 输出图像的宽度和高度(162 x 251)
        Log.e("ceshi", "image size: width x height = " + view.getImageBitmap().getWidth() + " x " + view.getImageBitmap().getHeight());
        // 1. 平移
        matrix.postTranslate(view.getImageBitmap().getWidth() / 2, view.getImageBitmap().getHeight() / 2);
        view.setImageMatrix(matrix);
    }

    public void rotateView(View v) {
        Matrix matrix = new Matrix();
        // 2. 旋转(围绕图像的中心点)
        matrix.setRotate(45f, view.getImageBitmap().getWidth() / 2f, view.getImageBitmap().getHeight() / 2f);
//        // 3. 旋转(围绕坐标原点) + 平移(效果同2)
//        matrix.setRotate(45f);
//        matrix.preTranslate(-1f * view.getImageBitmap().getWidth() / 2f, -1f * view.getImageBitmap().getHeight() / 2f);
//        matrix.postTranslate((float)view.getImageBitmap().getWidth() / 2f, (float)view.getImageBitmap().getHeight() / 2f);
        view.setImageMatrix(matrix);
    }

    public void scaleView(View v) {
        Matrix matrix = new Matrix();
//          matrix.setScale(0.5f, 0.5f);//相对于坐标原点缩小到原来的0.5
        matrix.setScale(0.5f, 0.5f, view.getImageBitmap().getWidth() / 2f, view.getImageBitmap().getHeight() / 2f);
        view.setImageMatrix(matrix);
    }

    public void skewView(View v) {
        Matrix matrix = new Matrix();
//        // 5. 错切 - 水平
//        matrix.setSkew(0.5f, 0f);
//        // 6. 错切 - 垂直
//        matrix.setSkew(0f, 0.5f);
        //7. 错切 - 水平 + 垂直
        matrix.setSkew(0.5f, 0.5f);
        view.setImageMatrix(matrix);
    }

    public void symmetricView(View v) {
        Matrix matrix = new Matrix();

//        // 8. 对称 (水平对称)
//        float matrix_values[] = {1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
//        matrix.setValues(matrix_values);
//        // 做下面的平移变换，为了让变换后的图像和原图像不重叠
//        matrix.postTranslate(0f,view.getImageBitmap().getHeight() * 1f);

//        // 9. 对称 - 垂直
//        float matrix_values[] = {-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
//        matrix.setValues(matrix_values);
//        // 做下面的平移变换，为了让变换后的图像和原图像不重叠
//        matrix.postTranslate(view.getImageBitmap().getHeight() * 1f,0f);


        // 10. 对称(对称轴为直线y = x)
        float matrix_values[] = {0f, -1f, 0f, -1f, 0f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrix_values);
        // 做下面的平移变换，为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getHeight() + view.getImageBitmap().getWidth(), view.getImageBitmap().getHeight() + view
                .getImageBitmap().getWidth());

        view.setImageMatrix(matrix);
    }

    /**
     * 问题:大小失真,图片被切割
     *
     * @param v
     */
//    public void translateSuccess(View v) {
//        Log.e("ceshi", "image size: width x height = " + ivTest.getWidth() + " x " + ivTest.getHeight());
//        Bitmap bitmapOld = BitmapFactory.decodeResource(getResources(), R.mipmap.test_pic);
//        Bitmap bitmap = Bitmap.createBitmap((int) ivTest.getWidth(), ivTest.getHeight(), bitmapOld.getConfig());
////        Bitmap  bitmap = Bitmap.createBitmap(DensityUtil.dip2px(this,bitmapOld.getWidth()), DensityUtil.dip2px(this,bitmapOld.getHeight()),
//// bitmapOld.getConfig());
//        Canvas canvas = new Canvas(bitmap);
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(ivTest.getWidth() / 2, 0);
//        canvas.drawBitmap(bitmapOld, matrix, new Paint());
//        ivTest.setScaleType(ImageView.ScaleType.FIT_XY);
//        ivTest2.setImageBitmap(bitmap);
//    }

    /**
     * 打印Matrix中的元素值
     *
     * @param matrix
     */
    private void printMatrixValue(Matrix matrix) {
        // 下面的代码是为了查看matrix中的元素
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