package tuannt.appweather.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tuannt.appweather.R;
import tuannt.appweather.utils.MyMethods;

public class SettingActivty extends AppCompatActivity {

    private ImageView ivAutoLocation, ivNotifyStatus;

    private TextView tvBackground, tvTransformer;

    private int positionBackground = 0;

    private int positionTransformer = 0;

    private boolean canSendStatus = true;

    private boolean canAutoLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);
        positionBackground = sharedPreferences.getInt("background_position", 0);
        positionTransformer = sharedPreferences.getInt("transformer_position", 0);

        linkIDResource();

        initView();


    }

    private void linkIDResource(){
        ivAutoLocation = (ImageView)findViewById(R.id.ivAutoLocation);
        ivNotifyStatus = (ImageView)findViewById(R.id.ivNotifyStatus);
        tvBackground = (TextView)findViewById(R.id.tvBackground);
        tvTransformer = (TextView)findViewById(R.id.tvTransformer);
    }

    private void initView(){
        MyMethods.setMyBackground(positionBackground, (CoordinatorLayout)findViewById(R.id.coordinator));
        MyMethods.setMyBackground(positionBackground, (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        initImageViewNotifyStatus();
        initImageViewAutoLocation();
        initTextViewBackground();
        initTextViewTransformer();
    }

    private void initTextViewBackground(){
        switch (positionBackground){
            case 0:
                tvBackground.setText("Blue");
                break;
            case 1:
                tvBackground.setText("Pink");
                break;
            case 2:
                tvBackground.setText("Green");
                break;
        }
    }

    private void initTextViewTransformer(){
        switch (positionTransformer){
            case 0:
                tvTransformer.setText("None");
                break;
            case 1:
                tvTransformer.setText("Depth");
                break;
            case 2:
                tvTransformer.setText("Zoom Out");
                break;
        }
    }

    private void initImageViewNotifyStatus(){
        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);
        canSendStatus = sharedPreferences.getBoolean("canSendStatus", true);
        if(canSendStatus==true){
            ivNotifyStatus.setImageResource(R.drawable.switch_on);
            ivNotifyStatus.setTag(R.drawable.switch_on);
        }else{
            ivNotifyStatus.setImageResource(R.drawable.switch_off);
            ivNotifyStatus.setTag(R.drawable.switch_off);
        }
    }

    private void initImageViewAutoLocation(){
        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);
        canAutoLocation = sharedPreferences.getBoolean("canAutoLocation", true);
        if(canAutoLocation==true){
            ivAutoLocation.setImageResource(R.drawable.switch_on);
            ivAutoLocation.setTag(R.drawable.switch_on);
        }else{
            ivAutoLocation.setImageResource(R.drawable.switch_off);
            ivAutoLocation.setTag(R.drawable.switch_off);
        }
    }

    public void changeBackground(View view){
        PopupMenu popup = new PopupMenu(this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu_background, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.one:
                        tvBackground.setText("Blue");
                        positionBackground = 0;
                        break;
                    case R.id.two:
                        tvBackground.setText("Pink");
                        positionBackground = 1;
                        break;
                    case R.id.three:
                        tvBackground.setText("Green");
                        positionBackground = 2;
                        break;
                }
                MyMethods.setMyBackground(positionBackground, (CoordinatorLayout)findViewById(R.id.coordinator));
                MyMethods.setMyBackground(positionBackground, (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar));
                return true;
            }
        });


        popup.show();

        if (popup.getDragToOpenListener() instanceof ListPopupWindow.ForwardingListener)
        {
            ListPopupWindow.ForwardingListener listener = (ListPopupWindow.ForwardingListener) popup.getDragToOpenListener();
            listener.getPopup().setVerticalOffset(-view.getHeight());
            listener.getPopup().show();
        }
    }

    public void changeTransformer(View view){
        PopupMenu popup = new PopupMenu(this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu_transformer, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.one:
                        tvTransformer.setText("None");
                        positionTransformer = 0;
                        break;
                    case R.id.two:
                        tvTransformer.setText("Depth");
                        positionTransformer = 1;
                        break;
                    case R.id.three:
                        tvTransformer.setText("Zoom Out");
                        positionTransformer = 2;
                        break;
                }
                return true;
            }
        });


        popup.show();

        if (popup.getDragToOpenListener() instanceof ListPopupWindow.ForwardingListener)
        {
            ListPopupWindow.ForwardingListener listener = (ListPopupWindow.ForwardingListener) popup.getDragToOpenListener();
            listener.getPopup().setVerticalOffset(-view.getHeight());
            listener.getPopup().show();
        }
    }

    public void autoLocation(View view){
        if((int)ivAutoLocation.getTag() == R.drawable.switch_on){
            ivAutoLocation.setImageResource(R.drawable.switch_off);
            ivAutoLocation.setTag(R.drawable.switch_off);
            canAutoLocation = false;
        }else{
            ivAutoLocation.setImageResource(R.drawable.switch_on);
            ivAutoLocation.setTag(R.drawable.switch_on);
            canAutoLocation = true;
        }
    }

    public void notifyStatus(View view){
        if((int)ivNotifyStatus.getTag() == R.drawable.switch_on){
            ivNotifyStatus.setImageResource(R.drawable.switch_off);
            ivNotifyStatus.setTag(R.drawable.switch_off);
            canSendStatus = false;
        }else{
            ivNotifyStatus.setImageResource(R.drawable.switch_on);
            ivNotifyStatus.setTag(R.drawable.switch_on);
            canSendStatus = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt("background_position" , positionBackground);
        prefsEditor.putInt("transformer_position", positionTransformer);
        prefsEditor.putBoolean("canSendStatus", canSendStatus);
        prefsEditor.putBoolean("canAutoLocation", canAutoLocation);
        prefsEditor.commit();
    }
}
