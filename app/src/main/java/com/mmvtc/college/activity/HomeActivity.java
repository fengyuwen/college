package com.mmvtc.college.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmvtc.college.R;
import com.mmvtc.college.adapter.HomeAdapter;
import com.mmvtc.college.adapter.ViewPagerAdapter;
import com.mmvtc.college.bean.ItemBean;
import com.mmvtc.college.fragment.BuildingFragment;
import com.mmvtc.college.fragment.CollegeFragment;
import com.mmvtc.college.fragment.ComputersFragment;
import com.mmvtc.college.utils.Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;
import static com.mmvtc.college.utils.Local.cookie;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_left_drawer)
    ListView listLeftDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_head_icon)
    ImageView ivHeadIcon;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.ll_right)
    LinearLayout llRight;


    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private boolean isLogin = false;
    private String headIconUrl;
    private CollegeFragment collegeFragment;
    private BuildingFragment buildingFragment;
    private ComputersFragment computersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        startActivity(new Intent(this,SplashActivity.class));
        init();
    }

    private void init() {
        tvMainTitle.setText("茂名职业技术学院");

        ArrayList<ItemBean> menuLists = new ArrayList<ItemBean>();
        menuLists.add(new ItemBean(R.drawable.college, "学院简介"));
        menuLists.add(new ItemBean(R.drawable.college, "校园风光"));
        menuLists.add(new ItemBean(R.drawable.main_course_icon_selected, "成绩查询"));
        menuLists.add(new ItemBean(R.drawable.main_exercises_icon_selected, "课程表"));
        menuLists.add(new ItemBean(R.drawable.zhou, "周程表"));
        menuLists.add(new ItemBean(R.drawable.myinfo_setting_icon, "修改密码"));
        menuLists.add(new ItemBean(R.drawable.exit, "退出登录"));
        HomeAdapter myAdapter = new HomeAdapter<ItemBean>(menuLists, R.layout.item_list) {
            @Override
            public void bindView(ViewHolder holder, ItemBean obj) {
                holder.setImageResource(R.id.img_icon, obj.getIconId());
                holder.setText(R.id.txt_content, obj.getIconName());
            }
        };
        //侧边栏头部


        llHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin)
                    startActivity(new Intent(HomeActivity.this, UserInfoActivity.class));
                else startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), 0);
            }
        });

        listLeftDrawer.setAdapter(myAdapter);
        listLeftDrawer.setOnItemClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //添加页面缓存页，防止页面丢失数据
        viewPager.setOffscreenPageLimit(4);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_news:
                                viewPager.setCurrentItem(0);
                                tvMainTitle.setText("茂名职业技术学院");
                                //collegeFragment.updateData();
                                break;
                            case R.id.item_lib:
                                viewPager.setCurrentItem(1);
                                tvMainTitle.setText("计算机工程系");
                                //computersFragment.updateData();
                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                tvMainTitle.setText("土木系");
                               // buildingFragment.updateData();
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);

                switch (position) {
                    case 0:
                        tvMainTitle.setText("茂名职业技术学院");
                        //collegeFragment.updateData();
                        break;
                    case 1:
                        tvMainTitle.setText("计算机工程系");
                       // computersFragment.updateData();
                        break;
                    case 2:
                        tvMainTitle.setText("土木系");
                      //  buildingFragment.updateData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            isLogin = true;
            tvUserName.setText(Local.readUser(this));
            setLogin();
        }
    }

    private void setLogin() {
        initHeadIcon();
    }

    private void initHeadIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url2 = Local.urls.get("个人信息");
                    Document doc = Jsoup.connect(Local.url + url2)
                            .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
                            .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                            .timeout(5000)
                            .get();
                    Element body = doc.body();
                    Element headIcon = body.getElementById("xszp");
                    headIconUrl = "http://jwc.mmvtc.cn/" + headIcon.attr("src");
                    updateHeadIcon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateHeadIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(headIconUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if (!cookie.isEmpty())
                        conn.setRequestProperty("Cookie", cookie);
                    final int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivHeadIcon.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        collegeFragment = CollegeFragment.newInstance();
        computersFragment = ComputersFragment.newInstance();
        buildingFragment = BuildingFragment.newInstance();
        adapter.addFragment(collegeFragment);
        adapter.addFragment(computersFragment);
        adapter.addFragment(buildingFragment);
        viewPager.setAdapter(adapter);
    }

    //侧边栏监听
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (isLogin)
            switch (i) {
                case 0:
                    startActivity(new Intent(this, ModifyPsw.class));
                    break;
                case 1:
                    startActivity(new Intent(this, ModifyPsw.class));
                    break;
                case 2:
                    startActivity(new Intent(this, GradeActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, CourseActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(this, ModifyPsw.class));
                    break;
                case 5:
                    startActivity(new Intent(this, ModifyPsw.class));
                    break;
                case 6:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Document doc = Jsoup.connect("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                        .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
                                        .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                        .timeout(3000)
                                        .get();
                                Elements inputs = doc.getElementsByTag("input");
                                String viewState = "";
                                for (Element e : inputs) {
                                    String view = e.attr("name");
                                    if (view.equals("__VIEWSTATE")) {
                                        viewState = e.attr("value");
                                    }
                                }
                                Document doc2 = Jsoup.connect("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                        .cookie("ASP.NET_SessionId", cookie.substring(cookie.indexOf("=") + 1, cookie.length()))
                                        .data("__EVENTTARGET", "likTc")
                                        .data("__EVENTARGUMENT", "")
                                        .data("__VIEWSTATE", viewState)
                                        .referrer("http://jwc.mmvtc.cn/xs_main.aspx?xh=" + Local.number)
                                        .timeout(3000)
                                        .post();
                                Log.i(TAG, "run: " + doc2.html());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isLogin = false;
                                        ivHeadIcon.setImageResource(R.drawable.logo);
                                        tvUserName.setText("");
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }
                    }).start();
                    break;
            }
        else Toast.makeText(this, "请先登录!", Toast.LENGTH_SHORT).show();
    }

    //左上角按钮打开侧边栏
    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        drawerLayout.openDrawer(llRight);
    }
}