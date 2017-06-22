package com.audio.miliao.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audio.miliao.R;


/**
 * 定制的等待框
 * 
 */
public class LoadingDialog
{
	/** dialog实例 */
	private static Dialog ms_dialog = null;


	/**
	 * 显示对话框
	 * @param context
	 * @param strMsg 要显示的文字(最多显示三行文字)
	 */
	public static void show(final Context context, final String strMsg)
	{
		show(context, strMsg, false, null);
	}

	/**
	 * 显示对话框
	 * @param context
	 * @param strMsg 要显示的文字(最多显示三行文字)
	 * @param bCancelable 是否可取消
	 * @param listener 取消时的监听器
	 */
	public static void show(final Context context, final String strMsg, final boolean bCancelable, final OnDismissListener listener)
	{
		try
		{
			dismiss();

			ms_dialog = createLoadingDialog(context, strMsg, bCancelable, listener);
			ms_dialog.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 关闭等待界面
	 */
	public static void dismiss()
	{
		try
		{
			if (ms_dialog != null)
			{
				ms_dialog.dismiss();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ms_dialog = null;
		}
	}

	/**
	 * 得到自定义的 progressDialog
	 * @param context
	 * @param strMsg 要显示的字符串
	 * @param bCancelable 是否可取消
	 * @param listener 取消时的监听器
	 * @return
	 */
	@SuppressLint("InflateParams")
	private static Dialog createLoadingDialog(final Context context, final String strMsg, final boolean bCancelable, OnDismissListener listener)
	{
		try
		{
			LayoutInflater inflater = LayoutInflater.from(context);

			// 得到加载view
			View root = inflater.inflate(R.layout.dialog_loading, null);
			
			// main.xml中的ImageView
			ImageView imgLoading = (ImageView) root.findViewById(R.id.img_loading);
			
			// 提示文字
			TextView txtLoading = (TextView) root.findViewById(R.id.txt_loading);

			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_animation);
			
			// 使用ImageView显示动画
			imgLoading.startAnimation(hyperspaceJumpAnimation);
			
			// 设置加载信息
			txtLoading.setText(strMsg);
			
			// 创建自定义样式dialog
			Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_style);
			
			// 可以用"返回键"取消
			loadingDialog.setCancelable(bCancelable);
			
			// 设置布局
			loadingDialog.setContentView(root, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			
			if (listener == null)
			{
				listener = new OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						ms_dialog = null;
					}
				};
			}
			
			loadingDialog.setOnDismissListener(listener);
			return loadingDialog;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
