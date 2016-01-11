package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.PictureUtil;
import com.topad.util.UploadUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ${todo}<上传照片>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class UploadPicActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = UploadPicActivity.class.getSimpleName();
    /**
     * 上下文
     **/
    private Activity mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    ImageView pic_1, pic_2;
    Button btn_save;
    String title;
    String pathString1, pathString2;
    String img_name1, img_name2;
    /**
     * 0：上传身份证  1：上传毕业证  2：上传名片  3，上传公司认证
     */
    int type;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_upload_shenfenzheng;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        type = intent.getIntExtra("type", 0);
        mTitleView = (TitleView) findViewById(R.id.title);
        pic_1 = (ImageView) findViewById(R.id.pic_1);
        pic_2 = (ImageView) findViewById(R.id.pic_2);
        if (type != 0) {
            pic_2.setVisibility(View.GONE);
        }
        btn_save = (Button) findViewById(R.id.btn_save);
        pic_1.setOnClickListener(this);
        pic_2.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        switch (type) {
            case 0:
                break;
            case 1:
                pic_1.setImageResource(R.drawable.upload_biyezheng);
                break;
            case 2:
                pic_1.setImageResource(R.drawable.upload_mingpian);
                break;
            case 3:
                pic_1.setImageResource(R.drawable.uploadback);
                break;
        }
        int width = Utils.getScreenWidth(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pic_1.getLayoutParams();
        params.width = width * 2 / 3;
        params.height = params.width * 2 / 3;
        if (type == 0) {
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) pic_2.getLayoutParams();
            params2.width = width * 2 / 3;
            params2.height = params2.width * 2 / 3;
        }

    }

    @Override
    public void initData() {
        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle(title);
        mTitleView.setLeftVisiable(true);
        mTitleView.setRightVisiable(false);
        mTitleView.setLeftClickListener(new TitleRightOnClickListener());
    }


    /**
     * 顶部布局--注册按钮事件监听
     */
    public class TitleRightOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }

    }

    final int PICKPHOTO = 1;
    final int PICKPHOTO_2 = 2;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pic_1://

                Intent intent1 = new Intent(mContext,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent1, PICKPHOTO);
                break;
            case R.id.pic_2://

                Intent intent2 = new Intent(mContext,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent2, PICKPHOTO_2);
                break;
            case R.id.btn_save://确认保存

                if (!Utils.isEmpty(pathString1)) {
                    uploadPic(pathString1);
                }
                if (!Utils.isEmpty(pathString2)) {
                    uploadPic(pathString2);
                }
                break;

            default:
                break;
        }
    }

    public void uploadPic(final String picPath) {

        File file = new File(picPath);
        if (file != null) {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.UPLOAD_PHOTO).append("?");
            String url = sb.toString();
            String fileKey = "userfile";
            UploadUtil uploadUtil = UploadUtil.getInstance(mContext);
            uploadUtil.setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {
                @Override
                public void onUploadDone(int responseCode, String message) {
                    LogUtil.d("#responseCode:" + responseCode);
                    LogUtil.d("#message:" + message);
                    if (responseCode == 1 && !Utils.isEmpty(message)) {//上传图片之后服务器返回数据  有可能失败
                        try {
                            JSONObject respObj = new JSONObject(message);
                            String status = respObj.getString("status");// 状态码
                            String msg = respObj.getString("msg");// 错误信息
                            String img = respObj.getString("img");// 图片名
                            if (Utils.isEmpty(img_name1)) {
                                img_name1 = img;
                            } else {
                                img_name2 = img;
                            }

                            if (!Utils.isEmpty(img_name1)) {
                                if (type == 0) {
                                    if (Utils.isEmpty(img_name2) && !Utils.isEmpty(pathString2)) {
                                        uploadPic(pathString2);
                                    }
                                    if(!Utils.isEmpty(img_name1)&&!Utils.isEmpty(img_name2)){
                                        submit();
                                    }else{
                                        Toast.makeText(mContext, "身份证上传失败", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    submit();
                                }

                            } else {//上传失败
                                if (!Utils.isEmpty(msg)) {
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//
                    } else {//上传失败  服务器报错
                        Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                }

                int totalSize;

                @Override
                public void onUploadProcess(int uploadSize) {
//                    LogUtil.d("#uploadSize:" + uploadSize);
//                    if(totalSize!=0){
//                        double percent=uploadSize*100/totalSize;
//                        LogUtil.d("#percent:" + percent);
//                    }
                }

                @Override
                public void initUpload(int fileSize) {
//                    totalSize=fileSize;
//                    LogUtil.d("#fileSize:" + fileSize);
                }
            }); //设置监听器监听上传状态

            Map<String, String> params = new HashMap<String, String>();
            uploadUtil.uploadFile(picPath, fileKey, url, params);
//            UploadUtil.uploadFile(file, url, new UploadInterface() {
//                @Override
//                public void onSucceed(BaseBean bean) {
//
//                }
//
//                @Override
//                public void onFailed(BaseBean bean) {
//
//                }
//            });
        }


    }

    public void submit() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.UPLOAD_HEAD).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("img1", img_name1);
        rp.add("img2", img_name2);
        switch (type) {
            case 0:
                rp.add("imgtype", "1");
                break;
            case 1:
                rp.add("imgtype", "2");
                break;
            case 2:
                rp.add("imgtype", "3");
                break;
            case 3:
                rp.add("imgtype", "4");
                break;
        }


        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                BaseBean base = (BaseBean) t;
                if (base != null) {
                    ToastUtil.show(mContext, base.getMsg());
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, BaseBean.class);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICKPHOTO:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
                    pathString1 = picPath;
                    if (!Utils.isEmpty(picPath)) {
                        Bitmap image = PictureUtil
                                .getSmallBitmap(picPath);
                        if (image != null) {
                            pic_1.setImageBitmap(image);
                        }
                    }
                }
                break;
            case PICKPHOTO_2:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
                    pathString2 = picPath;
                    if (!Utils.isEmpty(picPath)) {
                        Bitmap image = PictureUtil
                                .getSmallBitmap(picPath);
                        if (image != null) {
                            pic_2.setImageBitmap(image);
                        }
                    }
                }
                break;
            default:
                break;

        }
    }


}
