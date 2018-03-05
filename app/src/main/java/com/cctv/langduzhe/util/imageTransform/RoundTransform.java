package com.cctv.langduzhe.util.imageTransform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cctv.langduzhe.util.DensityUtil;
import com.squareup.picasso.Transformation;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：自定义圆角图片处理工具类(可以修改图片圆角的半径)
 */
public class RoundTransform implements Transformation{
    private Context mContext;
    private float cornerRadius;

    public RoundTransform(Context context,float radius) {
        mContext = context;
        this.cornerRadius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {

        int widthLight = source.getWidth();
        int heightLight = source.getHeight();
        int radius = DensityUtil.dp2px(mContext, cornerRadius); // 圆角半径

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, radius, radius, paintColor);
//        canvas.drawRoundRect(rectF, widthLight / 5, heightLight / 5, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0, 0, paintImage);
        source.recycle();
        return output;
    }

    @Override
    public String key() {
        return "roundcorner";
    }
}
