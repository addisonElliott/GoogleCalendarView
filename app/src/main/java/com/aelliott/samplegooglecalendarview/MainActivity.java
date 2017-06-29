package com.aelliott.samplegooglecalendarview;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aelliott.googlecalendarview.appbar.AppBarLayout;
import com.aelliott.googlecalendarview.calendar.CalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    final static int TOOLBAR_ONE = 1;
    final static int TOOLBAR_TWO = 2;

    static class AppBarLayoutHolder
    {
        @BindView(R.id.appBarLayout)
        AppBarLayout appBarLayout;
        @BindView(R.id.toolbar)
        Toolbar toolbar;
        ActionBar actionBar;
        @BindView(R.id.calendarView)
        CalendarView calendarView;

        public AppBarLayoutHolder(View view)
        {
            ButterKnife.bind(this, view);

            //calendarView.setMonthViewLayout(R.layout.month_view);
            //calendarView.setMonthViewHeaderLayout(R.layout.month_view_item_header);
        }

        @OnClick(R.id.relativeLayout_datePickerButton)
        public void datePickerButton_onClick(View v)
        {
            int state = appBarLayout.getState();
            switch (state)
            {
                case AppBarLayout.STATE_EXPANDED:
                    appBarLayout.setExpanded(false, true);
                    break;

                case AppBarLayout.STATE_COLLAPSED:
                    appBarLayout.setExpanded(true, true);
                    break;

                default:
                    // If transitioning to a new state, do nothing
                    return;
            }
        }
    }

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    AppBarLayoutHolder appBarLayoutHolder = null;
    int toolbarView = TOOLBAR_ONE;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all variables annotated with @BindView and other variants
        ButterKnife.bind(this);

        // Sets up toolbar
        setToolbarView(toolbarView);
    }

    public void setToolbarView(int toolbarView)
    {
        this.toolbarView = toolbarView;

        View view = null;
        switch (toolbarView)
        {
            case TOOLBAR_ONE:
                view = getLayoutInflater().inflate(R.layout.toolbar_one, coordinatorLayout, false);
                break;

            case TOOLBAR_TWO:
                view = getLayoutInflater().inflate(R.layout.toolbar_two, coordinatorLayout, false);
                break;

            default:
                Log.w(TAG, "Invalid toolbar view number given");
                break;
        }

        if (view != null)
        {
            if (appBarLayoutHolder != null)
                coordinatorLayout.removeView(appBarLayoutHolder.appBarLayout);
            coordinatorLayout.addView(view, 0);

            appBarLayoutHolder = new AppBarLayoutHolder(view);

            setSupportActionBar(appBarLayoutHolder.toolbar);
            appBarLayoutHolder.actionBar = getSupportActionBar();
        }
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mCoordinator.restoreState(savedInstanceState);
        if (savedInstanceState.getBoolean(STATE_TOOLBAR_TOGGLE, false))
        {
            View toggleButton = findViewById(R.id.toolbar_toggle_frame);
            if (toggleButton != null)
            {
                // can be null as disabled in landscape
                toggleButton.performClick();
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        mCoordinator.coordinate(mToolbarToggle, mCalendarView, mAgendaView);
        if (checkCalendarPermissions())
        {
            loadEvents();
        }
        else
            toggleEmptyView(true);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        switch (toolbarView)
        {
            case TOOLBAR_ONE:
                menu.findItem(R.id.action_toolbarViewOne).setChecked(true);
                break;

            case TOOLBAR_TWO:
                menu.findItem(R.id.action_toolbarViewTwo).setChecked(true);
                break;
        }

        /*switch (CalendarUtils.sWeekStart)
        {
            case Calendar.SATURDAY:
                menu.findItem(R.id.action_week_start_saturday).setChecked(true);
                break;
            case Calendar.SUNDAY:
                menu.findItem(R.id.action_week_start_sunday).setChecked(true);
                break;
            case Calendar.MONDAY:
                menu.findItem(R.id.action_week_start_monday).setChecked(true);
                break;
        }*/
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_toolbarViewOne:
                if (!item.isChecked())
                {
                    supportInvalidateOptionsMenu();
                    setToolbarView(TOOLBAR_ONE);
                }
                return true;

            case R.id.action_toolbarViewTwo:
                if (!item.isChecked())
                {
                    supportInvalidateOptionsMenu();
                    setToolbarView(TOOLBAR_TWO);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        /*if (item.getItemId() == R.id.action_today)
        {
            mCoordinator.reset();
            return true;
        }
        if (item.getItemId() == R.id.action_week_start_saturday || item.getItemId() == R.id.action_week_start_sunday || item
                .getItemId() == R.id.action_week_start_monday)
        {
            if (!item.isChecked())
            {
                changeWeekStart(item.getItemId());
            }
            return true;
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);*/
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mCoordinator.saveState(outState);
        outState.putBoolean(STATE_TOOLBAR_TOGGLE, mToolbarToggle.isChecked());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mCalendarView.deactivate();
        mAgendaView.setAdapter(null); // force detaching adapter
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(
                CalendarUtils.PREF_CALENDAR_EXCLUSIONS,
                TextUtils.join(SEPARATOR, mExcludedCalendarIds)).apply();
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(mDrawer))
            mDrawerLayout.closeDrawer(mDrawer);
        else
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_CODE_CALENDAR:
                if (checkCalendarPermissions())
                {
                    toggleEmptyView(false);
                    loadEvents();
                }
                else
                {
                    toggleEmptyView(true);
                }
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String selection = null;
        String[] selectionArgs = null;
        if (id == LOADER_LOCAL_CALENDAR)
        {
            selection = CalendarContract.Calendars.ACCOUNT_TYPE + "=?";
            selectionArgs = new String[] {String.valueOf(CalendarContract.ACCOUNT_TYPE_LOCAL)};
        }
        return new CursorLoader(this, CalendarContract.Calendars.CONTENT_URI,
                CalendarCursor.PROJECTION, selection, selectionArgs,
                CalendarContract.Calendars.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case LOADER_CALENDARS:
                if (data != null && data.moveToFirst())
                {
                    mCalendarSelectionView.swapCursor(new CalendarCursor(data),
                            mExcludedCalendarIds);
                }
                break;
            case LOADER_LOCAL_CALENDAR:
                if (data == null || data.getCount() == 0)
                {
                    createLocalCalendar();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mCalendarSelectionView.swapCursor(null, null);
    }

    private void setUpPreferences()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String exclusions = PreferenceManager.getDefaultSharedPreferences(this).getString(
                CalendarUtils.PREF_CALENDAR_EXCLUSIONS, null);
        if (!TextUtils.isEmpty(exclusions))
        {
            mExcludedCalendarIds.addAll(Arrays.asList(exclusions.split(SEPARATOR)));
        }

        CalendarUtils.sWeekStart = sp.getInt(CalendarUtils.PREF_WEEK_START, Calendar.SUNDAY);
    }

    private void setUpContentView()
    {
        /*mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mCalendarSelectionView = (CalendarSelectionView)findViewById(R.id.list_view_calendar);
        //noinspection ConstantConditions
        mCalendarSelectionView.setOnSelectionChangeListener(mCalendarSelectionListener);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer,
                R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mToolbarToggle = (CheckedTextView)findViewById(R.id.toolbar_toggle);
        View toggleButton = findViewById(R.id.toolbar_toggle_frame);
        if (toggleButton != null)
        { // can be null as disabled in landscape
            toggleButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mToolbarToggle.toggle();
                    toggleCalendarView();
                }
            });
        }
        mCalendarView = (EventCalendarView)findViewById(R.id.calendar_view);
        mAgendaView = (AgendaView)findViewById(R.id.agenda_view);
        mFabAdd = (FloatingActionButton)findViewById(R.id.fab);
        mFabAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createEvent();
            }
        });
        //noinspection ConstantConditions
        mFabAdd.hide();
    }

    private void toggleCalendarView()
    {
        if (mToolbarToggle.isChecked())
        {
            mCalendarView.setVisibility(View.VISIBLE);
        }
        else
        {
            mCalendarView.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void toggleEmptyView(boolean visible)
    {
        /*if (visible)
        {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
            findViewById(R.id.empty).bringToFront();
            findViewById(R.id.button_permission).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    requestCalendarPermissions();
                }
            });
        }
        else
        {
            findViewById(R.id.empty).setVisibility(View.GONE);
        }
    }

    private void changeWeekStart(@IdRes int selection)
    {
        switch (selection)
        {
            case R.id.action_week_start_saturday:
                CalendarUtils.sWeekStart = Calendar.SATURDAY;
                break;
            case R.id.action_week_start_sunday:
                CalendarUtils.sWeekStart = Calendar.SUNDAY;
                break;
            case R.id.action_week_start_monday:
                CalendarUtils.sWeekStart = Calendar.MONDAY;
                break;
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(
                CalendarUtils.PREF_WEEK_START, CalendarUtils.sWeekStart).apply();
        supportInvalidateOptionsMenu();
        mCoordinator.reset();
    }

    private void createEvent()
    {
        startActivity(new Intent(this, EditActivity.class));
    }

    private void loadEvents()
    {
        getSupportLoaderManager().initLoader(LOADER_CALENDARS, null, this);
        getSupportLoaderManager().initLoader(LOADER_LOCAL_CALENDAR, null, this);
        mFabAdd.show();
        mCalendarView.setCalendarAdapter(new CalendarCursorAdapter(this, mExcludedCalendarIds));
        mAgendaView.setAdapter(new AgendaCursorAdapter(this, mExcludedCalendarIds));
    }

    private void createLocalCalendar()
    {
        String name = getString(R.string.default_calendar_name);
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, BuildConfig.APPLICATION_ID);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, name);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, name);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, BuildConfig.APPLICATION_ID);
        new CalendarQueryHandler(getContentResolver()).startInsert(0, null,
                CalendarContract.Calendars.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "1")
                        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                                BuildConfig.APPLICATION_ID)
                        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                                CalendarContract.ACCOUNT_TYPE_LOCAL)
                        .build(), cv);
    }

    @VisibleForTesting
    protected boolean checkCalendarPermissions()
    {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) | ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)) == PackageManager.PERMISSION_GRANTED;
    }

    @VisibleForTesting
    protected boolean checkLocationPermissions()
    {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) | ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED;
    }

    @VisibleForTesting
    protected void requestCalendarPermissions()
    {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                REQUEST_CODE_CALENDAR);
    }

    @VisibleForTesting
    protected void requestLocationPermissions()
    {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
    }

    private void explainLocationPermissions()
    {
        Snackbar.make(mCoordinatorLayout, R.string.location_permission_required,
                Snackbar.LENGTH_LONG).setAction(R.string.grant_access, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                requestLocationPermissions();
            }
        }).show();
    }*/
}
