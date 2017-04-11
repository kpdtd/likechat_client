package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;

/**
 * 编辑用户信息
 */
public class EditUserInfoActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        try
        {
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
            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.img_back:
                            finish();
                            break;
                        case R.id.txt_save:
                            finish();
                            break;
                        case R.id.lay_avatar:
                            break;
                        case R.id.lay_name:
                            Intent intentInputName = new Intent(EditUserInfoActivity.this, InputActivity.class);
                            intentInputName.putExtra("title", getString(R.string.title_input_name));
                            intentInputName.putExtra("maxLen", 10);
                            startActivity(intentInputName);
                            break;
                        case R.id.lay_gender:
                            break;
                        case R.id.lay_birthday:
                            break;
                        case R.id.lay_location:
                            break;
                        case R.id.lay_self_intro:
                            Intent intentInputIntro = new Intent(EditUserInfoActivity.this, InputActivity.class);
                            intentInputIntro.putExtra("title", getString(R.string.title_input_intro));
                            intentInputIntro.putExtra("maxLen", 20);
                            startActivity(intentInputIntro);
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.txt_save).setOnClickListener(clickListener);
            findViewById(R.id.lay_avatar).setOnClickListener(clickListener);
            findViewById(R.id.lay_name).setOnClickListener(clickListener);
            findViewById(R.id.lay_gender).setOnClickListener(clickListener);
            findViewById(R.id.lay_birthday).setOnClickListener(clickListener);
            findViewById(R.id.lay_location).setOnClickListener(clickListener);
            findViewById(R.id.lay_self_intro).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
