package com.cctv.langduzhe.util.imageTransform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.cctv.langduzhe.util.DensityUtil;
import com.squareup.picasso.Transformation;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：自定义圆角图片处理工具类(可以修改图片圆角的半径)
 */
/**
 * 圆角显示图片-Picasso
 */
public class RoundTransform implements Transformation {
    private int radius;//圆角值

    public RoundTransform(int radius) {
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        //画板
        Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap);//创建同尺寸的画布
        paint.setAntiAlias(true);//画笔抗锯齿
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        //画圆角背景
        RectF rectF = new RectF(new Rect(0, 0, width, height));//赋值
        canvas.drawRoundRect(rectF, radius, radius, paint);//画圆角矩形
        //
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();//释放

        return bitmap;
    }

    @Override
    public String key() {
        return "round : radius = " + radius;
    }
}