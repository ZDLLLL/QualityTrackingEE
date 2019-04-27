package zjc.qualitytrackingee.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.anlia.photofactory.factory.PhotoFactory;
import com.anlia.photofactory.permission.PermissionAlwaysDenied;
import com.anlia.photofactory.result.ResultData;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zjc.qualitytrackingee.BuildConfig;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.FileUtil;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static com.anlia.photofactory.factory.PhotoFactory.ERROR_CROP_DATA;
import static zjc.qualitytrackingee.MyApplication.getContext;

public class UserInfoActivity extends AppCompatActivity {
    public static String image;
    @BindView(R.id.user_info_back_ll)
    LinearLayout user_info_back_ll;
    @BindView(R.id.user_info_company_tv)
    TextView user_info_company_tv;
    @BindView(R.id.user_info_job_tv)
    TextView user_info_job_tv;
    @BindView(R.id.user_info_name_tv)
    TextView user_info_name_tv;
    @BindView(R.id.user_info_phone_tv)
    TextView user_info_phone_tv;
    @BindView(R.id.user_info_image_iv)
    ImageView user_info_image_iv;
    @BindView(R.id.user_info_save_tv)
    TextView user_info_save_tv;
    @BindView(R.id.userPhoneLayout)RelativeLayout userPhoneLayout;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    private File tempFile;
    //public ImageView headImage1;
    private RelativeLayout userImageLayout;
    private  String e_img;
    private Bitmap bmp;
    private final String TAG = "UserinfoActivity";
    private String picName;
    private static Bitmap bm;
    private PhotoFactory photoFactory;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        photoFactory=new PhotoFactory(this);

        e_img = getIntent().getStringExtra("image");
        picName = Calendar.getInstance().getTimeInMillis() + ".png";
        MyApplication.addDestoryActivity(UserInfoActivity.this,"UserInfoActivity");
        user_info_image_iv = findViewById(R.id.user_info_image_iv);
        userImageLayout = findViewById(R.id.userImageLayout);
        userImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadHeadImage();
            }
        });
        getUser();

    }

    @OnClick(R.id.user_info_back_ll)
    public void user_info_back_ll_Onclick() {
        finish();
    }

    @OnClick(R.id.userPhoneLayout)
    public void userPhoneLayout_Onclick(){
            Intent intent=new Intent(UserInfoActivity.this,ChangePhoneActivity.class);
            startActivity(intent);

    }
    public void getUser() {
        String url = Net.GetUser + "?e_phone=" + MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(), url, "getuser",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("personInfo");
                            String e_name = jsonObject1.getString("e_name");
                            String e_img = jsonObject1.getString("e_img");
                            String e_job = jsonObject1.getString("j_name");
                            String e_compamy = jsonObject1.getString("c_name");
                            image = e_img.replaceAll("\\\\", "");
                            user_info_phone_tv.setText(MyApplication.getE_phone());
                            user_info_job_tv.setText(e_job);
                            user_info_company_tv.setText(e_compamy);
                            Glide.with(getContext())
                                    .load(e_img)
                                    .asBitmap()
                                    .error(R.drawable.head1)
                                    .into(user_info_image_iv);
                            user_info_name_tv.setText(e_name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(), "╮(╯▽╰)╭连接不上了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.user_info_save_tv)
    public void user_info_save_tv_Onclick() {
//        e_img=user_info_image_iv.getImageMatrix().toString();
//        bmp = BitmapFactory.decodeFile(image);
        UploadImg();
        //finish();
    }
    public void UploadImg(){
//        bmp = BitmapFactory.decodeFile(bm);
     final String result=convertIconToString(bm);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient mOkHttpClient=new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                .add("e_img", result)
                                .add("e_phone",MyApplication.getE_phone())
                                .build();
                        Request request = new Request.Builder()
                                .url(Net.UploadImg)
                                .post(formBody)
                                .build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                try {
                                    JSONObject jsonObject=new JSONObject(res);
                                    JSONObject jsonObject1=jsonObject.getJSONObject("EupImg");
                                    if ("1".equals(jsonObject1.getString("status"))){
                                        Looper.prepare();
                                        Toast.makeText(UserInfoActivity.this,"成功修改头像！",Toast.LENGTH_LONG).show();
                                       finish();

                                        Looper.loop();
                                    }else{
                                        Looper.prepare();
                                        Toast.makeText(UserInfoActivity.this,"修改头像失败！",Toast.LENGTH_LONG).show();
                                        finish();
                                        Looper.loop();


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });      } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }




    public  String convertIconToString(Bitmap bitmap)
    {  ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  int options = 100;
        while ( baos.toByteArray().length / 1024>500) { //循环判断如果压缩后图片是否大于1000kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            Log.i("Compress",baos.toByteArray().length+"");
        }
        byte[] bytes = baos.toByteArray();return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_user_info, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        PhotoFactory.setPermissionAlwaysDeniedAction(new PermissionAlwaysDenied.Action() {
            @Override
            public void onAction(Context context, List<String> permissions, final PermissionAlwaysDenied.Executor executor) {
                List<String> permissionNames = PhotoFactory.transformPermissionText(context, permissions);
                String permissionText = TextUtils.join("权限\n", permissionNames);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("权限说明");
                builder.setMessage("您禁止了以下权限的动态申请：\n\n" + permissionText + "权限\n\n是否去应用权限管理中手动授权呢？");
                builder.setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.toSetting();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                //跳转到调用系统相机
                photoFactory = new PhotoFactory(UserInfoActivity.this, Environment.getExternalStorageDirectory() + "/" + "DCIM", picName);
                photoFactory.FromCamera()
                        .AddOutPutExtra()
                        .StartForResult(new PhotoFactory.OnResultListener() {
                            @Override
                            public void onCancel() {
                                Log.e(TAG, "取消从相册选择");
                            }

                            @Override
                            public void onSuccess(ResultData resultData) {
                                dealSelectPhoto(resultData);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                popupWindow.dismiss();
            }

        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                photoFactory = new PhotoFactory(UserInfoActivity.this, Environment.getExternalStorageDirectory() + "/" + "DCIM", picName);
                photoFactory.FromGallery()
                        .StartForResult(new PhotoFactory.OnResultListener() {
                            @Override
                            public void onCancel() {
                                Log.e(TAG, "取消从相册选择");
                            }

                            @Override
                            public void onSuccess(ResultData resultData) {
                                dealSelectPhoto(resultData);
//                            Uri uri = resultData.GetUri();
//                            imgPhoto.setImageURI(uri);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    private void dealSelectPhoto(ResultData resultData) {
        Uri uri = resultData
                .setExceptionListener(new ResultData.OnExceptionListener() {
                    @Override
                    public void onCatch(String error, Exception e) {
                        Toast.makeText(UserInfoActivity.this, error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .GetUri();
        photoFactory.FromCrop(uri)
                .AddAspectX(1)
                .AddAspectY(1)
                .StartForResult(new PhotoFactory.OnResultListener() {
                    @Override
                    public void onCancel() {
                        Log.e(TAG, "取消裁剪");
                    }

                    @Override
                    public void onSuccess(ResultData data) {
                        bm=data.GetBitmap();
                        dealCropPhoto(data.addScaleCompress(164, 164).GetBitmap());
                    }

                    @Override
                    public void onError(String error) {
                        switch (error) {
                            case ERROR_CROP_DATA:
                                Toast.makeText(UserInfoActivity.this, "data为空", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
    private void dealCropPhoto(Bitmap bitmap) {
        user_info_image_iv.setImageBitmap(bitmap);
    }

}



