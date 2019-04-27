package zjc.qualitytrackingee.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.anlia.photofactory.factory.PhotoFactory;
import com.anlia.photofactory.permission.PermissionAlwaysDenied;
import com.anlia.photofactory.result.ResultData;
import com.othershe.nicedialog.NiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;

import static com.anlia.photofactory.factory.PhotoFactory.ERROR_CROP_DATA;
import static zjc.qualitytrackingee.MyApplication.getContext;

public class AddNewGoodsActivity extends AppCompatActivity {
    private EditText newgoods_name_et;
    private EditText newgoods_price_et;
    private EditText newgoods_class_et;
    private EditText newgoods_introduction_et;
    private ImageView  newaddgoods_iv;
    @BindView(R.id.newgoods_confirm_bt)
    Button newgoods_confirm_bt;
    private GoodsClassListActivity goodsClassListActivity;
    private EditText newgoods_number_et;
    private LinearLayout newaddgoods_back_ll;
    final NiceDialog progressDialog=NiceDialog.init();
    public static AddNewGoodsActivity instance=null;
    private final String TAG = "AddNewGoodsActivity";
    private String picName;
    private static Bitmap bm;
    private PhotoFactory photoFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_goods);
        instance = this;
        ButterKnife.bind(this);
        photoFactory=new PhotoFactory(this);
        picName = Calendar.getInstance().getTimeInMillis() + ".png";
        initView();
    }
    public void initView(){
        newgoods_name_et=findViewById(R.id.newgoods_name_et);
        newgoods_price_et=findViewById(R.id.newgoods_price_et);
        newgoods_class_et=findViewById(R.id.newgoods_class_et);
        newgoods_introduction_et=findViewById(R.id.newgoods_introduction_et);
        newgoods_number_et=findViewById(R.id.newgoods_number_et);
        newaddgoods_back_ll=findViewById(R.id.newaddgoods_back_ll);
        newaddgoods_iv=findViewById(R.id.newaddgoods_iv);
        newgoods_confirm_bt=findViewById(R.id.newgoods_confirm_bt);
        //隐藏
        newgoods_class_et.setInputType(InputType.TYPE_NULL);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newgoods_class_et.getWindowToken(),0);
        newaddgoods_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadHeadImage();
            }
        });
        newaddgoods_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        newgoods_confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag=false;
                if(TextUtils.isEmpty(newgoods_name_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(newgoods_price_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(newgoods_class_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(newgoods_number_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(newgoods_introduction_et.getText().toString())){
                    flag=true;
                }
                if(flag){
                    Toast.makeText(AddNewGoodsActivity.this,"请确认所有信息都填了",Toast.LENGTH_LONG).show();
                }else {
//                    progressDialog.setLayoutId(R.layout.loading_layout)
//                            .setWidth(100)
//                            .setHeight(100)
//                            .setDimAmount(0.5f)
//                            .setOutCancel(true).show(getSupportFragmentManager());
                    submitGoods();
                    //progressDialog.dismiss();
                   // finish();
                }
            }
        });

        showSoftInputFromWindow(this,newgoods_class_et);
        newgoods_class_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                newgoods_class_et.setInputType(InputType.TYPE_NULL);
               // getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                Intent intent=new Intent(AddNewGoodsActivity.this,GoodsClassListActivity.class);
                intent.putExtra("co_name",newgoods_name_et.getText().toString());
                intent.putExtra("co_price",newgoods_price_et.getText().toString());
                startActivity(intent);

            }
        });
        newgoods_name_et.setText(getIntent().getStringExtra("co_name"));
        newgoods_class_et.setText(getIntent().getStringExtra("co_class"));
        newgoods_price_et.setText(getIntent().getStringExtra("co_price"));

    }
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public  String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
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
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_add_new_goods, null);
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
                photoFactory = new PhotoFactory(AddNewGoodsActivity.this, Environment.getExternalStorageDirectory() + "/" + "DCIM", picName);
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
                photoFactory = new PhotoFactory(AddNewGoodsActivity.this, Environment.getExternalStorageDirectory() + "/" + "DCIM", picName);
                photoFactory.FromGallery()
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
                        Toast.makeText(AddNewGoodsActivity.this, error, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddNewGoodsActivity.this, "data为空", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
    private void dealCropPhoto(Bitmap bitmap) {
        newaddgoods_iv.setImageBitmap(bitmap);
    }
    public void submitGoods(){
//        String image=selImageList.get(0).path;
//        File file=new File(image);
//        if(file.exists()) {
//            bmp = BitmapFactory.decodeFile(image);
//            Log.v("图片的字节流",bmp.toString());
//        }
        final String name=newgoods_name_et.getText().toString();
        final String price=newgoods_price_et.getText().toString();
        final String introduction=newgoods_introduction_et.getText().toString();
        final String coclassid=goodsClassListActivity.coc_classid;
        final String result=convertIconToString(bm);
        final  String num=newgoods_number_et.getText().toString();
        progressDialog.setLayoutId(R.layout.loading_layout)
                            .setWidth(100)
                            .setHeight(100)
                            .setDimAmount(0.5f)
                            .setOutCancel(true).show(getSupportFragmentManager());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient mOkHttpClient=new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("co_img", result)
                            .add("co_price",price)
                            .add("co_name",name)
                            .add("c_id", MyApplication.getC_id())
                            .add("co_classid",coclassid)
                            .add("co_describe",introduction)
                            .add("num",num)
                            .build();
                    Request request = new Request.Builder()
                            .url(Net.UploadGoods)
                            .post(formBody)
                            .build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("TAG", e.getMessage(), e);
                            Looper.prepare();
                            Toast.makeText(getContext(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res=response.body().string();
                            // 需要解析json格式的字符串
                            try {
                                if(res != null && res.startsWith("\ufeff"))
                                {
                                    res =  res.substring(1);
                                }
                                JSONObject jsonObject=new JSONObject(res);
                                // JSONObject jsonObject1=jsonObject.getJSONObject("addCommodity");
                                if("yes".equals(jsonObject.getString("addCommodity"))){
                                    Looper.prepare();
                                    Toast.makeText(AddNewGoodsActivity.this,"商品上传成功",Toast.LENGTH_LONG).show();
                                    finish();
                                    Looper.loop();
                                   // finish();
                                }else {
                                    Looper.prepare();
                                    Toast.makeText(AddNewGoodsActivity.this,"商品上传失败",Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
