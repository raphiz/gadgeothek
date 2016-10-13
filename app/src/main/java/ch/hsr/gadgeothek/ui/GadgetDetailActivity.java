package ch.hsr.gadgeothek.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.ui.fragment.GadgetDetailFragment;

public class GadgetDetailActivity extends AppCompatActivity implements GadgetDetailCallback{

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();
        Gadget gadget = (Gadget) getIntent().getSerializableExtra(Constant.GADGET);
        Reservation reservation = (Reservation) getIntent().getSerializableExtra(Constant.RESERVATION);
        Loan loan = (Loan) getIntent().getSerializableExtra(Constant.LOAN);

        Fragment detailFragment = GadgetDetailFragment.newInstance(gadget, loan, reservation);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.gadgetDetailFragment_container, detailFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle back arrow click
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReserveButtonClicked(Gadget gadget) {
        // TODO: Implement
    }

    @Override
    public void onReserveDeleteButtonClicked(Gadget gadget) {
        // TODO: Implement
    }
}
