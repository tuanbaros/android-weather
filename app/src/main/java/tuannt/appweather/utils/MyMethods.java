package tuannt.appweather.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import tuannt.appweather.R;
import tuannt.appweather.models.User;

/**
 * Created by tuan on 18/02/2016.
 */
public class MyMethods {

    private static ProgressDialog progressDialog=null;

    public static String capitalize(final String line) {
        String[] arrayWord = line.split(" ");
        String s = null;
        for (int i = 0; i < arrayWord.length; i++){
            if(i==0){
                s = Character.toUpperCase(arrayWord[i].charAt(0)) + arrayWord[i].substring(1);
            }else{
                s += " " + Character.toUpperCase(arrayWord[i].charAt(0)) + arrayWord[i].substring(1);
            }
        }

        return s;
    }



    public static String getCTemp(String Ftemp){
        return ("" + (Math.round(Float.parseFloat(Ftemp) - 273) + "\u2103"));
    }


    public static DBAdapter openDB(Context appCompatActivity, String tableName){
        String[] s = {"id_city", "name_city"};
        DBAdapter dbAdapter = new DBAdapter(appCompatActivity, tableName, s);
        dbAdapter.open();
        return dbAdapter;
    }

    public static void closeDB(DBAdapter dbAdapter){
        dbAdapter.close();
    }

    public static ProgressDialog showUpdateDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Đang cập nhật");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void hideUpdateDialog(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.cancel();
            Variables.pagerAdapter.notifyDataSetChanged();
        }

    }

    public static void updateCityName(){
        if(Variables.canRefresh==true){
            User user = User.getInstance();
            Variables.tvCity.setText(user.getWeather().getCity_name());
        }
    }

    public static void showKeyBoard(Activity activity, View view){
        view.requestFocus();
        InputMethodManager mgr = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
    }

    public static void setIconStatus(ImageView view, String s){
        switch(s){
            case "light rain": view.setImageResource(R.drawable.light_rain); break;
            case "moderate rain": view.setImageResource(R.drawable.moderate_rain); break;
            case "clear sky": view.setImageResource(R.drawable.weather_image); break;
            case "heavy intensity rain": view.setImageResource(R.drawable.heavy_rain); break;
        }
    }


    public static void setMyBackground(int position, View v){
        v.setBackgroundColor(Variables.colorBackground[position]);
    }

    public static void setMyPageTransformer(int postition, ViewPager viewPager){
            viewPager.setPageTransformer(true, Variables.pageTransformers[postition]);

    }
}
