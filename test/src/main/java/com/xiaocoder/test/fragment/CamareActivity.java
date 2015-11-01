package com.xiaocoder.test.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.fragment.XCCameraPhotoFragment;
import com.xiaocoder.android.fw.general.fragment.XCCameraPhotoFragment.OnCaremaSelectedFileListener;
import com.xiaocoder.android.fw.general.fragment.XCLocalPhotoFragment;
import com.xiaocoder.android.fw.general.fragment.XCLocalPhotoFragment.OnLocalSelectedFileListener;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.io.File;

public class CamareActivity extends MActivity {
	XCCameraPhotoFragment camera_fragment;
	XCLocalPhotoFragment local_fragment;
	ImageView test_imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_camare);
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initWidgets() {
		camera_fragment = new XCCameraPhotoFragment();
		local_fragment = new XCLocalPhotoFragment();

		test_imageview = getViewById(R.id.test_imageview);
//		camera_fragment.setIsAllowResizeImage(true);
		local_fragment.setImage(com.xiaocoder.android_fw_general.R.drawable.xc_d_test_01);
		local_fragment.setIsAllowResizeImage(true);

		addFragment(R.id.xc_id_fragment_test_local, local_fragment);
		addFragment(R.id.xc_id_fragment_test_camera, camera_fragment);

		showBottomLayout(true);

	}

	@Override
	public void listeners() {
		camera_fragment.setOnCaremaSelectedFileListener(new OnCaremaSelectedFileListener() {

			@Override
			public void onCaremaSelectedFile(File file) {
				XCApp.i(Uri.fromFile(file));
				XCApp.i(file.getAbsolutePath());
				XCApp.i(file.toURI().toString());
				test_imageview.setImageURI(Uri.fromFile(file));
			}
		});

		local_fragment.setOnLocalSelectedFileListener(new OnLocalSelectedFileListener() {

			@Override
			public void onLocalSelectedFile(File file) {
				XCApp.i(Uri.fromFile(file));
				XCApp.i(file.getAbsolutePath());
				XCApp.i(file.toURI().toString());
				test_imageview.setImageURI(Uri.fromFile(file));
			}
		});
	}

}
