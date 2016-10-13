package ch.hsr.gadgeothek.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;

public class MainActivity extends AppCompatActivity implements GadgetListCallback {


    private boolean failed = false;
    private List<Loan> loans;
    private List<Reservation> reservations;
    private List<Gadget> gadgets;
    private Snackbar snackbar;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the toolbar to the same level as the action bar
        // looks ugly otherwise
        getSupportActionBar().setElevation(0f);

        // Setup Tabs
        TabLayout tabs = (TabLayout) findViewById(R.id.main_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshMenu:
                loadData();
                return true;
            case R.id.logoutMenu:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() {
        failed = false;
        loadingDialog = ProgressDialog.show(this, "Loading Gadgets", "Wait a sec...");
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                loans = input;
                if (reservations != null && gadgets != null){
                    loadingDialog.hide();
                }
            }

            @Override
            public void onError(String message) {
                handleDataLoadingError(message);
            }


        });
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                reservations = input;
                if (loans != null && gadgets != null){
                    loadingDialog.hide();
                }
            }

            @Override
            public void onError(String message) {
                handleDataLoadingError(message);
            }
        });
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                gadgets = input;
                if (reservations != null && loans != null){
                    loadingDialog.hide();
                }
            }

            @Override
            public void onError(String message) {
                handleDataLoadingError(message);
            }
        });
        // TODO: Update all Tabs - when done!
    }

    private void handleDataLoadingError(String message) {
        Log.d("LOG", message);
        if(failed) {
            return;
        }
        failed = true;
        loadingDialog.hide();
        snackbar = Snackbar
                .make(findViewById(R.id.activity_main), "Failed to load data..", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        loadData();
                    }
                });

        snackbar.show();
    }

    @Override
    public void onGadgetClicked(Gadget gadget) {
        // TODO: Distinguish between phone and tablet
        Log.d("LOG", "You clicked on: " + gadget.getName());
        Intent fragmentIntent = new Intent(this, GadgetDetailActivity.class);
        Reservation reservation = null;
        Loan load = null;
        fragmentIntent.putExtra(Constant.GADGET, gadget);
        fragmentIntent.putExtra(Constant.RESERVATION, reservation);
        fragmentIntent.putExtra(Constant.LOAN, load);

        startActivity(fragmentIntent);
    }

    public static class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter (FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: Pass List of Gadgets to fragment
            return GadgetListFragment.getInstance(Constant.pageTitles[position], new ArrayList<Gadget>());
        }

        @Override
        public int getCount() {
            return Constant.pageTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            // TODO: Localize!
            return Constant.pageTitles[position];
        }
    }

}
