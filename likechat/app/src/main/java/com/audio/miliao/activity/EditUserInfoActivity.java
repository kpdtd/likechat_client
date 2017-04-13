package com.audio.miliao.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.dialog.DatePickerActivity;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.User;
import com.audio.miliao.util.StringUtil;

import java.util.Calendar;

/**
 * 编辑用户信息
 */
public class EditUserInfoActivity extends BaseActivity
{
    /**
     * 输入名称
     */
    private static final int REQ_CODE_INPUT_NAME = 0;
    /**
     * 输入介绍
     */
    private static final int REQ_CODE_INPUT_INTRO = 1;
    /**
     * 选择生日
     */
    private static final int REQ_CODE_PICKER_DATE = 2;
    /**
     * 选择城市
     */
    private static final int REQ_CODE_PICKER_CITY = 3;

    private TextView m_txtName;
    private TextView m_txtGender;
    private TextView m_txtAge;
    private TextView m_txtCity;
    private TextView m_txtIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        try
        {
            initUI();
            updateData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if (resultCode != RESULT_OK)
            {
                return;
            }

            switch (requestCode)
            {
            case REQ_CODE_INPUT_NAME:
                onInputName(data);
                break;
            case REQ_CODE_INPUT_INTRO:
                onInputIntro(data);
                break;
            case REQ_CODE_PICKER_DATE:
                onPickDate(data);
                break;
            case REQ_CODE_PICKER_CITY:
                onPickCity(data);
                break;
            }
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
            m_txtName = (TextView) findViewById(R.id.txt_edit_user_info_name_hint);
            m_txtGender = (TextView) findViewById(R.id.txt_hint_edit_user_info_gender);
            m_txtAge = (TextView) findViewById(R.id.txt_hint_edit_user_info_age);
            m_txtCity = (TextView) findViewById(R.id.txt_hint_edit_user_info_location);
            m_txtIntro = (TextView) findViewById(R.id.txt_hint_edit_user_info_self_intro);

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
                            onSave();
                            break;
                        case R.id.lay_avatar:
                            break;
                        case R.id.lay_name:
                            Intent intentInputName = new Intent(EditUserInfoActivity.this, InputActivity.class);
                            intentInputName.putExtra("title", getString(R.string.title_input_name));
                            intentInputName.putExtra("maxLen", 10);
                            startActivityForResult(intentInputName, REQ_CODE_INPUT_NAME);
                            break;
                        case R.id.lay_gender:
                            showListDialog();
                            break;
                        case R.id.lay_birthday:
                            Intent intentDatePicker = new Intent(EditUserInfoActivity.this, DatePickerActivity.class);
                            startActivityForResult(intentDatePicker, REQ_CODE_PICKER_DATE);
                            break;
                        case R.id.lay_location:
                            break;
                        case R.id.lay_self_intro:
                            Intent intentInputIntro = new Intent(EditUserInfoActivity.this, InputActivity.class);
                            intentInputIntro.putExtra("title", getString(R.string.title_input_intro));
                            intentInputIntro.putExtra("maxLen", 20);
                            startActivityForResult(intentInputIntro, REQ_CODE_INPUT_INTRO);
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

    private void updateData()
    {
        try
        {
            User user = AppData.getCurUser();
            if (user == null)
            {
                return;
            }

            m_txtName.setText(user.name);
            m_txtIntro.setText(user.intro);
            String strGender = (user.gender == User.GENDER_FEMALE ? getString(R.string.txt_female) : getString(R.string.txt_male));
            m_txtGender.setText(strGender);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onInputName(Intent data)
    {
        try
        {
            String strInput = data.getStringExtra("input");
            if (StringUtil.isNotEmpty(strInput))
            {
                m_txtName.setText(strInput);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onInputIntro(Intent data)
    {
        try
        {
            String strInput = data.getStringExtra("input");
            if (StringUtil.isNotEmpty(strInput))
            {
                m_txtIntro.setText(strInput);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onPickDate(Intent data)
    {
        try
        {
            int nYear = data.getIntExtra("year", 0);
            int nMonth = data.getIntExtra("month", 0);
            int nDay = data.getIntExtra("day", 0);
            int nAge = getAgeByBirthday(nYear, nMonth, nDay);
            m_txtAge.setText(String.valueOf(nAge));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onPickCity(Intent data)
    {
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onSave()
    {
        try
        {
            User user = AppData.getCurUser();
            user.name = m_txtName.getText().toString();
            user.intro = m_txtIntro.getText().toString();

            finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showListDialog()
    {
        try
        {
            final String[] items = {getString(R.string.txt_male), getString(R.string.txt_female)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_select_gender);
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int item)
                {
                    m_txtGender.setText(items[item]);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(int nYear, int nMonth, int nDay)
    {
        Calendar cal = Calendar.getInstance();

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - nYear;

        if (monthNow <= nMonth)
        {
            if (monthNow == nMonth)
            {
                // monthNow==monthBirth
                if (dayOfMonthNow < nDay)
                {
                    age--;
                }
            }
            else
            {
                // monthNow>monthBirth
                age--;
            }
        }

        return age;
    }
}
