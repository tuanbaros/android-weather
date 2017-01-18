package tuannt.appweather.models;

import tuannt.appweather.utils.SettingInterface;

/**
 * Created by tuan on 23/02/2016.
 */
public class Setting implements SettingInterface{

    private SettingInterface settingInterface;

    public Setting(SettingInterface settingInterface) {
        this.settingInterface = settingInterface;
    }


    @Override
    public void changeBackgoundColor(int position) {
        settingInterface.changeBackgoundColor(position);
    }

    @Override
    public void changePageTransformer(int position) {
        settingInterface.changePageTransformer(position);
    }
}
