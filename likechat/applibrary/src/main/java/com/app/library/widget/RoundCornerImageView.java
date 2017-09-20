package com.app.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    /** 画笔 */
    private Paint m_paint;
    /**  */
    private Rect m_rectSrc, m_rectDest;
    /** 绘制出来的图片矩形 */
    private Rect m_rectRound;
    /** 绘制圆角矩形 */
    private RectF m_rectFRoundCorner;
    /** 临时的画布 */
    private Canvas m_canvasTemp;

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
        m_rectRound = new Rect();

        m_rectFRoundCorner = new RectF();

        m_canvasTemp = new Canvas();
    }

    /**
     * 绘制圆角矩形图片
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getDrawable();
        if (null != drawable)
        {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = roundBitmap(bitmap, 14);
            m_rectSrc.set(0, 0, b.getWidth(), b.getHeight());
            m_rectDest.set(0, 0, getWidth(), getHeight());
            m_paint.reset();
            canvas.drawBitmap(b, m_rectSrc, m_rectDest, m_paint);
            b.recycle();
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
    private Bitmap roundBitmap(Bitmap bitmap, int roundPx)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min = (width < height ? width : height);

        //创建一个图片对象
        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        //创建一个图片游标
        //Canvas canvas = new Canvas(output);
        m_canvasTemp.setBitmap(output);
        m_rectRound.set(0, 0, min, min);
        m_rectFRoundCorner.set(0, 0, min, min);
        /* 设置取消锯齿效果 */
        m_paint.setAntiAlias(true);
        m_canvasTemp.drawARGB(0, 0, 0, 0);
        m_paint.setColor(Color.WHITE);
        //int x = bitmap.getWidth();
        /* 绘画一个圆角矩形 */
        m_canvasTemp.drawRoundRect(m_rectFRoundCorner, roundPx, roundPx, m_paint);
        m_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        m_canvasTemp.drawBitmap(bitmap, m_rectRound, m_rectRound, m_paint);

        return output;

    }
}
