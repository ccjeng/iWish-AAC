package com.ccjeng.iwish.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;

/**
 * A fragment representing a single StoryItem detail screen.
 * This fragment is either contained in a {@link StoryItemListActivity}
 * in two-pane mode (on tablets) or a {@link StoryItemDetailActivity}
 * on handsets.
 */
public class StoryItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_CONTENT = "content";
    public static final String ARG_FONTSIZE = "fontsize";

    private String mName;
    private int mFontSize;

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoryItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_CONTENT)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_CONTENT));

            mName = getArguments().getString(ARG_CONTENT);
            mFontSize = getArguments().getInt(ARG_FONTSIZE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.storyitem_detail, container, false);

        // Show the dummy content as text in a TextView.
        ((TextView) rootView.findViewById(R.id.storyitem_detail)).setText(mName);
        ((TextView) rootView.findViewById(R.id.storyitem_detail)).setTextSize(TypedValue.COMPLEX_UNIT_SP, mFontSize/2);


        return rootView;
    }
}
