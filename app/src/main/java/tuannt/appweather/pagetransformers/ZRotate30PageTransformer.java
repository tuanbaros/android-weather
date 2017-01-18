package tuannt.appweather.pagetransformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by tuan on 26/02/2016.
 */
public class ZRotate30PageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setRotationY(position * -30);
    }
}
