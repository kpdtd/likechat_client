package com.audio.miliao.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.audio.miliao.R;
import com.audio.miliao.dialog.LoadingDialog;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchActorPage;
import com.audio.miliao.http.cmd.UpdateUserInfo;
import com.audio.miliao.theApp;
import com.audio.miliao.util.EntityUtil;
import com.audio.miliao.util.FileUtil;
import com.audio.miliao.util.StringUtil;
import com.netease.nim.uikit.common.ui.widget.CircleImageView;
import com.netease.nim.uikit.miliao.util.ImageLoaderUtil;
import com.netease.nim.uikit.miliao.vo.ActorPageVo;
import com.netease.nim.uikit.miliao.vo.ActorVo;
import com.uikit.loader.entity.LoaderAppData;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 编辑用户信息
 */
public class EditUserInfoActivity extends BaseActivity
{
    /**
     * 输入名称
     */
    private static final int REQ_INPUT_NAME = 0;
    /**
     * 输入介绍
     */
    private static final int REQ_INPUT_INTRO = 1;
    /**
     * 选择生日
     */
    private static final int REQ_PICKER_DATE = 2;
    /**
     * 选择城市
     */
    private static final int REQ_PICKER_CITY = 3;
    /**
     * 拍照获取头像
     */
    public static final int REQ_SELECT_AVATAR_FROM_CAMERA = 4;
    /**
     * 从图库选择头像
     */
    public static final int REQ_SELECT_AVATAR_FROM_ALBUM = 5;
    /**
     * 裁剪头像
     */
    public static final int REQ_CROP_AVATAR = 6;

    private CircleImageView m_imgAvatar;
    private TextView m_txtName;
    private TextView m_txtGender;
    private TextView m_txtAge;
    private TextView m_txtCity;
    private TextView m_txtIntro;
    /**
     * 调用相机拍照后的照片存储的路径
     */
    private String mAvatarTempFilePath = "";

    /**
     * 用于显示个人信息
     */
    private ActorPageVo m_actorPageVo;
    /**
     * 用于编辑个人信息
     */
    private ActorVo m_actorVo = new ActorVo();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        try
        {
            initUI();
            //updateData();
            FetchActorPage fetchActorPage = new FetchActorPage(handler(), LoaderAppData.getCurUserId(), null);
            fetchActorPage.send();
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
            case REQ_INPUT_NAME:
                onInputName(data);
                break;
            case REQ_INPUT_INTRO:
                onInputIntro(data);
                break;
//            case REQ_PICKER_DATE:
//                //onPickDate(data);
//                break;
//            case REQ_PICKER_CITY:
//                onPickCity(data);
//                break;
            case REQ_SELECT_AVATAR_FROM_ALBUM:
                // 从相册选择图片作为头像
                if (data != null)
                {
                    cropAvatar(data.getData());
                }
                break;
            case REQ_SELECT_AVATAR_FROM_CAMERA:
                // 拍照
                File temp = new File(mAvatarTempFilePath);
                cropAvatar(Uri.fromFile(temp));
                break;
            case REQ_CROP_AVATAR:
                if (data != null)
                {
                    File cropAvatar = saveCropAvatar(data);
                    if (cropAvatar != null)
                    {
                        String filePath = cropAvatar.getAbsolutePath();
                        ImageLoaderUtil.displayFromFile(m_imgAvatar, filePath);
                        byte[] fileBytes = FileUtil.getFileBytes(filePath);

                        m_actorVo.setIcon(Base64.encodeToString(fileBytes, Base64.NO_WRAP));
                        m_actorVo.setIconName(filePath);
                    }
                }
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
            m_imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
            m_txtName = (TextView) findViewById(R.id.txt_edit_user_info_name_hint);
            m_txtGender = (TextView) findViewById(R.id.txt_hint_edit_user_info_gender);
            m_txtAge = (TextView) findViewById(R.id.txt_hint_edit_user_info_age);
            m_txtCity = (TextView) findViewById(R.id.txt_hint_edit_user_info_location);
            m_txtIntro = (TextView) findViewById(R.id.txt_hint_edit_user_info_self_intro);

            mAvatarTempFilePath = FileUtil.getSDCardRootDir() + "/temp_avatar";

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
                            showChangeAvatarDialog();
                            break;
                        case R.id.lay_name:
                            Intent intentInputName = new Intent(EditUserInfoActivity.this, InputActivity.class);
                            intentInputName.putExtra("title", getString(R.string.title_input_name));
                            intentInputName.putExtra("maxLen", 10);
                            startActivityForResult(intentInputName, REQ_INPUT_NAME);
                            break;
                        case R.id.lay_gender:
                            showSelectGenderDialog();
                            break;
                        case R.id.lay_birthday:
                            DatePicker picker = new DatePicker(EditUserInfoActivity.this, DatePicker.YEAR_MONTH_DAY);
                            picker.setRangeStart(1900, 1, 1);
                            picker.setRangeEnd(2017, 11, 11);
                            picker.setCanceledOnTouchOutside(true);
                            picker.setUseWeight(true);
                            picker.setTopPadding(ConvertUtils.toPx(EditUserInfoActivity.this, 10));
                            picker.setSelectedItem(1990, 1, 1);
                            picker.setResetWhileWheel(false);
                            picker.setCycleDisable(true);
                            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener()
                            {
                                @Override
                                public void onDatePicked(String year, String month, String day)
                                {
                                    //theApp.showToast(year + "-" + month + "-" + day);
                                    onPickDate(Integer.valueOf(year),
                                            Integer.valueOf(month),
                                            Integer.valueOf(day));
                                }
                            });
                            picker.show();
                            break;
                        case R.id.lay_location:
//                            Intent intentCityPicker = new Intent(EditUserInfoActivity.this, CityPickerActivity.class);
//                            startActivityForResult(intentCityPicker, REQ_PICKER_CITY);
                            pickAddress();
                            break;
                        case R.id.lay_self_intro:
                            Intent intentInputIntro = new Intent(EditUserInfoActivity.this, InputActivity.class);
                            intentInputIntro.putExtra("title", getString(R.string.title_input_intro));
                            intentInputIntro.putExtra("maxLen", 20);
                            startActivityForResult(intentInputIntro, REQ_INPUT_INTRO);
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

    private void pickAddress()
    {
        try
        {
            ArrayList<Province> data = new ArrayList<>();
            String json = ConvertUtils.toString(getAssets().open("city2.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            AddressPicker picker = new AddressPicker(this, data);
            picker.setShadowVisible(true);
            picker.setHideProvince(false);
            picker.setHideCounty(true);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener()
            {
                @Override
                public void onAddressPicked(Province province, City city, County county)
                {
                    //theApp.showToast("province : " + province + ", city: " + city + ", county: " + county);
                    onPickCity(province.getName(), city.getName());
                }
            });
            picker.show();
        }
        catch (Exception e)
        {
            //theApp.showToast(LogUtils.toStackTraceString(e));
        }
    }

    private void updateData()
    {
        try
        {
            ActorPageVo actor = m_actorPageVo;
            if (actor == null)
            {
                return;
            }

            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar, m_actorPageVo.getIcon());
            m_txtName.setText(actor.getNickname());
            EntityUtil.setActorGenderText(m_txtGender, actor.getSex());
            m_txtAge.setText(String.valueOf(actor.getAge()));
            m_txtCity.setText(actor.getCity());
            m_txtIntro.setText(actor.getSignature());
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
                m_actorVo.setNickname(strInput);
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
                m_actorVo.setSignature(strInput);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onPickDate(int nYear, int nMonth, int nDay)
    {
        try
        {
//            int nYear = data.getIntExtra("year", 0);
//            int nMonth = data.getIntExtra("month", 0);
//            int nDay = data.getIntExtra("day", 0);
            int nAge = getAgeByBirthday(nYear, nMonth, nDay);
            m_txtAge.setText(String.valueOf(nAge));
            String strAge = String.format("%4d-%02d-%02d", nYear, nMonth, nDay);
            m_actorVo.setAge(strAge);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onPickCity(String provice, String city)
    {
        try
        {
//            String provice = data.getStringExtra("provice");
//            String city = data.getStringExtra("city");
//            String district = data.getStringExtra("district");
            m_txtCity.setText(provice + "/" + city);
            m_actorVo.setCity(provice + "/" + city);
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
//            ActorPageVo actor = AppData.getCurActorPageVo();
//            actor.setNickname(m_txtName.getText().toString());
//            actor.setIntroduction(m_txtIntro.getText().toString());

            UpdateUserInfo updateUserInfo = new UpdateUserInfo(handler(), m_actorVo, null);
            updateUserInfo.send();
            LoadingDialog.show(this, getString(R.string.notice_update_user_info));
            //finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 拍照设置头像
     */
    private void getAvatarFromCamera()
    {
        try
        {
            /**
             * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照<br>
             * 大家可以参考如下官方 文档，you_sdk_path/docs/guide/topics/media /camera.html<br>
             * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了<br>
             * 所以大家不要认为 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
             */
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mAvatarTempFilePath)));

            startActivityForResult(intent, REQ_SELECT_AVATAR_FROM_CAMERA);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            theApp.showToast(getString(R.string.toast_no_camera));
        }
    }

    /**
     * 从手机相册选择图片设置头像
     */
    private void getAvatarFromAlbum()
    {
        try
        {
            /**
             * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
             * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
             */
            Intent intent = new Intent(Intent.ACTION_PICK, null);

            /**
             * 下面这句话，与其它方式写是一样的效果 <br>
             * 如果： intent.setData(MediaStore.Images.Media.
             * EXTERNAL_CONTENT_URI); intent.setType(""image/*"); <br>
             * 设置数据类型 <br>
             * 如果好友们要限制上传到服务器的图片类型时可以直接写如： "image/jpeg 、 image/png等的类型" <br>
             * 这个地方有个疑问，希望高手解答下： 就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
             */
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(intent, REQ_SELECT_AVATAR_FROM_ALBUM);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            theApp.showToast(getString(R.string.toast_no_album));
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void cropAvatar(Uri uri)
    {
        try
        {
            /**
             * 至于下面这个Intent的ACTION是怎么知道的<br>
             * 大家可以看下自己路径下的如下网页
             * yourself_sdk_path/docs/reference/android/content/Intent.html <br>
             * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过<br>
             * 其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的<br>
             */
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高 单位像素
            intent.putExtra("outputX", 180);
            intent.putExtra("outputY", 180);
            intent.putExtra("scale", true); // 去黑边
            intent.putExtra("scaleUpIfNeeded", true); // 去黑边
            intent.putExtra("return-data", true);

            startActivityForResult(intent, REQ_CROP_AVATAR);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            theApp.showToast(getString(R.string.toast_no_album));
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private File saveCropAvatar(Intent picdata)
    {
        try
        {
            Bundle extras = picdata.getExtras();
            if (extras != null)
            {
                Bitmap photo = extras.getParcelable("data");
                if (photo != null)
                {
                    /**
                     * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
                     */

                    /**
                     * ByteArrayOutputStream stream = new ByteArrayOutputStream();<br>
                     * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);<br>
                     * byte[] b = stream.toByteArray(); <br>
                     * 将图片流以字符串形式存储下来 tp = new String(Base64Coder.encodeLines(b));<br>
                     * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了<br>
                     * 服务器处理的方法是服务器那边的事了，吼吼 <br>
                     * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换<br>
                     * 为我们可以用的图片类型就OK啦...吼吼<br>
                     * Bitmap dBitmap = BitmapFactory.decodeFile(tp);<br>
                     * Drawable drawable = new BitmapDrawable(dBitmap);
                     */

                    // 裁剪后的头像图片路径
                    String cropAvatarPath = FileUtil.getSDCardAppRootDir(getApplicationContext(),
                            "content/temp") + "/crop_avatar.jpg";
                    File cropAvatar = FileUtil.saveBitmapAsFile(photo, Bitmap.CompressFormat.JPEG,
                            cropAvatarPath);

                    FileUtil.deleteFile(mAvatarTempFilePath);

                    return cropAvatar;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private void showSelectGenderDialog()
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
                    m_actorVo.setSex(item + 1);
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

    private void showChangeAvatarDialog()
    {
        try
        {
            final String[] items = {
                    getString(R.string.txt_edit_user_info_select_avatar_from_camera),
                    getString(R.string.txt_edit_user_info_select_avatar_from_album)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_change_avatar);
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int item)
                {
                    switch (item)
                    {
                    case 0:
                        getAvatarFromCamera();
                        break;
                    case 1:
                        getAvatarFromAlbum();
                        break;
                    }
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
    private static int getAgeByBirthday(int nYear, int nMonth, int nDay)
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

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_ACTOR_PAGE:
            FetchActorPage fetchActorPage = (FetchActorPage) msg.obj;
            if (FetchActorPage.isSucceed(fetchActorPage))
            {
                m_actorPageVo = fetchActorPage.rspActorPageVo;
//                m_actorVo.setNickname(m_actorPageVo.getNickname());
//                m_actorVo.setSex(m_actorPageVo.getSex());
//                m_actorVo.setAge(m_actorPageVo.getAge());
//                m_actorVo.setCity(m_actorPageVo.getCity());
//                m_actorVo.setSignature(m_actorPageVo.getSignature());
                updateData();
            }
            else
            {

            }
            break;
        case HttpUtil.RequestCode.UPDATE_USER_INFO:
            LoadingDialog.dismiss();
            UpdateUserInfo updateUserInfo = (UpdateUserInfo) msg.obj;
            if (UpdateUserInfo.isSucceed(updateUserInfo))
            {
                theApp.showToast(getString(R.string.toast_update_user_info_succeed));
                setResult(RESULT_OK);
                finish();
            }
            else
            {
                theApp.showToast(getString(R.string.toast_update_user_info_failed));
            }
            break;
        }
    }
}
