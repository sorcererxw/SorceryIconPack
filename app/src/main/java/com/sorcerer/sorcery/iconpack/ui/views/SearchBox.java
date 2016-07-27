package com.sorcerer.sorcery.iconpack.ui.views;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.MenuRes;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.ps.MaterialMenuDrawable;
import com.balysv.materialmenu.ps.MaterialMenuView;
import com.quinny898.library.persistentsearch.SearchResult;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import io.codetailps.animation.ReverseInterpolator;
import io.codetailps.animation.SupportAnimator;
import io.codetailps.animation.ViewAnimationUtils;

/**
 * Created by Sorcerer on 2016/5/4 12:14.
 */
public class SearchBox extends RelativeLayout {

    public static final int VOICE_RECOGNITION_CODE = 1234;

    private MaterialMenuView mMaterialMenu;
    private TextView mLogoTextView;
    private EditText mSearchEditText;
    private Context mContext;
    private ListView mResultsListView;
    private ArrayList<SearchResult> mResultList;
    private ArrayList<SearchResult> mSearchableList;
    private boolean mSearchOpen;
    private boolean mAnimate;
    private View tint;
    private boolean mIsMic;
    private ImageView mMic;
    private ImageView mOverflow;
    private PopupMenu mPopupMenu;
    private ImageView mDrawerLogo;
    private SearchListener mListener;
    private MenuListener mMenuListener;
    private FrameLayout mRootLayout;
    private String mLogoText;
    private ProgressBar mProgressBar;
    private ArrayList<SearchResult> mInitialResults;
    private boolean mSearchWithoutSuggestions = true;
    private boolean mAnimateDrawerLogo = true;

    private boolean mIsVoiceRecognitionIntentSupported;
    private VoiceRecognitionListener mVoiceRecognitionListener;
    private Activity mContainerActivity;
    private Fragment mContainerFragment;
    private android.support.v4.app.Fragment mContainerSupportFragment;
    private SearchFilter mSearchFilter;
    private ArrayAdapter<? extends SearchResult> mAdapter;


    /**
     * Create a new searchbox
     *
     * @param context Context
     */
    public SearchBox(Context context) {
        this(context, null);
    }

    /**
     * Create a searchbox with params
     *
     * @param context Context
     * @param attrs   Attributes
     */
    public SearchBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Create a searchbox with params and a style
     *
     * @param context  Context
     * @param attrs    Attributes
     * @param defStyle Style
     */
    public SearchBox(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.searchbox, this);
        mSearchOpen = false;
        mIsMic = true;
        mMaterialMenu = (MaterialMenuView) findViewById(R.id.material_menu_button);
        mLogoTextView = (TextView) findViewById(R.id.logo);
        mSearchEditText = (EditText) findViewById(R.id.search);
        if (Build.VERSION.SDK_INT < 21) {
            mLogoTextView.setPadding(0, DisplayUtil.dip2px(getContext(), 8), 0, 0);
            mSearchEditText.setPadding(0, DisplayUtil.dip2px(getContext(), 8), 0, 0);
        }
        mResultsListView = (ListView) findViewById(R.id.results);
        mContext = context;
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mMic = (ImageView) findViewById(R.id.mic);
        mOverflow = (ImageView) findViewById(R.id.overflow);
        mDrawerLogo = (ImageView) findViewById(R.id.drawer_logo);
        mMaterialMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchOpen) {

                    toggleSearch();
                } else {
                    if (mMenuListener != null) {
                        mMenuListener.onMenuClick();
                    }
                }
            }

        });
        mResultList = new ArrayList<>();
        setAdapter(new SearchAdapter(context, mResultList, mSearchEditText));
        mAnimate = true;
        mIsVoiceRecognitionIntentSupported =
                isIntentAvailable(context, new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        mLogoTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleSearch();
            }

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RelativeLayout searchRoot = (RelativeLayout) findViewById(R.id.search_root);
            LayoutTransition lt = new LayoutTransition();
            lt.setDuration(100);
            searchRoot.setLayoutTransition(lt);
        }
        mSearchableList = new ArrayList<>();

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    search(getSearchText());
                    mSearchEditText.setSelection(mSearchEditText.getSelectionStart());
                    return true;
                }
                return false;
            }
        });
        mSearchEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    if (TextUtils.isEmpty(getSearchText())) {
//                        toggleSearch();
//                    } else {
//                        search(getSearchText());
//                    }
                    mSearchEditText.setSelection(mSearchEditText.getSelectionStart());
                    return true;
                }
                return false;
            }
        });

        mLogoText = "";
        micStateChanged();
        mMic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceRecognitionListener != null) {
                    mVoiceRecognitionListener.onClick();
                } else {
                    micClick();
                }
            }
        });

        mOverflow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupMenu.show();
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    micStateChanged(false);
                    mMic.setImageDrawable(
                            ResourceUtil.getDrawable(getContext(), R.drawable.ic_clear));
                    updateResults();
                } else {
                    micStateChanged(true);
                    mMic.setImageDrawable(
                            ResourceUtil.getDrawable(getContext(), R.drawable.ic_action_mic));
                    if (mInitialResults != null) {
                        setInitialResults();
                    } else {
                        updateResults();
                    }
                }

                if (mListener != null) {
                    mListener.onSearchTermChanged(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }
        });
        // Default search Algorithm
        mSearchFilter = new SearchFilter() {
            @Override
            public boolean onFilter(SearchResult searchResult, String searchTerm) {
                return searchResult.title.toLowerCase()
                        .startsWith(searchTerm.toLowerCase());
            }
        };
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager mgr = context.getPackageManager();
        if (mgr != null) {
            List<ResolveInfo> list =
                    mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }

    /***
     * Reveal the searchbox from a menu item. Specify the menu item id and pass the activity so the item can be found
     *
     * @param id       View ID
     * @param activity Activity
     */
    public void revealFromMenuItem(int id, Activity activity) {
        setVisibility(View.VISIBLE);
        View menuButton = activity.findViewById(id);
        if (menuButton != null) {
            FrameLayout layout = (FrameLayout) activity.getWindow().getDecorView()
                    .findViewById(android.R.id.content);
            if (layout.findViewWithTag("searchBox") == null) {
                int[] location = new int[2];
                menuButton.getLocationInWindow(location);
                revealFrom((float) location[0], (float) location[1],
                        activity, this);
            }
        }
    }

    /***
     * Hide the searchbox using the circle animation which centres upon the provided menu item. Can be called regardless of result list length
     *
     * @param id       ID of menu item
     * @param activity Activity
     */
    public void hideCircularlyToMenuItem(int id, Activity activity) {
        View menuButton = activity.findViewById(id);
        if (menuButton != null) {
            FrameLayout layout = (FrameLayout) activity.getWindow().getDecorView()
                    .findViewById(android.R.id.content);
            if (layout.findViewWithTag("searchBox") == null) {
                int[] location = new int[2];
                menuButton.getLocationInWindow(location);
                hideCircularly(location[0] + menuButton.getWidth() * 2 / 3, location[1],
                        activity);
            }
        }
    }

    /***
     * Hide the searchbox using the circle animation. Can be called regardless of result list length
     *
     * @param activity Activity
     */
    public void hideCircularly(int x, int y, Activity activity) {
        final FrameLayout layout = (FrameLayout) activity.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.search_root);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                r.getDisplayMetrics());
        int finalRadius = (int) Math.max(layout.getWidth() * 1.5, px);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                root, x, y, 0, finalRadius);
        animator.setInterpolator(new ReverseInterpolator());
        animator.setDuration(500);
        animator.start();
        animator.addListener(new SupportAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }

        });
    }

    /***
     * Toggle the searchbox's open/closed state manually
     */
    public void toggleSearch() {
        if (mSearchOpen) {
            if (TextUtils.isEmpty(getSearchText())) {
                setLogoTextInt(mLogoText);
            }
            closeSearch();
        } else {
            openSearch(true);
        }
    }


    public boolean getSearchOpen() {
        return getVisibility() == VISIBLE;
    }

    /***
     * Hide the search results manually
     */
    public void hideResults() {
        mSearchEditText.setVisibility(View.GONE);
        mResultsListView.setVisibility(View.GONE);
    }

    /***
     * Start the voice input activity manually
     */
    public void startVoiceRecognition() {
        if (isMicEnabled()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    mContext.getString(R.string.speak_now));
            if (mContainerActivity != null) {
                mContainerActivity.startActivityForResult(intent, VOICE_RECOGNITION_CODE);
            } else if (mContainerFragment != null) {
                mContainerFragment.startActivityForResult(intent, VOICE_RECOGNITION_CODE);
            } else if (mContainerSupportFragment != null) {
                mContainerSupportFragment.startActivityForResult(intent, VOICE_RECOGNITION_CODE);
            }
        }
    }

    /***
     * Enable voice recognition for Activity
     *
     * @param context Context
     */
    public void enableVoiceRecognition(Activity context) {
        mContainerActivity = context;
        micStateChanged();
    }

    /***
     * Enable voice recognition for Fragment
     *
     * @param context Fragment
     */
    public void enableVoiceRecognition(Fragment context) {
        mContainerFragment = context;
        micStateChanged();
    }

    /***
     * Enable voice recognition for Support Fragment
     *
     * @param context Fragment
     */
    public void enableVoiceRecognition(android.support.v4.app.Fragment context) {
        mContainerSupportFragment = context;
        micStateChanged();
    }

    private boolean isMicEnabled() {
        return mIsVoiceRecognitionIntentSupported
                && (mContainerActivity != null
                || mContainerSupportFragment != null
                || mContainerFragment != null);
    }

    private void micStateChanged() {
        mMic.setVisibility((!mIsMic || isMicEnabled()) ? VISIBLE : INVISIBLE);
    }

    private void micStateChanged(boolean isMic) {
        mIsMic = isMic;
        micStateChanged();
    }

    public void setOverflowMenu(@MenuRes int overflowMenuResId) {
        mOverflow.setVisibility(VISIBLE);
        mPopupMenu = new PopupMenu(mContext, mOverflow);
        mPopupMenu.getMenuInflater().inflate(overflowMenuResId, mPopupMenu.getMenu());
    }

    public void setOverflowMenuItemClickListener(
            PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        mPopupMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    /***
     * Set whether to show the progress bar spinner
     *
     * @param show Whether to show
     */

    public void showLoading(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
            mMic.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mMic.setVisibility(View.VISIBLE);
        }
    }

    /***
     * Mandatory method for the onClick event
     */
    public void micClick() {
        if (!mIsMic) {
            setSearchString("");
        } else {
            startVoiceRecognition();
        }

    }

    /***
     * Populate the searchbox with words, in an arraylist. Used by the voice input
     *
     * @param match Matches
     */
    public void populateEditText(String match) {
        toggleSearch();
        String text = match.trim();
        setSearchString(text);
        search(text);
    }

    /***
     * Force an update of the results
     */
    public void updateResults() {
        mResultList.clear();
        int count = 0;
        for (int x = 0; x < mSearchableList.size(); x++) {
            SearchResult searchable = mSearchableList.get(x);

            if (mSearchFilter.onFilter(searchable, getSearchText()) && count < 5) {
                addResult(searchable);
                count++;
            }
        }
        if (mResultList.size() == 0) {
            mResultsListView.setVisibility(View.GONE);
        } else {
            mResultsListView.setVisibility(View.VISIBLE);
        }

    }

    /***
     * Set the results that are shown (up to 5) when the searchbox is opened with no text
     *
     * @param results Results
     */
    public void setInitialResults(ArrayList<SearchResult> results) {
        mInitialResults = results;
    }

    /***
     * Set whether the menu button should be shown. Particularly useful for apps that adapt to screen sizes
     *
     * @param visibility Whether to show
     */

    public void setMenuVisibility(int visibility) {
        mMaterialMenu.setVisibility(visibility);
    }

    /***
     * Set the menu listener
     *
     * @param menuListener MenuListener
     */
    public void setMenuListener(MenuListener menuListener) {
        mMenuListener = menuListener;
    }

    /***
     * Set the search listener
     *
     * @param listener SearchListener
     */
    public void setSearchListener(SearchListener listener) {
        mListener = listener;
    }

    /***
     * Set whether to search without suggestions being available (default is true). Disable if your app only works with provided options
     *
     * @param state Whether to show
     */
    public void setSearchWithoutSuggestions(boolean state) {
        mSearchWithoutSuggestions = state;
    }

    /***
     * Set the maximum length of the searchbox's edittext
     *
     * @param length Length
     */
    public void setMaxLength(int length) {
        mSearchEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                length)});
    }

    /***
     * Set the text of the logo (default text when closed)
     *
     * @param text Text
     */
    public void setLogoText(String text) {
        mLogoText = text;
        setLogoTextInt(text);
    }


    /***
     * Set the text color of the logo
     *
     * @param color
     */
    public void setLogoTextColor(int color) {
        mLogoTextView.setTextColor(color);
    }

    /***
     * Set the image drawable of the drawer icon logo (do not set if you have not hidden the menu icon)
     *
     * @param icon Icon
     */
    public void setDrawerLogo(Drawable icon) {
        mDrawerLogo.setImageDrawable(icon);
    }

    public void setDrawerLogo(Integer icon) {
        setDrawerLogo(getResources().getDrawable(icon));
    }

    /***
     * Set the SearchFilter used to filter out results based on the current search term
     *
     * @param filter SearchFilter
     */
    public void setSearchFilter(SearchFilter filter) {
        this.mSearchFilter = filter;
    }

    /***
     * Sets the hint for the Search Field
     *
     * @param hint The hint for Search Field
     */
    public void setHint(String hint) {
        this.mSearchEditText.setHint(hint);
    }

    /***
     * Get result list
     *
     * @return Results
     */
    public ArrayList<SearchResult> getResultsListView() {
        return mResultList;
    }

    /***
     * Get the searchbox's current text
     *
     * @return Text
     */
    public String getSearchText() {
        return mSearchEditText.getText().toString();
    }

    /***
     * Set the adapter for the search results
     *
     * @param adapter Adapter
     */
    public void setAdapter(ArrayAdapter<? extends SearchResult> adapter) {
        mAdapter = adapter;
        mResultsListView.setAdapter(adapter);
    }

    /***
     * Set the searchbox's current text manually
     *
     * @param text Text
     */
    public void setSearchString(String text) {
        mSearchEditText.setText("");
        mSearchEditText.append(text);
    }

    /***
     * Add a result
     *
     * @param result SearchResult
     */
    private void addResult(SearchResult result) {
        if (mResultList != null) {
            mResultList.add(result);
            mAdapter.notifyDataSetChanged();
        }
    }

    /***
     * Clear all the results
     */
    public void clearResults() {
        if (mResultList != null) {
            mResultList.clear();
            mAdapter.notifyDataSetChanged();
        }
        mListener.onSearchCleared();
    }

    /***
     * Return the number of results that are currently shown
     *
     * @return Number of Results
     */
    public int getNumberOfResults() {
        if (mResultList != null) {
            return mResultList.size();
        }
        return 0;
    }

    /***
     * Set the searchable items from a list (replaces any current items)
     */
    public void setSearchableList(ArrayList<SearchResult> searchableList) {
        mSearchableList = searchableList;
    }

    /***
     * Add a searchable item
     *
     * @param searchable SearchResult
     */
    public void addSearchable(SearchResult searchable) {
        if (!mSearchableList.contains(searchable)) {
            mSearchableList.add(searchable);
        }
    }

    /***
     * Add all searchable items
     *
     * @param searchable SearchResult
     */
    public void addAllSearchables(ArrayList<? extends SearchResult> searchable) {
        mSearchableList.addAll(searchable);
    }

    /***
     * Remove a searchable item
     *
     * @param searchable SearchResult
     */
    public void removeSearchable(SearchResult searchable) {
        if (mSearchableList.contains(searchable)) {
            mSearchableList.remove(mSearchEditText);
        }
    }

    /***
     * Clear all searchable items
     */
    public void clearSearchable() {
        mSearchableList.clear();
    }

    /***
     * Get all searchable items
     *
     * @return ArrayList of SearchResults
     */
    public ArrayList<SearchResult> getSearchableList() {
        return mSearchableList;
    }

    private void revealFrom(float x, float y, Activity a, SearchBox s) {
        FrameLayout layout = (FrameLayout) a.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        RelativeLayout root = (RelativeLayout) s.findViewById(R.id.search_root);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                r.getDisplayMetrics());

        int finalRadius = (int) Math.max(layout.getWidth(), px);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                root, (int) x, (int) y, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.addListener(new SupportAnimator.AnimatorListener() {

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationEnd() {
                toggleSearch();
            }

            @Override
            public void onAnimationRepeat() {

            }

            @Override
            public void onAnimationStart() {

            }

        });
        animator.start();
    }

    private void search(SearchResult result, boolean resultClicked) {
        if (!mSearchWithoutSuggestions && getNumberOfResults() == 0) {
            return;
        }
        setSearchString(result.title);
        if (!TextUtils.isEmpty(getSearchText())) {
            setLogoTextInt(result.title);
            if (mListener != null) {
                if (resultClicked) {
                    mListener.onResultClick(result);
                } else {
                    mListener.onSearch(result.title);
                }
            }
        } else {
            setLogoTextInt(mLogoText);
        }
        toggleSearch();
    }

    /***
     * Set to false to retain the logo from setDrawerLogo() instead of animating to the arrow during searches.
     *
     * @param show Should the SearchBox animate the drawer logo
     */
    public void setAnimateDrawerLogo(boolean show) {
        mAnimateDrawerLogo = show;
    }

    private void openSearch(Boolean openKeyboard) {
        if (mAnimateDrawerLogo) {
            mMaterialMenu.animateState(MaterialMenuDrawable.IconState.ARROW);
            mDrawerLogo.setVisibility(View.GONE);
        }
        mLogoTextView.setVisibility(View.GONE);
        mSearchEditText.setVisibility(View.VISIBLE);
        mSearchEditText.requestFocus();
        mResultsListView.setVisibility(View.VISIBLE);
        mAnimate = true;
        setAdapter(new SearchAdapter(mContext, mResultList, mSearchEditText));
        mSearchOpen = true;
        mResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                SearchResult result = mResultList.get(arg2);
                search(result, true);
            }

        });
        if (mInitialResults != null) {
            setInitialResults();
        } else {
            updateResults();
        }

        if (mListener != null) {
            mListener.onSearchOpened();
        }
        if (getSearchText().length() > 0) {
            micStateChanged(false);
            mMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_clear));
        }
        if (openKeyboard) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void setInitialResults() {
        mResultList.clear();
        int count = 0;
        for (int x = 0; x < mInitialResults.size(); x++) {
            if (count < 5) {
                addResult(mInitialResults.get(x));
                count++;
            }
        }
        if (mResultList.size() == 0) {
            mResultsListView.setVisibility(View.GONE);
        } else {
            mResultsListView.setVisibility(View.VISIBLE);
        }
    }


    public void closeSearch() {
        if (mAnimateDrawerLogo) {
            mMaterialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
            mDrawerLogo.setVisibility(View.VISIBLE);
        }
        mLogoTextView.setVisibility(View.VISIBLE);
        mSearchEditText.setVisibility(View.GONE);
        mResultsListView.setVisibility(View.GONE);
        if (tint != null && mRootLayout != null) {
            mRootLayout.removeView(tint);
        }
        if (mListener != null) {
            mListener.onSearchClosed();
        }
        micStateChanged(true);
        mMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_action_mic));
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        setSearchString("");
        mSearchOpen = false;
    }

    private void setLogoTextInt(String text) {
        mLogoTextView.setText(text);
    }

    private void search(String text) {
        SearchResult option = new SearchResult(text, null);
        search(option, false);

    }

    public static class SearchAdapter extends ArrayAdapter<SearchResult> {
        private boolean mAnimate;
        private EditText mSearch;

        public SearchAdapter(Context context, ArrayList<SearchResult> options, EditText search) {
            super(context, 0, options);
            mSearch = search;
        }

        int count = 0;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchResult option = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.search_option, parent, false);

                if (mAnimate) {
                    Animation anim = AnimationUtils.loadAnimation(getContext(),
                            R.anim.anim_down);
                    anim.setDuration(400);
                    convertView.startAnimation(anim);
                    if (count == this.getCount()) {
                        mAnimate = false;
                    }
                    count++;
                }
            }

            View border = convertView.findViewById(R.id.border);
            if (position == 0) {
                border.setVisibility(View.VISIBLE);
            } else {
                border.setVisibility(View.GONE);
            }
            final TextView title = (TextView) convertView
                    .findViewById(R.id.title);
            title.setText(option.title);
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            icon.setImageDrawable(option.icon);
            ImageView up = (ImageView) convertView.findViewById(R.id.up);
            up.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mSearch.setText(title.getText().toString());
                    mSearch.setSelection(mSearch.getText().length());
                }

            });

            return convertView;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.KEYCODE_BACK && getVisibility() == View.VISIBLE) {
            toggleSearch();
            return true;
        }

        return super.dispatchKeyEvent(e);
    }


    public interface SearchListener {
        /**
         * Called when the searchbox is opened
         */
        void onSearchOpened();

        /**
         * Called when the clear button is pressed
         */
        void onSearchCleared();

        /**
         * Called when the searchbox is closed
         */
        void onSearchClosed();

        /**
         * Called when the searchbox's edittext changes
         */
        void onSearchTermChanged(String term);

        /**
         * Called when a search happens, with a result
         *
         * @param result
         */
        void onSearch(String result);

        /**
         * Called when a search result is clicked, with the result
         *
         * @param result
         */
        void onResultClick(SearchResult result);
    }

    public interface MenuListener {
        /**
         * Called when the menu button is pressed
         */
        void onMenuClick();
    }

    public interface VoiceRecognitionListener {
        /**
         * Called when the menu button is pressed
         */
        void onClick();
    }

    public interface SearchFilter {
        /**
         * Called against each Searchable to determine if it should be filtered out of the results
         */
        boolean onFilter(SearchResult searchResult, String searchTerm);
    }

    public void setLogoTypeface(Typeface tf) {
        mLogoTextView.setTypeface(tf);
    }

}
