package Ottu.util;

import android.os.SystemClock;
import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener{

    private static final long SINGLE_CLICK_INTERVAL = 450L;

    private long lastClickTime = 0L;

    public abstract void onSingleClick();

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClickTime < SINGLE_CLICK_INTERVAL) {
            return;
        }

        lastClickTime = SystemClock.elapsedRealtime();
        onSingleClick();
    }
}
