package tuannt.appweather.activities;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import tuannt.appweather.R;
import tuannt.appweather.tasks.GetCityWeather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;

public class FirstActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location);

        data = getData();

        ImageView ivBack = (ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.actvSearch);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(autoCompleteTextView, InputMethodManager.RESULT_SHOWN);

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

    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

    private String[]  getData(){
        DBAdapter dbAdapter = MyMethods.openDB(this, "IDCity");
        int count = dbAdapter.getAllRow().getCount();
        Cursor c = dbAdapter.getAllRow();
        String s[] = new String[count];
        for (int i = 0; i< count; i++){
            c.moveToPosition(i);
            s[i] = c.getString(1);
        }
        MyMethods.closeDB(dbAdapter);
        return s;
    }
}
