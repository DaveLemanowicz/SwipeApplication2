package org.lemanowicz.swipeapplication2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


interface URLListManager{
    public void deleteSelectedURL();
    public void addAndSelectURL(String s);
}

public class SwipeActivity extends FragmentActivity
        implements MyFragment.OnFragmentInteractionListener,  URLListManager {

    MyPageAdapter pageAdapter;
    private Spinner spinner;

    static public class DeleteURLConfirmation extends DialogFragment {

        URLListManager the_callback;

        public DeleteURLConfirmation(){}

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            the_callback = (URLListManager)activity;
        }


            @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.delete_url_confirm)
                    .setPositiveButton(R.string.delete_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            the_callback.deleteSelectedURL();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    static public class AddURLDialog extends DialogFragment {

        private URLListManager the_callback;
        private EditText edit_text = null;

        public AddURLDialog(){}

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            the_callback = (URLListManager)activity;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.new_url, null);
            edit_text = (EditText) view.findViewById(R.id.new_url);
            builder.setView(view);
            builder.setMessage(R.string.delete_url_confirm)
                    .setPositiveButton(R.string.add_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            the_callback.addAndSelectURL(edit_text.getText().toString());
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    protected void updateSpinner(){

        List<String> def_urls = Arrays.asList(getResources().getStringArray(R.array.url_list));
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Set<String> urls = prefs.getStringSet("URLs", new TreeSet<String>(def_urls));

        urls.add(getResources().getString(R.string.url_not_selected));

        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> url_list = new ArrayList<String>(urls);

        Collections.sort(url_list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, url_list);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        updateSpinner();
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

    public void onPlusButton(View v){
        DialogFragment d = new AddURLDialog();
        d.show(getSupportFragmentManager(), "Enter Name");
    }
    public void onMinusButton(View v){
        DialogFragment d = new DeleteURLConfirmation();
        d.show(getSupportFragmentManager(), "Enter Name");
    }

    public void deleteSelectedURL(){
        String selected_item = spinner.getSelectedItem().toString();
        String not_selected_text = getResources().getString(R.string.url_not_selected);
        if (selected_item.equals(not_selected_text)){
            return;
        }
        SpinnerAdapter adapter = spinner.getAdapter();
        long count = adapter.getCount();

        Set<String> new_list = new TreeSet<String>();

        for (int i=0; i<count; i++){
            String this_item = adapter.getItem(i).toString();
            if (this_item.equals(not_selected_text)){
                continue;
            }
            if (this_item.equals(selected_item)){
                continue;
            }
            new_list.add(this_item);
        }

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("URLs",new_list);
        editor.commit();

        updateSpinner();

    }
    public void addAndSelectURL(String url){
        String not_selected_text = getResources().getString(R.string.url_not_selected);
        if (url.equals(not_selected_text)){
            return;
        }
        SpinnerAdapter adapter = spinner.getAdapter();
        long count = adapter.getCount();

        Set<String> new_list = new TreeSet<String>();

        for (int i=0; i<count; i++){
            String this_item = adapter.getItem(i).toString();
            if (this_item.equals(not_selected_text)){
                continue;
            }
            new_list.add(this_item);
        }
        new_list.add(url);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("URLs",new_list);
        editor.commit();

        updateSpinner();

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
