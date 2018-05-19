package com.prateek.crypto.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coin {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("price_usd")
    @Expose
    private Object priceUsd;
    @SerializedName("price_btc")
    @Expose
    private Object priceBtc;
    @SerializedName("24h_volume_usd")
    @Expose
    private Object _24hVolumeUsd;
    @SerializedName("market_cap_usd")
    @Expose
    private Object marketCapUsd;
    @SerializedName("available_supply")
    @Expose
    private String availableSupply;
    @SerializedName("total_supply")
    @Expose
    private String totalSupply;
    @SerializedName("max_supply")
    @Expose
    private Object maxSupply;
    @SerializedName("percent_change_1h")
    @Expose
    private Object percentChange1h;
    @SerializedName("percent_change_24h")
    @Expose
    private Object percentChange24h;
    @SerializedName("percent_change_7d")
    @Expose
    private Object percentChange7d;
    @SerializedName("last_updated")
    @Expose
    private Object lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Object getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(Object priceUsd) {
        this.priceUsd = priceUsd;
    }

    public Object getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(Object priceBtc) {
        this.priceBtc = priceBtc;
    }

    public Object get24hVolumeUsd() {
        return _24hVolumeUsd;
    }

    public void set24hVolumeUsd(Object _24hVolumeUsd) {
        this._24hVolumeUsd = _24hVolumeUsd;
    }

    public Object getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(Object marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Object getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(Object maxSupply) {
        this.maxSupply = maxSupply;
    }

    public Object getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(Object percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public Object getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(Object percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public Object getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(Object percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public Object getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Object lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
