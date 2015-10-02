package org.lemanowicz.swipeapplication2;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends FragmentActivity implements MyFragment.OnFragmentInteractionListener {

    MyPageAdapter pageAdapter;
    private String[] urls;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        urls = getResources().getStringArray(R.array.url_list);
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, urls);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(MyFragment.newInstance("Fragment 1"));
        fList.add(MyFragment.newInstance("Fragment 2"));

        return fList;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
