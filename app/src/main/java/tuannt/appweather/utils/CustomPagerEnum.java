package tuannt.appweather.utils;

import tuannt.appweather.R;

/**
 * Created by tuan on 23/02/2016.
 */
public enum CustomPagerEnum {

    LOCATION(R.string.red, R.layout.fragment_slide_page0),
    INFO(R.string.blue, R.layout.fragment_slide_page1),
    NEXTDAY(R.string.orange, R.layout.fragment_slide_page2),
    WVIDEO(R.string.green, R.layout.fragment_slide_page3);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
