package com.sairame.demo.quintessa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.axis.axismerchantsdk.AxisUpi;
import com.axis.axismerchantsdk.model.MerchantKeys;
import com.axis.axismerchantsdk.model.UpiTransactionParams;
import com.axis.axismerchantsdk.util.TransactionCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {


    Button btnPay;
    public static String MERCH_ID = "JPMERCHANT";
    public static String MERCH_CHAN_ID = "JPMERCHANT";
    public static String MERCHANT_KEY = "qlsUjKdMIDSHfwfwjehfbwebfjwQGRTWQ21323NJNJ0En6blp6SIlmHmvCNhoLuiAL4toSzWo238i35OXtGmmdVlfUWOHGwtv3csriX9GHjiuxqwrbKdVw75NBuxQRLBlyvkewCjephWfOtfpAj9Avob6eCt8MVN6r19jp0Be1rZGNdLhwVjRRdbmOKL1ynXXm9YLF1fLtJcuTff7gtimiNKLWca9iyq171ZkibZoGhknV5UTDDPvgrd0Qkg9mnKqVfFyE8A";
    public  final String MCC_CODE = "";
   
    public  String UNQ_TXN_ID= "1234A" + Math.random();
    public  String UNQ_CUST_ID="918754543283";
    public  final String MOBILENO="918754543283";
    public  final String EMAILID="sathoshthepro@gmail.com";
    public  String AMOUNT="2.00";
    public  final String TXN_DTL="TRAVEL";

    public  final String CURRENCY="INR";
    public  String ORDER_ID;
    public  String UDF_PARAMS = new JSONObject().toString();
    public  String MERCH_CHECKSUM="MERCH_CHECKSUM";
    HashMap<String, String> customParameters = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLibraryForPayment();
            }
        });
    }

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);
            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }


    void callLibraryForPayment(){
        AxisUpi axisUpi = new AxisUpi(MainActivity.this);
        final UpiTransactionParams upiParams = new UpiTransactionParams();
        upiParams.setMerchantId(MERCH_ID);
        upiParams.setMerchantChannelId(MERCH_CHAN_ID);
        upiParams.setMcc(MCC_CODE);
        upiParams.setMerchantRequestId(UNQ_TXN_ID);
        upiParams.setMerchantCustomerId(UNQ_CUST_ID);
        upiParams.setCustomerMobileNumber(MOBILENO);
        upiParams.setCustomerEmail(EMAILID);
        upiParams.setAmount(AMOUNT);
        upiParams.setTransactionDescription(TXN_DTL);
        upiParams.setCurrency(CURRENCY);
        upiParams.setOrderId(ORDER_ID);
        upiParams.setUdfParameters(UDF_PARAMS);

        String payload = MERCH_ID+MERCH_CHAN_ID+UNQ_TXN_ID+UNQ_CUST_ID+MCC_CODE+AMOUNT+TXN_DTL+CURRENCY+MOBILENO+EMAILID+ORDER_ID+UDF_PARAMS;
        MERCH_CHECKSUM = hmacDigest(payload, MERCHANT_KEY, "HmacSHA256");
        upiParams.setMerchantChecksum(MERCH_CHECKSUM);

        upiParams.setAllowOtherVpa("false");
        upiParams.setShowOtherPaymentOptions("true");
        upiParams.setOtherPaymentOptionType("COLLECT");

        Log.d("SDK","TRANSACTION "+upiParams);

        axisUpi.startTransaction(upiParams, new TransactionCallback() {

            public String result = "";

            @Override
            public void onTransactionComplete(HashMap<String,String> upiResponse) {
                for (String name: upiResponse.keySet()){

                    String key =name.toString();
                    String value = upiResponse.get(name).toString();
                    result += key + " :: " + value;

                }
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }

        });
    }

}
