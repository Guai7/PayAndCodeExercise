package com.study.payandcodeexercise;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MainActivity extends AppCompatActivity {


    private Button mainSmBtn;
    private EditText mainTextEdit;
    private Button mainScBtn;
    private ImageView mainImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //隐藏系统默认的标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //初始化相机权限
        ZXingLibrary.initDisplayOpinion(this);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA}, 1);


        mainSmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
            }
        });

        mainScBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第一个也可以是图片的网址展示图片，也可以是网址进行跳转，如果是图片的话size大一些就可以了
                Bitmap bitmap = CodeUtils.createImage(mainTextEdit.getText().toString(),300, 300,null);
                mainImg.setImageBitmap(bitmap);
            }
        });

    }

    private void initView() {
        mainSmBtn = findViewById(R.id.main_sm_btn);
        mainTextEdit = findViewById(R.id.main_text_edit);
        mainScBtn = findViewById(R.id.main_sc_btn);
        mainImg = findViewById(R.id.main_img);
    }

    //获取手机相机权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(MainActivity.this, "请打开相机权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //处理扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    获取到扫描的结果
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }


    }
}