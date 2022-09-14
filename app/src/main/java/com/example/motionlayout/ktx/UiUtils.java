package com.example.motionlayout.ktx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;

public class UiUtils {
    private static Handler sMainThreadHandler;
    private static long sLastClickTime;

    /**
     * @return a {@link Handler} tied to the main thread.
     */
    public static Handler getMainThreadHandler() {
        if (sMainThreadHandler == null) {
            // No need to synchronize -- it's okay to create an extra Handler,
            // which will be used
            // only once and then thrown away.
            sMainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return sMainThreadHandler;
    }


    /**
     * A thread safe way to show a Toast. Can be called from any thread.
     *
     * @param context application  context
     * @param resId   Resource ID of the message string.
     */
    public static void showToast(Context context, int resId) {
        if (context == null) {
            return;
        }
        showToast(context, context.getApplicationContext().getResources().getString(resId));
    }

    /**
     * A thread safe way to show a Toast. Can be called from any thread.
     *
     * @param context application context
     * @param message Message to show.
     */
    public static void showToast(final Context context, final String message) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(message)) {
            return;
        }

        getMainThreadHandler().post(() -> Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

    /**
     * check whether device is tablet or phone
     */
    public static boolean isTablet(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        return (context.getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Check App is running and can be seen.
     */
    public static boolean isAppInForeground(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;

        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    /**
     * Check android service component is running in background.
     */
    public static boolean isServiceRunning(Context context, String className) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        if (TextUtils.isEmpty(className)) {
            throw new IllegalArgumentException();
        }

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;

        List<RunningServiceInfo> serviceList = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * Check user double clicked button, if true should do nothing,
     * otherwise do things you want to do.
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - sLastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        sLastClickTime = time;
        return false;
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void dimSystemBarCan(Activity activity, boolean dim) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
            if (dim) {
                decorView.setSystemUiVisibility(uiOptions);
            } else {
                // Calling setSystemUiVisibility() with a value of 0 to clears all flags.
                decorView.setSystemUiVisibility(0);
            }
        }
    }

    /**
     * Hide both the navigation bar and the status bar.
     * <p/>
     * <p>SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
     * a general rule, you should design your app to hide the status bar whenever you
     * hide the navigation bar.
     * <p/>
     * <p>Note: If you hide the system bars in your activity's onCreate() method and the
     * user presses Home, the system bars will reappear. When the user reopens the activity,
     * onCreate() won't get called, so the system bars will remain visible. If you want system
     * UI changes to persist as the user navigates in and out of your activity, set UI flags in
     * onResume() or onWindowFocusChanged().
     *
     * @param activity         Activity instance
     * @param keepLayoutStable if true, when layout content will appear behind the navigation bar.
     */
    @TargetApi(VERSION_CODES.JELLY_BEAN)
    public static void hideNavigationBar(Activity activity, boolean hide, boolean keepLayoutStable) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            if (hide) {
                if (keepLayoutStable) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                } else {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
                }
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
    }

    /**
     * Hide both the navigation bar and the status bar but layout content can resize when switch state,
     * about detail please refer  {@link #hideNavigationBar(Activity, boolean, boolean)}
     *
     * @param activity activity instance
     * @param hide     whether hide navigation bar
     */
    public static void hideNavigationBar(Activity activity, boolean hide) {
        hideNavigationBar(activity, hide, false);
    }

    /**
     * Set the IMMERSIVE flag.<br>
     * Set the content to appear under the system bars so that the content doesn't resize when the system bars hide and show.
     *
     * @param activity  activity
     * @param stickMode if true navigation bar disappear after user interaction.
     */
    @TargetApi(VERSION_CODES.KITKAT)
    private void setImmersiveMode(Activity activity, boolean immersive, boolean stickMode) {
        if (immersive) {
            if (stickMode) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static int getStatusBarHeight(Context context) {
        final Resources resources = context.getResources();
        final int heightResId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (heightResId > 0) {
            return resources.getDimensionPixelSize(heightResId);
        } else {
            int heightFromDp = Build.VERSION.SDK_INT >= VERSION_CODES.M ? 24 : 25;
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, heightFromDp, resources.getDisplayMetrics());
        }
    }

    public static int getActionBarHeight(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        try {
            return (int) typedArray.getDimension(0, 0);
        } finally {
            typedArray.recycle();
        }
    }

    public static void setStatusBarColor(@NonNull Window window, @ColorInt final int color) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    // set statusBar background color
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(color);

                    // set statusBar content color
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                        boolean isLightBackground = ColorUtils.calculateLuminance(color) >= 0.5;
                        View decorView = window.getDecorView();
                        decorView.setSystemUiVisibility(isLightBackground ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0);
                    }
                }
            });
        }
    }

    public static void setStatusBarTextColor(@NonNull Window window, @StatusBarTextColor int color) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    View decorView = window.getDecorView();
                    int systemUiVisibility = decorView.getSystemUiVisibility();
                    // 设置状态栏背景亮色（LIGHT），系统自动完成字体反转成黑色
                    if (color == StatusBarTextColor.BLACK) {
                        systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    } else if (color == StatusBarTextColor.WHITE) {
                        systemUiVisibility = (systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    }
                    decorView.setSystemUiVisibility(systemUiVisibility);
                }
            });
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({StatusBarTextColor.WHITE,
            StatusBarTextColor.BLACK})
    public @interface StatusBarTextColor {
        int WHITE = 1;
        int BLACK = 2;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void setBackgroundResource(View view, @DrawableRes int backgroundResource) {
        // retrieve padding first
        int left = view.getPaddingLeft();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();

        view.setBackgroundResource(backgroundResource);

        // set padding again
        view.setPadding(left, top, right, bottom);
    }

    public static void shakeWarning(View shakeView) {
        if (shakeView == null) {
            throw new IllegalArgumentException();
        }

        Animation animation = new TranslateAnimation(0, 10, 0, 0);
        animation.setInterpolator(new CycleInterpolator(4));
        animation.setDuration(500);
        shakeView.startAnimation(animation);
    }

    public static GradientDrawable getBackgroundDrawable(int solidColor, int strokeColor, int strokeWidth, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    @Nullable
    public static FragmentActivity getBaseActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        } else if (context instanceof ContextWrapper) {
            return getBaseActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static Context getBaseContext(Context context) {
        if (context == null) {
            throw new RuntimeException("context can not be null");
        } else if (context instanceof Activity) {
            return context;
        } else if (context instanceof ContextWrapper) {
            return getBaseContext(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 状态栏透明化，使得布局可以延伸到状态栏底部
     *
     * @param activity
     */
    public static void translucentStatusBar(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(option);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    /**
     * 设置Activity全屏
     */
    public static void setActivityFullScreen(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(flags);
            // 参考携程代码，为何采用反射layoutInDisplayCutoutMode，原因未知
            WindowManager.LayoutParams object = window.getAttributes();
            try {
                Field field = WindowManager.LayoutParams.class.getDeclaredField("layoutInDisplayCutoutMode");
                field.setAccessible(true);
                field.set(object, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
                window.setAttributes(object);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
