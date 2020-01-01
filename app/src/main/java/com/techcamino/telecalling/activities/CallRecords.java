package com.techcamino.telecalling.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.PaginationAdapter;
import com.techcamino.telecalling.adapter.RecordingAdapter;
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

public class CallRecords extends AppCompatActivity {

    private APIInterFace apiService;
    private ProgressDialog dialog;
    private Context context = this;
    private HashMap<String,String> queryData;

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = -1;
    private int currentPage = PAGE_START;

    private int resultSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_records);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        //getInquiryList(queryData);


        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        queryData = new HashMap<>();
        adapter = new PaginationAdapter(this);

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
    protected void onStart() {
        super.onStart();
        //queryData = (HashMap<String, String>) getIntent().getSerializableExtra(Constants.INQUIRY_DETAIL);
        //init service and load data
        queryData.put("limit","10");
        queryData.put("offset",String.valueOf(resultSize));
        loadFirstPage();
    }

    private void loadFirstPage() {
        callRecordLogApi().enqueue(new Callback<ArrayList<RecordDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<RecordDetails>> call, Response<ArrayList<RecordDetails>> response) {
                // Got data. Send it to adapter

                ArrayList<RecordDetails> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);
                resultSize = results.size();

                adapter.addLoadingFooter();

                /*if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;*/
            }

            @Override
            public void onFailure(Call<ArrayList<RecordDetails>> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }

    /**
     * @param response extracts List<{@link >} from response
     * @return
     */
    private ArrayList<RecordDetails> fetchResults(Response<ArrayList<RecordDetails>> response) {
        ArrayList<RecordDetails> topRatedMovies = response.body();
        return topRatedMovies;
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callRecordLogApi().enqueue(new Callback<ArrayList<RecordDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<RecordDetails>> call, Response<ArrayList<RecordDetails>> response) {
                adapter.removeLoadingFooter();
                isLoading = false;
                if(response.isSuccessful()) {
                    ArrayList<RecordDetails> results = fetchResults(response);
                    adapter.addAll(results);
                    resultSize += results.size();

                    adapter.addLoadingFooter();

                    /*if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;*/
                }else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ArrayList<RecordDetails>> call, Throwable t) {
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
    private Call<ArrayList<RecordDetails>> callRecordLogApi() {
        return apiService.getRecordLog(queryData);
    }


}