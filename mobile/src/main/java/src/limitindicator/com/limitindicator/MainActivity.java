package src.limitindicator.com.limitindicator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
            mLimitIndicator.clearAnimation();
            mLimitIndicator.setAnimationType(LimitIndicator.ANIMATION_TYPE.NORMAL);
            mLimitIndicator.restartProgress();
        } else if (id == R.id.animation_type_increase_width) {
            mLimitIndicator.clearAnimation();
            mLimitIndicator.setAnimationType(LimitIndicator.ANIMATION_TYPE.INCREASE_WIDTH);
            mLimitIndicator.restartProgress();
        }

        return super.onOptionsItemSelected(item);
    }
}
