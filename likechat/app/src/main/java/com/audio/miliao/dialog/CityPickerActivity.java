package com.audio.miliao.dialog;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.audio.miliao.R;
import com.audio.miliao.activity.BaseActivity;
import com.audio.miliao.xml.CityModel;
import com.audio.miliao.xml.DistrictModel;
import com.audio.miliao.xml.ProvinceModel;
import com.audio.miliao.xml.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 城市选择框
 */
public class CityPickerActivity extends BaseActivity
{
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
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

    private void initUI()
    {
        try
        {
            mViewProvince = (WheelView) findViewById(R.id.whl_province);
            mViewCity = (WheelView) findViewById(R.id.whl_city);
            mViewDistrict = (WheelView) findViewById(R.id.whl_district);
            mBtnConfirm = (Button) findViewById(R.id.btn_ok);

            mViewProvince.setDrawShadows(false);
            mViewProvince.setWheelBackground(R.drawable.transparent);
            mViewProvince.setWheelForeground(R.drawable.layer_list_time_picker_left);

            mViewCity.setDrawShadows(false);
            mViewCity.setWheelBackground(R.drawable.transparent);
            mViewCity.setWheelForeground(R.drawable.layer_list_time_picker_left);

            mViewDistrict.setDrawShadows(false);
            mViewDistrict.setWheelBackground(R.drawable.transparent);
            mViewDistrict.setWheelForeground(R.drawable.layer_list_time_picker_left);

            mBtnConfirm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent data = new Intent();
                    data.putExtra("provice", mCurrentProviceName);
                    data.putExtra("city", mCurrentCityName);
                    data.putExtra("district", mCurrentDistrictName);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });

            mViewProvince.addChangingListener(new OnWheelChangedListener()
            {
                @Override
                public void onChanged(WheelView wheelView, int oldValue, int newValue)
                {
                    updateCities();
                }
            });

            mViewCity.addChangingListener(new OnWheelChangedListener()
            {
                @Override
                public void onChanged(WheelView wheelView, int oldValue, int newValue)
                {
                    updateAreas();
                }
            });

            mViewDistrict.addChangingListener(new OnWheelChangedListener()
            {
                @Override
                public void onChanged(WheelView wheelView, int oldValue, int newValue)
                {
                    mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                    mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
                }
            });
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
            initProvinceDatas();
            mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(this, mProvinceDatas));
            // 设置可见条目数量
            mViewProvince.setVisibleItems(7);
            mViewCity.setVisibleItems(7);
            mViewDistrict.setVisibleItems(7);
            updateCities();
            updateAreas();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void initProvinceDatas()
    {
        List<ProvinceModel> provinceList;
        AssetManager asset = getAssets();
        try
        {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty())
            {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty())
                {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++)
            {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++)
                {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++)
                    {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {

        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas()
    {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null)
        {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities()
    {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null)
        {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
}
