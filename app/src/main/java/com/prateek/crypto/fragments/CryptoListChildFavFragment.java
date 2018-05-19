package com.prateek.crypto.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoListChildFavFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private CoinAdapter mAdapter;
    private NetworkService retrofit;
    private ArrayList<Coin> coinList;
    private ArrayList<Coin> coinListFav;
    private FrameLayout root;

    public CryptoListChildFavFragment(){

    }


    public static CryptoListChildFavFragment newInstance() {
        return new CryptoListChildFavFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_crypto_list_child_fav, container, false);
        mRecyclerView = view.findViewById(R.id.coin_list);
        coinList=new ArrayList<Coin>();
        coinListFav=new ArrayList<Coin>();
        setList();
        mProgressBar = view.findViewById(R.id.progress_bar_coins);
        root = view.findViewById(R.id.main_view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser &&  getContext()!= null) {
            if(mAdapter!=null){
               setFavList();
               mAdapter.setList(coinListFav);
            }

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofit = NetworkClient.getClient().create(NetworkService.class);
        if(CoinListSingleton.getInstance().getList().size()==0){
            networkCall();
        }
        else{
            coinList=CoinListSingleton.getInstance().getList();
            setFavList();
            mAdapter.setList(coinListFav);
        }


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
                setFavList();
                mAdapter.setList(coinListFav);
            }

            @Override
            public void onFailure(Call<CoinListResponse> call, Throwable t) {
                Log.e("HERE",call.toString()+t.toString());
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(root, "Network Error", Snackbar.LENGTH_SHORT)
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

    private void setFavList(){
        Set<String> set= UserPrefrenceManager.getFavourite(getContext());
        coinListFav=new ArrayList<>();
        for(Coin coin:coinList){
            if(set.contains(coin.getId())){
                coinListFav.add(coin);
            }
        }

    }

    private void setList() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new CoinAdapter(coinListFav);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class CoinHolder extends RecyclerView.ViewHolder {

        private ToggleButton button;
        private Coin mCoin;
        private Set<String> set;
        private TextView rankText;
        private TextView coinText;
        private TextView marketText;
        private TextView volumeText;
        private TextView change1Text;
        private TextView change24Text;
        private TextView change7Text;

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
        }

        public void bind(Coin coin, int pos) {
            button.setBackgroundResource(R.drawable.ic_baseline_delete_24px);
            set = UserPrefrenceManager.getFavourite(getContext());
            mCoin = coin;
            rankText.setText(coin.getRank());
            coinText.setText(mCoin.getName());
            marketText.setText("MarketCap: $ "+mCoin.getMarketCapUsd());
            volumeText.setText("Volume 24h: $ "+mCoin.get24hVolumeUsd());
            change1Text.setText("1h "+mCoin.getPercentChange1h());
            change24Text.setText("24h "+mCoin.getPercentChange24h());
            change7Text.setText("7d "+mCoin.getPercentChange7d());
            button.setTag(pos);
            if (set.contains(mCoin.getId())) {
                button.setChecked(true);
            } else
                button.setChecked(false);

        }
    }

    public class CoinAdapter extends RecyclerView.Adapter<CoinHolder>  {
        private Set<String> set;
        public ArrayList<Coin> mCoins;

        public CoinAdapter(ArrayList<Coin> coins) {
            mCoins = coins;
            set = UserPrefrenceManager.getFavourite(getContext());
        }

        private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                set = UserPrefrenceManager.getFavourite(getContext());

                    int pos = (int) buttonView.getTag();
                    Log.e("HERE",pos+"");
                    UserPrefrenceManager.removeFavourite(getContext(), mCoins.get(pos).getId());
                    mCoins.remove(mCoins.get(pos));
                    notifyDataSetChanged();
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

        public void setList(ArrayList<Coin> coins){
            this.mCoins=coins;
            notifyDataSetChanged();
        }
    }

}
