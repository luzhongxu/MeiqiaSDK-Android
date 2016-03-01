package com.meiqia.meiqiasdk.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_CONVERSATION_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 替换成自己的key
        // 发布sdk时用
        String meiqiaKey = "a71c257c80dfe883d92a64dca323ec20";
        // 给测试打包时用，TODO 正式发布前记得删除
        meiqiaKey = "67c9f3bcdc08c7cf435d7f2527378fa4";

        MQManager.init(this, meiqiaKey, new OnInitCallback() {
            @Override
            public void onSuccess(String clientId) {
                Toast.makeText(MainActivity.this, "init success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(MainActivity.this, "int failure", Toast.LENGTH_SHORT).show();
            }
        });
        MQManager.setDebugMode(true);

    }

    /**
     * 咨询客服
     *
     * @param v
     */
    public void conversation(View v) {
        // 不兼容Android6.0动态权限
//        conversation();

        // 兼容Android6.0动态权限
        conversationWrapper();
    }

    /**
     * 开发者功能
     *
     * @param v
     */
    public void developer(View v) {
        startActivity(new Intent(MainActivity.this, ApiSampleActivity.class));
    }

    /**
     * 自定义 Activity
     *
     * @param view
     */
    public void customizedConversation(View view) {
        startActivity(new Intent(MainActivity.this, CustomizedMQConversationActivity.class));
    }

    // 处理 Android 6.0 的权限获取
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(List<String> perms) {
        MQUtils.show(this, R.string.mq_permission_denied_tip);
    }

    @AfterPermissionGranted(REQUEST_CODE_CONVERSATION_PERMISSIONS)
    private void conversationWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            conversation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.mq_runtime_permission_tip), REQUEST_CODE_CONVERSATION_PERMISSIONS, perms);
        }
    }

    private void conversation() {
//        startActivity(new Intent(MainActivity.this, MQConversationActivity.class));


        MQConfig.backArrowIconResId = android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha;
        MQConfig.bgColorTitle = R.color.colorPrimary;
        MQConfig.textColorTitle = android.R.color.white;
        MQConfig.titleGravity = MQConfig.MQTitleGravity.LEFT;
        startActivity(new Intent(MainActivity.this, MQConversationActivity.class));
    }
}