package com.techcamino.telecalling.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.FollowupAdapter;
import com.techcamino.telecalling.adapter.InquiryAdapter;
import com.techcamino.telecalling.adapter.InterestAdapter;
import com.techcamino.telecalling.adapter.PaginationAdapter;
import com.techcamino.telecalling.details.FollowupDetails;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.details.RecordDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.PaginationScrollListener;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryList extends AppCompatActivity implements InquiryAdapter.CustomDialogListener {

    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private ArrayList<InquiryDetails> inquiryDetailsArrayList;
    private RecyclerView inquiryList;
    private TextView noData;

    private HashMap<String,String> queryData;

    private static final String TAG = "MainActivity";

    InquiryAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private int resultSize=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.inquiry_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        rv = (RecyclerView) findViewById(R.id.inquiry_list);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        queryData = new HashMap<>();
        adapter = new InquiryAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                queryData.put("offset",String.valueOf(resultSize));

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inquiry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_add:
                Intent i = new Intent(InquiryList.this, FilterActivity.class);
                i.putExtra(Constants.INQUIRY_DETAIL,true);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        queryData = (HashMap<String, String>) getIntent().getSerializableExtra(Constants.INQUIRY_DETAIL);
        //init service and load data
        queryData.put("limit","10");
        queryData.put("offset",String.valueOf(resultSize));
        loadFirstPage();
    }

    private void loadFirstPage() {
        callRecordLogApi().enqueue(new Callback<ArrayList<InquiryDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<InquiryDetails>> call, Response<ArrayList<InquiryDetails>> response) {
                // Got data. Send it to adapter

                if(response.isSuccessful()) {
                    ArrayList<InquiryDetails> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    resultSize = results.size();

                    adapter.addLoadingFooter();
                /*if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;*/
                }else {
                    isLastPage = true;
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InquiryDetails>> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }

    /**
     * @param response extracts List<{@link >} from response
     * @return
     */
    private ArrayList<InquiryDetails> fetchResults(Response<ArrayList<InquiryDetails>> response) {
        ArrayList<InquiryDetails> topRatedMovies = response.body();
        return topRatedMovies;
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callRecordLogApi().enqueue(new Callback<ArrayList<InquiryDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<InquiryDetails>> call, Response<ArrayList<InquiryDetails>> response) {
                adapter.removeLoadingFooter();
                isLoading = false;
                if(response.isSuccessful()) {
                    ArrayList<InquiryDetails> results = fetchResults(response);
                    adapter.addAll(results);
                    resultSize += results.size();

                    adapter.addLoadingFooter();
                    /*if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;*/
                }else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ArrayList<InquiryDetails>> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<ArrayList<InquiryDetails>> callRecordLogApi() {
        return apiService.getInquiryList(queryData);
    }


    @Override
    public void onImageClicked(InquiryDetails inquiry) {
        Intent remark = new Intent(InquiryList.this, FollowRemark
                .class);
        remark.putExtra(Constants.INQUIRY_DETAIL,inquiry);
        startActivity(remark);
    }

    @Override
    public void onItemClicked(InquiryDetails inquiry) {
        Intent intent = new Intent(InquiryList.this, ViewInquiry
                .class);
        intent.putExtra(Constants.INQUIRY_DETAIL, inquiry);
        intent.putExtra(Constants.USER_LOGIN_ID, queryData.get(Constants.USER_LOGIN_ID));
        startActivity(intent);
    }

}
