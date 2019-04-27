package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.anlia.photofactory.factory.PhotoFactory;
import com.anlia.photofactory.result.ResultData;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.othershe.nicedialog.NiceDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.ImagePickerAdapter;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.Common;
import zjc.qualitytrackingee.utils.GlideImageLoader;
import zjc.qualitytrackingee.utils.SelectDialog;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static com.anlia.photofactory.factory.PhotoFactory.ERROR_CROP_DATA;
import static com.mob.tools.utils.DeviceHelper.getApplication;
import static zjc.qualitytrackingee.MyApplication.getContext;

public class AddGoodsActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{
    private EditText goods_name_et;
    private EditText goods_price_et;
    private EditText goods_class_et;
    private EditText goods_introduction_et;
    @BindView(R.id.goods_confirm_bt)
    Button goods_confirm_bt;
    private EditText goods_number_et;
    private static Bitmap bmp;
    private static Bitmap bm;
    final NiceDialog progressDialog=NiceDialog.init();
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private GoodsClassListActivity goodsClassListActivity;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;//允许选择图片最大数
    private LinearLayout addgoods_back_ll;
    public static AddGoodsActivity instance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        instance = this;
        ButterKnife.bind(this);
        initView();
        initImagePicker();
        initWidget();
    }

    public void initView(){
        goods_name_et=findViewById(R.id.goods_name_et);
        goods_price_et=findViewById(R.id.goods_price_et);
        goods_class_et=findViewById(R.id.goods_class_et);
        goods_introduction_et=findViewById(R.id.goods_introduction_et);
        goods_number_et=findViewById(R.id.goods_number_et);
        addgoods_back_ll=findViewById(R.id.addgoods_back_ll);
        addgoods_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        goods_confirm_bt=findViewById(R.id.goods_confirm_bt);
        goods_confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag=false;
                if(TextUtils.isEmpty(goods_name_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(goods_price_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(goods_class_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(goods_number_et.getText().toString())){
                    flag=true;
                }
                if(TextUtils.isEmpty(goods_introduction_et.getText().toString())){
                    flag=true;
                }
                if(flag){
                    Toast.makeText(AddGoodsActivity.this,"请确认所有信息都填了",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.setLayoutId(R.layout.loading_layout)
                            .setWidth(100)
                            .setHeight(100)
                            .setDimAmount(0.5f)
                            .setOutCancel(true).show(getSupportFragmentManager());
                    submitGoods();
                    //progressDialog.dismiss();
//            finish();
                }
            }
        });
        goods_class_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddGoodsActivity.this,GoodsClassListActivity.class);
                intent.putExtra("co_name",goods_name_et.getText().toString());
                intent.putExtra("co_price",goods_price_et.getText().toString());
                startActivity(intent);

            }
        });
        goods_name_et.setText(getIntent().getStringExtra("co_name"));
        goods_class_et.setText(getIntent().getStringExtra("co_class"));
        goods_price_et.setText(getIntent().getStringExtra("co_price"));

    }
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(false);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000); //保存文件的高度。单位像素

    }
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private void initWidget() {
        RecyclerView goods_rv =findViewById(R.id.goods_rv);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        goods_rv.setLayoutManager(new GridLayoutManager(this, 4));
        goods_rv.setHasFixedSize(true);
        goods_rv.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(AddGoodsActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(AddGoodsActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(AddGoodsActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    public void submitGoods(){
        String image=selImageList.get(0).path;
        File file=new File(image);
        if(file.exists()) {
            bmp = BitmapFactory.decodeFile(image);
            Log.v("图片的字节流",bmp.toString());
        }
        final String name=goods_name_et.getText().toString();
        final String price=goods_price_et.getText().toString();
        final String introduction=goods_introduction_et.getText().toString();
        final String coclassid=goodsClassListActivity.coc_classid;
        final String result=convertIconToString(bmp);
        final  String num=goods_number_et.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient mOkHttpClient=new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("co_img", result)
                            .add("co_price",price)
                            .add("co_name",name)
                            .add("c_id",MyApplication.getC_id())
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

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res=response.body().string();
                            try {
                                JSONObject jsonObject=new JSONObject(res);
                               // JSONObject jsonObject1=jsonObject.getJSONObject("addCommodity");
                                if("yes".equals(jsonObject.getString("addCommodity"))){
                                    Looper.prepare();
                                    Toast.makeText(AddGoodsActivity.this,"商品上传成功",Toast.LENGTH_LONG).show();

                                    Looper.loop();
                                }else {
                                    Looper.prepare();
                                    Toast.makeText(AddGoodsActivity.this,"商品上传失败",Toast.LENGTH_LONG).show();

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


    //Bitmap转Base64
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }


}
