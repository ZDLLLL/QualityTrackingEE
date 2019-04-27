package zjc.qualitytrackingee.activity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.fragment.HomeFragment;
import zjc.qualitytrackingee.fragment.MineFragment;
import zjc.qualitytrackingee.utils.MoreWindow;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    LinearLayout idContainer;
    MoreWindow mMoreWindow;
    @BindView(R.id.tab_home_ib)ImageButton tab_home_ib;
    @BindView(R.id.tab_mine_ib)ImageButton tab_mine_ib;

    @BindView(R.id.tab_home_ll) LinearLayout tab_home_ll;
    @BindView(R.id.tab_mine_ll) LinearLayout tab_mine_ll;
    @BindView(R.id.lin_tab_menu) LinearLayout lin_tab_menu;
    @BindView(R.id.tab_home_tv) TextView tab_home_tv;

    @BindView(R.id.tab_mine_tv) TextView tab_mine_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.addDestoryActivity(MainActivity.this,"MainActivity");
        ButterKnife.bind(this);
        JPushInterface.setAlias(this,1, MyApplication.getE_phone());
        inListener();
        setSelect(0);

        //状态栏透明
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        idContainer=findViewById(R.id.id_container);
        mMoreWindow = new MoreWindow(this);
        mMoreWindow.init(idContainer);
    }
    public void inListener(){

        tab_home_ll.setOnClickListener(this);
        tab_mine_ll.setOnClickListener(this);
        lin_tab_menu.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        resetImgs();
      switch (view.getId()){
          case R.id.tab_home_ll:
              tab_home_ib.setImageResource(R.drawable.home_press);
              startShakeByPropertyAnim(tab_home_ib, 0.9f, 1.2f, 10f, 400);
              tab_home_tv.setTextColor(Color.parseColor("#FF4081"));
              setSelect(0);
              break;
          case R.id.tab_mine_ll:
              tab_mine_ib.setImageResource(R.drawable.mine_press);
              startShakeByPropertyAnim(tab_mine_ib,0.9f, 1.2f, 10f, 400);
              tab_mine_tv.setTextColor(Color.parseColor("#FF4081"));
              setSelect(1);
              break;
          case R.id.lin_tab_menu:
              showMoreWindow();
              break;
          default:break;
      }
    }
    private void setSelect(int i) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tf=fm.beginTransaction();
        hideFragment(tf);
        switch (i){
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    tf.add(R.id.main_fl, homeFragment);
                } else {
                    tf.show(homeFragment);
                }

                break;
            case 1:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    tf.add(R.id.main_fl, mineFragment);
                } else {
                    tf.show(mineFragment);

                }

                break;
        }
        tf.commit();
    }

    private void hideFragment(FragmentTransaction tf){
        if(homeFragment!=null){
            tf.hide(homeFragment);
        }
        if (mineFragment!=null){
            tf.hide(mineFragment);
        }
    }
    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
    private void resetImgs() {
        //重置icon图标
        tab_home_ib.setImageResource(R.drawable.home);

        tab_mine_ib.setImageResource(R.drawable.mine);

        //重置文字颜色
        tab_home_tv.setTextColor(Color.parseColor("#272727"));

        tab_mine_tv.setTextColor(Color.parseColor("#272727"));
    }
    private void showMoreWindow() {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init(idContainer);
        }

        mMoreWindow.showMoreWindow(idContainer );
    }
}
