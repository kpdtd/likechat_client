package com.likechat.likechat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 圆形图片
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView
{
//    private Path path;
//    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
    private Paint m_paint;
    private Rect m_rectSrc, m_rectDest;

    public CircleImageView(Context context)
    {
        super(context);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        m_paint = new Paint();

        m_rectSrc = new Rect();
        m_rectDest = new Rect();
    }

    @Override
    protected void onDraw(Canvas cns)
    {
        Drawable drawable = getDrawable();
        if (null != drawable)
        {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = circleDraw(bitmap);
            m_rectSrc.set(0, 0, b.getWidth(), b.getHeight());
            m_rectDest.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
            m_paint.reset();
            cns.drawBitmap(b, m_rectSrc, m_rectDest, m_paint);
            b.recycle();
        } else
        {
            super.onDraw(cns);
        }
    }

    private Bitmap circleDraw(Bitmap bitmap)
    {
        int r = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //Rect rectSource = null;
        if (width > height)
        {
            r = height;
        }
        else
        {
            r = width;
        }
        //创建一个图片对象
        Bitmap output = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);
        //创建一个图片游标
        Canvas canvas = new Canvas(output);
        final Rect rect = new Rect(0, 0, r, r);
        /* 设置取消锯齿效果 */
        m_paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        m_paint.setColor(Color.WHITE);
        /* 绘画一个圆图形 */
        canvas.drawCircle(r / 2, r / 2, r / 2, m_paint);
        m_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, m_paint);
        return output;
    }
}
