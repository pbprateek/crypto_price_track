package com.prateek.crypto.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prateek.crypto.R;
import com.prateek.crypto.models.Coin;
import com.prateek.crypto.models.CoinListResponse;
import com.prateek.crypto.network.NetworkClient;
import com.prateek.crypto.network.NetworkService;
import com.prateek.crypto.prefrences.UserPrefrenceManager;
import com.prateek.crypto.utilities.CoinListSingleton;
import com.prateek.crypto.utilities.MyFilter;

import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoListChildFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private CoinAdapter mAdapter;
    private NetworkService retrofit;
    private ArrayList<Coin> coinList;
    private FrameLayout root;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CryptoListChildFragment(){

    }

    public static CryptoListChildFragment newInstance() {
         return new CryptoListChildFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crypto_list_child, container, false);
        mRecyclerView = view.findViewById(R.id.coin_list);
        coinList=new ArrayList<Coin>();
        setList();
        mProgressBar = view.findViewById(R.id.progress_bar_coins);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh);
        root = view.findViewById(R.id.main_view);
        toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getContext()!= null) {
            if(mAdapter!=null)
                mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        retrofit = NetworkClient.getClient().create(NetworkService.class);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if(CoinListSingleton.getInstance().getList().size()==0){
            networkCall();
        }
        else {
            coinList=  CoinListSingleton.getInstance().getList();
            setList();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate( R.menu.search_bar, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void networkCall() {
        Call<CoinListResponse> call = retrofit.getAllCoinList();
        call.enqueue(new Callback<CoinListResponse>() {
            @Override
            public void onResponse(Call<CoinListResponse> call, Response<CoinListResponse> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                CoinListResponse coinListResponse = response.body();
                coinList = coinListResponse.getCoinsList();
                setList();
            }

            @Override
            public void onFailure(Call<CoinListResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(root, "Network Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RELOAD", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                networkCall();
                            }
                        });
                snackbar.show();

            }
        });


    }

    private void setList() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new CoinAdapter(coinList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class CoinHolder extends RecyclerView.ViewHolder {
        private TextView rankText;
        private TextView coinText;
        private TextView marketText;
        private TextView volumeText;
        private TextView change1Text;
        private TextView change24Text;
        private TextView change7Text;
        private ToggleButton button;
        private TextView priceText;
        private Coin mCoin;
        private Set<String> set;

        public CoinHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_coin, parent, false));
            button = itemView.findViewById(R.id.myToggleButton);
            rankText = itemView.findViewById(R.id.text_view);
            coinText=itemView.findViewById(R.id.coinName);
            marketText = itemView.findViewById(R.id.marketCap);
            volumeText=itemView.findViewById(R.id.volume24);
            change1Text = itemView.findViewById(R.id.change1);
            change7Text=itemView.findViewById(R.id.change7);
            change24Text = itemView.findViewById(R.id.change24);
            priceText=itemView.findViewById(R.id.coinPrice);
        }

        public void bind(Coin coin, int pos) {
            set = UserPrefrenceManager.getFavourite(getContext());
            mCoin = coin;
            rankText.setText(coin.getRank());
            coinText.setText(mCoin.getName());
            marketText.setText("MarketCap: $ "+mCoin.getMarketCapUsd());
            volumeText.setText("Volume 24h: $ "+mCoin.get24hVolumeUsd());
            change1Text.setText("1h "+mCoin.getPercentChange1h());
            change24Text.setText("24h "+mCoin.getPercentChange24h());
            change7Text.setText("7d "+mCoin.getPercentChange7d());
            priceText.setText("Price: $ "+mCoin.getPriceUsd());
            button.setTag(pos);
            if (set.contains(mCoin.getId())) {
                button.setChecked(true);
            } else
                button.setChecked(false);

        }
    }

    public class CoinAdapter extends RecyclerView.Adapter<CoinHolder> implements Filterable {
        private Set<String> set;
        public ArrayList<Coin> mCoins;
        private MyFilter filter;

        public CoinAdapter(ArrayList<Coin> coins) {
            this.mCoins=coins;
            set = UserPrefrenceManager.getFavourite(getContext());
        }

        private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                set = UserPrefrenceManager.getFavourite(getContext());
                int pos = (int) buttonView.getTag();

                if (set.contains(mCoins.get(pos).getId())) {
                    UserPrefrenceManager.removeFavourite(getContext(), mCoins.get(pos).getId());
                    notifyItemChanged(pos);

                } else {
                    UserPrefrenceManager.setFavourite(getContext(), mCoins.get(pos).getId());
                    notifyItemChanged(pos);

                }
            }
        };

        @NonNull
        @Override
        public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CoinHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CoinHolder holder, int position) {
            Coin coin = mCoins.get(position);
            holder.button.setOnCheckedChangeListener(null);
            holder.bind(coin, position);
            holder.button.setOnCheckedChangeListener(checkedListener);
        }

        @Override
        public int getItemCount() {
            return mCoins.size();
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new MyFilter(this, mCoins);
            }
            return filter;
        }
    }
}
