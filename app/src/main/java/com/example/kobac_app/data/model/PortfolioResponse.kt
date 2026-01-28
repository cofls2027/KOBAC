package com.example.kobac_app.data.model

import com.google.gson.annotations.SerializedName

data class Bank(
    @SerializedName("bankName")
    val bankName: String,
    
    @SerializedName("accountNum")
    val accountNum: String,
    
    @SerializedName("prodName")
    val prodName: String,
    
    @SerializedName("accountType")
    val accountType: String,
    
    @SerializedName("balanceAmt")
    val balanceAmt: String,
    
    @SerializedName("lastTranDate")
    val lastTranDate: String
)

data class BankIrp(
    @SerializedName("bankName")
    val bankName: String,
    
    @SerializedName("accountNum")
    val accountNum: String,
    
    @SerializedName("prodName")
    val prodName: String,
    
    @SerializedName("accountType")
    val accountType: String,
    
    @SerializedName("balanceAmt")
    val balanceAmt: String,
    
    @SerializedName("lastTranDate")
    val lastTranDate: String
)

data class Invest(
    @SerializedName("companyName")
    val companyName: String,
    
    @SerializedName("accountNum")
    val accountNum: String,
    
    @SerializedName("accountName")
    val accountName: String,
    
    @SerializedName("totalEvalAmt")
    val totalEvalAmt: String,
    
    @SerializedName("withdrawableAmt")
    val withdrawableAmt: String
)

data class InvestIrp(
    @SerializedName("companyName")
    val companyName: String,
    
    @SerializedName("accountNum")
    val accountNum: String,
    
    @SerializedName("accountName")
    val accountName: String,
    
    @SerializedName("totalEvalAmt")
    val totalEvalAmt: String,
    
    @SerializedName("withdrawableAmt")
    val withdrawableAmt: String
)

data class Card(
    @SerializedName("cardCompanyName")
    val cardCompanyName: String,
    
    @SerializedName("cardName")
    val cardName: String,
    
    @SerializedName("cardNum")
    val cardNum: String,
    
    @SerializedName("cardType")
    val cardType: String?,
    
    @SerializedName("paymentAmt")
    val paymentAmt: String,
    
    @SerializedName("issueDate")
    val issueDate: String?
)

data class Insu(
    @SerializedName("companyName")
    val companyName: String,
    
    @SerializedName("prodName")
    val prodName: String,
    
    @SerializedName("insuStatus")
    val insuStatus: String?,
    
    @SerializedName("paidAmt")
    val paidAmt: String,
    
    @SerializedName("expDate")
    val expDate: String?
)

data class PortfolioResponse(
    @SerializedName("bankList")
    val bankList: List<Bank>,
    
    @SerializedName("bankIrpList")
    val bankIrpList: List<BankIrp>,
    
    @SerializedName("investList")
    val investList: List<Invest>,
    
    @SerializedName("investIrpList")
    val investIrpList: List<InvestIrp>,
    
    @SerializedName("cardList")
    val cardList: List<Card>,
    
    @SerializedName("insuList")
    val insuList: List<Insu>,
    
    @SerializedName("cryptoList")
    val cryptoList: List<Any>,
    
    @SerializedName("totalNetWorthKrw")
    val totalNetWorthKrw: Long,
    
    @SerializedName("totalNetWorthUsd")
    val totalNetWorthUsd: Long
)
