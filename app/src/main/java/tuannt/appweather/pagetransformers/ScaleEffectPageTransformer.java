package tuannt.appweather.pagetransformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by tuan on 26/02/2016.
 */
public class ScaleEffectPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        final float normalizedposition = Math.abs(Math.abs(position) - 1);
        page.setScaleX(normalizedposition / 2 + 0.5f);
        page.setScaleY(normalizedposition / 2 + 0.5f);
    }
}
