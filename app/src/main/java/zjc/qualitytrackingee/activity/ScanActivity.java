package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.R;

public class ScanActivity extends AppCompatActivity {
     @BindView(R.id.scan_ib) ImageButton scan_ib;
     @BindView(R.id.scan_back_ll)
    LinearLayout scan_back_ll;
    private int REQUEST_CODE_SCAN = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
      //  getSupportActionBar().hide();
        ButterKnife.bind(this);

    }
    @OnClick(R.id.scan_back_ll)
    public void scan_back_ll_Onclick(){
        finish();
    }
    @OnClick(R.id.scan_ib)
    public void  scan_ib_Onclick(){
        Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                Intent intent=new Intent(ScanActivity.this,AnalysisActivity.class);
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                intent.putExtra("content",content);
                startActivity(intent);
                finish();
                Toast.makeText(ScanActivity.this,"扫描结果为：" + content,Toast.LENGTH_LONG).show();
                //result.setText("扫描结果为：" + content);
            }
        }
    }
}
