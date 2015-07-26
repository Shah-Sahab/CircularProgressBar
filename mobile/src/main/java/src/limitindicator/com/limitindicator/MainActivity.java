package src.limitindicator.com.limitindicator;

import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import src.limitindicator.com.limitindicatorlibrary.LimitIndicator;


public class MainActivity extends ActionBarActivity {

    LimitIndicator mLimitIndicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLimitIndicator = (LimitIndicator) findViewById(R.id.limitIndicator);
        mLimitIndicator.setAnimationType(LimitIndicator.ANIMATION_TYPE.INCREASE_WIDTH);
        mLimitIndicator.startProgress();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.animation_type_normal) {
//            mLimitIndicator.reset();
            mLimitIndicator.mReset = true;
            mLimitIndicator.setAnimationType(LimitIndicator.ANIMATION_TYPE.NORMAL);
            float radiusInDps = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
//            mLimitIndicator.setOuterCircleRadius(radiusInDps);
            mLimitIndicator.startProgress();
        } else if (id == R.id.animation_type_increase_width) {
//            mLimitIndicator.reset();
            mLimitIndicator.mReset = true;
            mLimitIndicator.setAnimationType(LimitIndicator.ANIMATION_TYPE.INCREASE_WIDTH);
            mLimitIndicator.startProgress();

        } else if (id == R.id.animation_type_butt_corner) {

            // TODO:
            mLimitIndicator.setIndicatorCap(Paint.Cap.BUTT);
            mLimitIndicator.invalidate();

        } else if (id == R.id.animation_type_round_corner) {

            // TODO:
            mLimitIndicator.setIndicatorCap(Paint.Cap.ROUND);
            mLimitIndicator.invalidate();

        } else if (id == R.id.animation_type_square_corner) {

            // TODO:
            mLimitIndicator.setIndicatorCap(Paint.Cap.SQUARE);
            mLimitIndicator.invalidate();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * custom:borderColor="#e3e"
     custom:borderRadius="15dp"
     custom:outerCircleRadius="105dp"
     custom:text="Test Test"
     custom:textColor="#ffffff"
     custom:textSize="16sp"
     custom:numerator="75"
     custom:innerCircleColor="#000000"
     */
}
