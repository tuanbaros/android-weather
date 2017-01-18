package tuannt.appweather.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import tuannt.appweather.R;
import tuannt.appweather.fragments.FirstFragment;
import tuannt.appweather.fragments.SecondFragment;
import tuannt.appweather.fragments.ThirdFragment;
import tuannt.appweather.fragments.ZeroFragment;
import tuannt.appweather.models.Setting;
import tuannt.appweather.models.User;
import tuannt.appweather.tasks.GetCityWeather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.FlipAnimation;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.MyNotification;
import tuannt.appweather.utils.SettingInterface;
import tuannt.appweather.utils.Variables;

public class SlideActivity extends FragmentActivity implements SettingInterface{

    private static final int NUM_PAGES = 4;

    private ViewPager viewPager;

    private ImageView ivPageFirst, ivPageSecond, ivPageThird, ivPageFourth;

    private FirstFragment firstFragment;

    private SecondFragment secondFragment;

    private ZeroFragment zeroFragment;

    private ThirdFragment thirdFragment;

    private  TextView tvTime, tvLocation;

    private ImageView ivRefresh;

    private LinearLayout llSearchLocation;

    private ImageView ivBack, ivSetting;

    private AutoCompleteTextView autoCompleteTextView;

    private boolean flag = false;

    private RelativeLayout relativeLayout;

    private ImageView background;

//    int ResolveTransparentStatusBarFlag()
//    {
//        String[] libs = getPackageManager().getSystemSharedLibraryNames();
//        String reflect = null;
//
//        if (libs == null)
//            return 0;
//
//        for (String lib : libs)
//        {
//            if (lib.equals("touchwiz"))
//                reflect = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND";
//            else if (lib.startsWith("com.sonyericsson.navigationbar"))
//                reflect = "SYSTEM_UI_FLAG_TRANSPARENT";
//        }
//
//        if (reflect == null)
//            return 0;
//
//        try
//        {
//            Field field = View.class.getField(reflect);
//            if (field.getType() == Integer.TYPE)
//                return field.getInt(null);
//        }
//        catch (Exception e)
//        {
//        }
//
//        return 0;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        background = (ImageView) findViewById(R.id.background);
        setMainBackground();

//        if(android.os.Build.VERSION.SDK_INT < 19){
//            applyTransparentStatusBar();
//        }
        SharedPreferences sharedPreferences = getSharedPreferences( "tuannt" , MODE_PRIVATE);

        MyMethods.setMyBackground(sharedPreferences.getInt("background_position", 0), (RelativeLayout)findViewById(R.id.mn_activity_root));

        Variables.canRefresh = true;

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        zeroFragment = new ZeroFragment();
        thirdFragment = new ThirdFragment();


        final User user = User.getInstance();

        DBAdapter dbAdapter = MyMethods.openDB(this, "Favorite");
        if(dbAdapter.getAllRow().getCount() < 1){
            dbAdapter.insertRow(new String[]{
                    user.getWeather().getCity_id(), user.getWeather().getCity_name()});
        }else{
            if(Variables.fromNotify==false){
                Cursor c = dbAdapter.getAllRow();
                c.moveToPosition(0);
                if(c.getString(0).equals(user.getWeather().getCity_id()) == false){
                    for(int i = 1; i< c.getCount(); i++){
                        c.moveToPosition(i);
                        if(c.getString(0).equals(user.getWeather().getCity_id())){
                            dbAdapter.deletePost(c.getString(0));
                            break;
                        }
                    }
                    c.moveToPosition(0);
                    dbAdapter.updateContact(c.getString(0), new String[]{
                            user.getWeather().getCity_id(), user.getWeather().getCity_name()});
                }
            }else{
                Variables.fromNotify = false;
            }
        }
        dbAdapter.close();

        tvTime = (TextView)findViewById(R.id.tvTime);
        tvTime.setText(getDay() + ", " + getHour());

        Variables.tvCity = (TextView)findViewById(R.id.tvCity);
        Variables.tvCity.setText(MyMethods.capitalize(user.getWeather().getCity_name()));

        llSearchLocation = (LinearLayout)findViewById(R.id.llSearchLocation);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.actvSearch);

        ivRefresh = (ImageView)findViewById(R.id.ivRefresh);
        ivRefresh.setTag(R.drawable.refresh);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ivRefresh.getTag() == R.drawable.refresh) {
//                    rotation = AnimationUtils.loadAnimation(SlideActivity.this, R.anim.rotate);
//                    rotation.setFillBefore(true);
//                    ivRefresh.setAnimation(rotation);
//                    ivRefresh.startAnimation(rotation);
                    Variables.reSetLocation = true;
                    MyMethods.showUpdateDialog(SlideActivity.this);
                    new GetCityWeather(SlideActivity.this).execute(API.getApiCurrentCityById(user.getWeather()
                        .getCity_id()));
                    tvTime.setText(getDay() + ", " + getHour());
                } else {
                    if (llSearchLocation.getVisibility() == View.VISIBLE) {
                        llSearchLocation.setVisibility(View.GONE);
                    } else {
                        llSearchLocation.setVisibility(View.VISIBLE);
                        autoCompleteTextView.requestFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(autoCompleteTextView, InputMethodManager.RESULT_SHOWN);
                    }

                }
            }
        });

        ivBack = (ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSearchLocation.setVisibility(View.GONE);
                hideKeyboard();
            }
        });

        ivPageFirst = (ImageView)findViewById(R.id.ivPageFirst);
        ivPageSecond = (ImageView)findViewById(R.id.ivPageSecond);
        ivPageThird = (ImageView)findViewById(R.id.ivPageThird);
        ivPageFourth = (ImageView)findViewById(R.id.ivPageFourth);

        ivPageSecond.setImageResource(R.drawable.page_dot);

        viewPager = (ViewPager)findViewById(R.id.pager);
        Variables.pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(Variables.pagerAdapter);
        viewPager.setOffscreenPageLimit(NUM_PAGES);
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImagePageDot(position);
                setTextTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state != ViewPager.SCROLL_STATE_IDLE) {
                    final int childCount = viewPager.getChildCount();
                    for (int i = 0; i < childCount; i++)
                    viewPager.getChildAt(i).setLayerType(View.LAYER_TYPE_NONE, null);
                }
            }
        });

        tvLocation = (TextView)findViewById(R.id.tvLocation);

        final String data[] = getData();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                getIDCityJustSelect(autoCompleteTextView.getText().toString());
                autoCompleteTextView.setText("");
            }
        });

        ImageView ivDeleteAllCharacter = (ImageView)findViewById(R.id.ivDeleteAllCharacter);
        ivDeleteAllCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });

        MyNotification myNotification = new MyNotification(this);
        myNotification.showStatus();

        llSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSearchLocation.setVisibility(View.GONE);
                hideKeyboard();
            }
        });

        ivSetting = (ImageView)findViewById(R.id.ivSetting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SlideActivity.this, ivSetting);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.one){
                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            // To count with Play market backstack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }else{
                            Intent intent = new Intent(getBaseContext(), SettingActivty.class);
                            startActivity(intent);
                        }
//                        Toast.makeText(SlideActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });


                popup.show();

            }
        });

        Variables.setting = new Setting(this);

        relativeLayout = (RelativeLayout)findViewById(R.id.mn_activity_root);

    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        llSearchLocation.setVisibility(View.GONE);
        tvTime.setText(getDay() + ", " + getHour());

    }

    private void getIDCityJustSelect(String s){
        DBAdapter dbAdapter = MyMethods.openDB(this, "IDCity");
        int count = dbAdapter.getAllRow().getCount();
        Cursor c = dbAdapter.getAllRow();
        for (int i = 0; i<count; i++){
            c.moveToPosition(i);
            if (c.getString(1).equals(s)){
                String id = c.getString(0);
                String name = c.getString(1);
                MyMethods.showUpdateDialog(this);
                new GetCityWeather(this).execute(API.getApiCurrentCityById(id));
                DBAdapter db = MyMethods.openDB(this, "Favorite");
                count = db.getAllRow().getCount();
                c = db.getAllRow();
                for (i = 0; i< count; i++){
                    if(i<count-1){
                        c.moveToPosition(i);
                        if (c.getString(1).equals(s)){
                            db.close();
                            break;
                        }
                    }else{
                        c.moveToPosition(i);
                        if (c.getString(1).equals(s)){
                            db.close();
                            break;
                        }else{
                            db.insertRow(new String[]{id, name});
                            db.close();
                        }
                    }
                }
                db.close();
                break;
            }
        }
        dbAdapter.close();
    }


    @Override
    public void onBackPressed() {
        if(Variables.isFullScreen == false) {
            if (viewPager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                if (llSearchLocation.getVisibility() == View.GONE) {
                    if (!flag) {
                        Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                flag = true;
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                flag = false;
                            }
                        }).start();
                    } else {
                        super.onBackPressed();
//                    Variables.canRefresh = false;
                    }
                } else {
                    llSearchLocation.setVisibility(View.GONE);
//                flipCard();
                }
            } else {
                // Otherwise, select the previous step.
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        }else{
            thirdFragment.collapsingVideo();
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return zeroFragment;
                case 1: return firstFragment;
                case 2: return secondFragment;
                case 3: return thirdFragment;
            }
            return null;

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void setImagePageDot(int position){
        switch (position){
            case 0:
                ivPageFirst.setImageResource(R.drawable.page_dot);
                ivPageSecond.setImageResource(R.drawable.page_circle);
                ivPageThird.setImageResource(R.drawable.page_circle);
                ivPageFourth.setImageResource(R.drawable.page_circle);
                break;
            case 1:
                ivPageFirst.setImageResource(R.drawable.page_circle);
                ivPageSecond.setImageResource(R.drawable.page_dot);
                ivPageThird.setImageResource(R.drawable.page_circle);
                ivPageFourth.setImageResource(R.drawable.page_circle);
                break;
            case 2:
                ivPageFirst.setImageResource(R.drawable.page_circle);
                ivPageSecond.setImageResource(R.drawable.page_circle);
                ivPageThird.setImageResource(R.drawable.page_dot);
                ivPageFourth.setImageResource(R.drawable.page_circle);
                break;
            case 3:
                ivPageFirst.setImageResource(R.drawable.page_circle);
                ivPageSecond.setImageResource(R.drawable.page_circle);
                ivPageThird.setImageResource(R.drawable.page_circle);
                ivPageFourth.setImageResource(R.drawable.page_dot);
                break;
        }
    }

    private String getDay(){
        String time = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        getSixNextDay(day);
        switch (day){
            case Calendar.SATURDAY: time = "Th 7"; break;
            case Calendar.SUNDAY: time = "CN"; break;
            case Calendar.MONDAY: time = "Th 2"; break;
            case Calendar.TUESDAY: time = "Th 3"; break;
            case Calendar.WEDNESDAY: time = "Th 4"; break;
            case Calendar.THURSDAY: time = "Th 5"; break;
            case Calendar.FRIDAY: time = "Th 6"; break;
        }
        return time;
    }

    private String getHour(){
        String time;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String minute = "";
        if(minutes<10){
            minute = "0" + minutes;
        }else{
            minute = "" + minutes;
        }
        if(hour < 12){
            time = "" + hour + ":" + minute + " AM";
        }else{
            if(hour == 12){
                time = "" + hour + ":" + minute + " PM";
            }else{
                time = "" + (hour - 12) + ":" + minute + " PM";
            }
        }
        return time;
    }

    private void setTextTitle(int position){
        switch (position){
            case 0:
                tvTime.setVisibility(View.GONE);
                Variables.tvCity.setVisibility(View.GONE);
                tvLocation.setVisibility(View.VISIBLE);
                tvLocation.setText("Vị trí");
                ivRefresh.setImageResource(R.drawable.add);
                ivRefresh.setTag(R.drawable.add);
                ivRefresh.clearAnimation();
                break;
            case 1:
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(getDay() + ", " + getHour());
                tvLocation.setVisibility(View.GONE);
                Variables.tvCity.setVisibility(View.VISIBLE);
                ivRefresh.setImageResource(R.drawable.refresh);
                ivRefresh.setTag(R.drawable.refresh);
                break;
            case 2:
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText("5 ngày tiếp theo");
                tvLocation.setVisibility(View.GONE);
                Variables.tvCity.setVisibility(View.VISIBLE);
                ivRefresh.setImageResource(R.drawable.refresh);
                ivRefresh.setTag(R.drawable.refresh);
                break;

            case 3:
                tvTime.setVisibility(View.GONE);
                Variables.tvCity.setVisibility(View.GONE);
                tvLocation.setVisibility(View.VISIBLE);
                tvLocation.setText("Bản tin thời tiết");
                ivRefresh.setImageResource(R.drawable.refresh);
                ivRefresh.setTag(R.drawable.refresh);
                break;

        }
    }

    private void getSixNextDay(int day){
        User user = User.getInstance();
        switch (day){
            case 1:
                user.setSixNextDay(new String[]{"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"});
                break;
            case 2:
                user.setSixNextDay(new String[]{"Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"});
                break;
            case 3:
                user.setSixNextDay(new String[]{"Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật", "Thứ 2"});
                break;
            case 4:
                user.setSixNextDay(new String[]{"Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật", "Thứ 2", "Thứ 3"});
                break;
            case 5:
                user.setSixNextDay(new String[]{"Thứ 6", "Thứ 7", "Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4"});
                break;
            case 6:
                user.setSixNextDay(new String[]{"Thứ 7", "Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5"});
                break;
            case 7:
                user.setSixNextDay(new String[]{"Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6"});
                break;
        }

    }

    private String[]  getData(){
        DBAdapter dbAdapter = MyMethods.openDB(this, "IDCity");
        int count = dbAdapter.getAllRow().getCount();
        Cursor c = dbAdapter.getAllRow();
        String s[] = new String[count];
        for (int i = 0; i< count; i++){
            c.moveToPosition(i);
            s[i] = c.getString(1);
        }
        dbAdapter.close();
        return s;
    }

//    private void applyTransparentStatusBar()
//    {
//        Window window = getWindow();
//        if (window != null)
//        {
//            View decor = window.getDecorView();
//            if (decor != null)
//                decor.setSystemUiVisibility(ResolveTransparentStatusBarFlag());
//        }
//    }

    private void flipCard()
    {
        View rootLayout = (View) findViewById(R.id.mn_activity_root);
        View cardFace = (View) findViewById(R.id.front);
        View cardBack = (View) findViewById(R.id.llSearchLocation);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE)
        {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }

    @Override
    public void changeBackgoundColor(int position) {
        switch (position){
            case 0:
                relativeLayout.setBackgroundColor(Color.parseColor("#027ab6"));
                break;
            case 1:
                relativeLayout.setBackgroundColor(Color.parseColor("#da1194"));
                break;
            case 2:
                relativeLayout.setBackgroundColor(Color.parseColor("#02b611"));
                break;
        }
    }

    @Override
    public void changePageTransformer(int position) {
        MyMethods.setMyPageTransformer(position, viewPager);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Variables.canRefresh = false;
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences( "tuannt" , MODE_PRIVATE);
//        MyMethods.setMyBackground(sharedPreferences.getInt("background_position", 0), (RelativeLayout) findViewById(R.id.mn_activity_root));
        MyMethods.setMyBackground(sharedPreferences.getInt("background_position", 0), (RelativeLayout) findViewById(R.id.rlSearch));
        MyMethods.setMyPageTransformer(sharedPreferences.getInt("transformer_position", 0), viewPager);
    }

    private void setMainBackground(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour >= 18){
            background.setImageResource(R.drawable.bg_fine_night);
        }else{
            background.setImageResource(R.drawable.bg_nice_day);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

}
