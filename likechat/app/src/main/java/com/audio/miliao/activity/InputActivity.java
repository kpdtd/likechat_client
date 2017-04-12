package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.audio.miliao.R;

/**
 * 编辑用户信息时输入界面
 */
public class InputActivity extends BaseActivity
{
    /** 标题 */
    private String m_strTitle;
    /** 总共能输入多少个字 */
    private int m_nMaxLen;
    /** 输入框 */
    private EditText m_edtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        try
        {
            m_strTitle = getIntent().getStringExtra("title");
            m_nMaxLen = getIntent().getIntExtra("maxLen", 0);
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
            m_edtInput = (EditText) findViewById(R.id.edt_input);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.txt_cancel:
                            finish();
                            break;
                        case R.id.txt_save:
                            String strInput = m_edtInput.getText().toString().trim();
                            Intent data = new Intent();
                            data.putExtra("input", strInput);
                            setResult(RESULT_OK, data);
                            finish();
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.txt_cancel).setOnClickListener(clickListener);
            findViewById(R.id.txt_save).setOnClickListener(clickListener);

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);
            txtTitle.setText(m_strTitle);

            m_edtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(m_nMaxLen)});
            m_edtInput.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    setLenLimitText();
                }
            });

            setLenLimitText();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setLenLimitText()
    {
        try
        {
            TextView txtMaxLen = (TextView) findViewById(R.id.txt_hint);
            int lenLimit = m_nMaxLen - m_edtInput.getText().length();
            String strMaxLen = String.format(getString(R.string.edt_hint_input_max_len), lenLimit);
            txtMaxLen.setText(strMaxLen);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
