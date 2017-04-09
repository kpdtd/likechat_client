package com.likechat.likechat.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.likechat.likechat.R;
import com.likechat.likechat.util.ImageLoaderUtil;

import org.json.JSONArray;

/**
 * 图片浏览
 */
public class ImageBrowseActivity extends BaseActivity
{
    private int m_nPositoin = 0;
    private int m_nCount    = 0;
    private String m_strPhotoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);

        try
        {
            m_nPositoin = getIntent().getIntExtra("pos", -1);
            m_nCount = getIntent().getIntExtra("count", -1);
            m_strPhotoUrl = getIntent().getStringExtra("urls");

            initUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initUI()
    {
        try
        {
            ImageView ivPhoto = (ImageView)findViewById(R.id.img_photo);
            TextView txtCount = (TextView)findViewById(R.id.txt_count);

            if (m_nCount > 0 && m_nPositoin > -1)
            {
                JSONArray jsonArray = new JSONArray((null == m_strPhotoUrl || "".equals(m_strPhotoUrl)) ? "[]" : m_strPhotoUrl);
                if (m_nPositoin < jsonArray.length())
                {
                    ImageLoaderUtil.displayListAvatarImageFromAsset(ivPhoto, jsonArray.getString(m_nPositoin));
                }
                txtCount.setText("" + (m_nPositoin + 1) + "/" + m_nCount);
            }
            else
            {
                ivPhoto.setImageResource(0);
                txtCount.setText("");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
