package com.likechat.likechat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 圆角矩形 Imageview
 */

public class RoundCornerImageView extends android.support.v7.widget.AppCompatImageView
{
    private Paint m_paint;
    private Rect m_rectSrc, m_rectDest;
    private RectF m_rectRoundCorner;

    public RoundCornerImageView(Context context)
    {
        super(context);
        init();
    }

    public RoundCornerImageView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RoundCornerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        m_paint = new Paint();

        m_rectSrc = new Rect();
        m_rectDest = new Rect();

        m_rectRoundCorner = new RectF();
    }

    /**
     * 绘制圆角矩形图片
     *
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getDrawable();
        if (null != drawable)
        {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getRoundBitmap(bitmap, 20);
            m_rectSrc.set(0, 0, b.getWidth(), b.getHeight());
            m_rectDest.set(0, 0, getWidth(), getHeight());
            m_paint.reset();
            canvas.drawBitmap(b, m_rectSrc, m_rectDest, m_paint);

        }
        else
        {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取圆角矩形图片方法
     *
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
        m_rectRoundCorner.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m_paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        m_paint.setColor(color);
        //int x = bitmap.getWidth();

        canvas.drawRoundRect(m_rectRoundCorner, roundPx, roundPx, m_paint);
        m_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, m_rectSrc, m_rectSrc, m_paint);
        return output;

    }
}
