package com.example.appdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.appdemo.utils.Constants.ADD_OVERLAY_INTENT_KEY;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_TAG;

public class AddOverlayActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAG";

    private int mDrawableId, mOverlayImageWidth, mOverlayImageHeight, mScreenOrientation;
    private ImageView mOverlayImage;
    private TextView mOverlayActionAddPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mScreenOrientation = getResources().getConfiguration().orientation;
        if (mScreenOrientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_add_overlay_portrait);

        if (mScreenOrientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_add_overlay_landscape);

        mOverlayImage = findViewById(R.id.overlay_image);
        mOverlayActionAddPoint = findViewById(R.id.overlay_action_add_point);
        mOverlayActionAddPoint.setOnClickListener(this);

            getIncomingIntent();
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra(ADD_OVERLAY_INTENT_KEY)) {
            mDrawableId = getIntent().getIntExtra(ADD_OVERLAY_INTENT_KEY, 0);
            mOverlayImage.setImageResource(mDrawableId);
        }

        detectScreenDimens();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.overlay_action_add_point:
                OverlayDialog dialog = new OverlayDialog();
                Bundle b = new Bundle();
                b.putInt(OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH, mOverlayImageWidth);
                b.putInt(OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT, mOverlayImageHeight);
                dialog.setArguments(b);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, OVERLAY_DIALOG_TAG);
                break;
        }
    }


    private void detectScreenDimens() {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Log.d(TAG, "detectScreenDimens: width of screen: " + width);
        Log.d(TAG, "detectScreenDimens: height of screen: " + height);

        ViewTreeObserver viewTreeObserver = mOverlayImage.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mOverlayImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mOverlayImageWidth = mOverlayImage.getWidth();
                    mOverlayImageHeight = mOverlayImage.getHeight();
                    Log.d(TAG, "onGlobalLayout: viewWidth: " + mOverlayImageWidth);
                    Log.d(TAG, "onGlobalLayout: viewHeight" + mOverlayImageHeight);
                }
            });
        }
    }
}
