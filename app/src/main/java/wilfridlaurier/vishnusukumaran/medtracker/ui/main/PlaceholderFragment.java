package wilfridlaurier.vishnusukumaran.medtracker.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import wilfridlaurier.vishnusukumaran.medtracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public  class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    ImageView img;
    String [] title={"Welcome to MedTracker","Place where you put medicine reminders", "Reminders can have date, time and number of repetitions. Lets get started!"};
    int[] bgs = new int[]{R.mipmap.ic_launcher, R.drawable.ic_access_alarms_black_24dp, R.drawable.ic_vibration_black_24dp};

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_pager, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(title[ getArguments().getInt(ARG_SECTION_NUMBER)-1]);

        img = (ImageView) rootView.findViewById(R.id.imageView);
        img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);


        return rootView;
    }



}