package org.lemanowicz.swipeapplication2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServerTestFragment.OnServerTestInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServerTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServerTestFragment extends Fragment implements GatewayTestFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";


    private OnServerTestInteractionListener mListener;
    private View mView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static ServerTestFragment newInstance(String message) {
        Log.e("Huh?","in newInstance");
        ServerTestFragment fragment = new ServerTestFragment();
        Bundle args = new Bundle(1);
        args.putString(EXTRA_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public ServerTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Huh?", "in ServerTestFragment.onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Huh?","in onCreateView. this: "+this.toString());
        String message = getArguments().getString(EXTRA_MESSAGE);
        mView = inflater.inflate(R.layout.server_test, container, false);
        TextView messageTextView = (TextView)mView.findViewById(R.id.textView);
        messageTextView.setText(message);
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onServerTestInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnServerTestInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnServerTestInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void urlSelected(String url){

        Activity a = getActivity();
        View v = getView();

        if (a==null) {
            Log.e("Huh?","activity: null");
        } else {
            Log.e("Huh?","activity: " + a.toString());
        }

        if (v==null) {
            Log.e("Huh?","view: null");
        } else {
            Log.e("Huh?","view: " + v.toString());
        }

        //if (a == null){
        //    return;
        //}

        Log.e("Huh?","in urlSelected. this: "+this.toString());

        Button test_button = (Button)getView().findViewById(R.id.test_url_button);
        if (url.startsWith("http")){
            test_button.setEnabled(true);
        } else {
            test_button.setEnabled(false);
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnServerTestInteractionListener {
        // TODO: Update argument type and name
        public void onServerTestInteraction(Uri uri);
    }



}
