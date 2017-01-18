package tuannt.simplepermission;

/**
 * Created by tuannt on 18/01/2017.
 * Project: AppWeather
 * Package: tuannt.appweather.permission
 */
interface PermissionCallback {
    void onRequestPermissionGranted(String[] permission, int[] grantResults);

    void onRequestPermissionDenied(String[] permission, int[] grantResults);
}
