package com.uav.ava.constant;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationConstant {


    public final static String AUTHKEY= "G4s4cCMx2aM7lky1";
   // public final static String HTTPURL = "http://192.168.1.12:8080/rof/rest/stateless/";


    public final static  boolean IS_PRODUCTION_ENVIRONMENT= true;
    static String  URLAddress= getServerAddress();


    public final static String HTTPURL = URLAddress + "adminapi/";
    public final static String HTTP_PRINT_INVOICE_URL =URLAddress +"printInvoice.php";
    public final static String HTTP_APK_DOWNLOAD_URL="http://205.147.111.69/apk/mpos.apk";


    public static final String EZETAP_DEMO_APPKEY= "23b87280-a7fb-49ca-839a-7e5999f8df88";
    public static final String EZETAP_PROD_APPKEY="262a3d0e-1195-4d72-9c23-a0ee1cc580f3";
    public static final String EZETAP_MERCHANTNAME = getEzetapMerchantname();



    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_DIALOG_SINGLEBUTTON = "singlebutton";
    public static final String ACTION_DIALOG_TWOBUTTONS = "twobuttons";
    public static final String ACTION_DIALOG_THREEBUTTONS = "threebuttons";
    public static final String INTENT_EXTRA_CONNECTION = "connection";

    public  static final int SOCKET_TIMEOUT_MILLISEC = 60000;
    public static final String SHAREDPREFENCE = "ava";

    public static final String CACHE_USERNAME="username";
    public static final String CACHE_FLIGHTNOSALEREASONS="FlightNoSaleReasons";


    public static final String CACHE_SCANEDPRODUCTLIST="SCANEDPRODUCTLIST";






    public static final String CACHE_SERVER_DATE="serverdate";
    public static final String CACHE_UNIXTIME_STAMP="UnixTimeStamp";

    public static final String CACHE_PAYMENT_TYPE="paymenttype";
    public static final String CACHE_CLASS_TYPE="classtype";
    public static final String CACHE_PAYMENT_CARD_LIST="paymentcardlist";

    public static final String CACHE_PAYMENT_TYPE_With_Action="paymenttypewithaction";



    public static final String CACHE_TITLE="title";

    public static final String CACHE_PAYMENT_TYPE_MANUAL="payment_type_manual";

    public static final String CACHE_EMPLOYEE_LIST="employeelist";
    public static final String CACHE_PASSENGERINFO="passengerinfo";

    public static final String CACHE_INDIGOONFLIGHTPAYMENT="IndigoOnFlightPayment";



    public static final String CACHE_PASSENGER_DETAIL="passengerdetail";

    public static final String CACHE_PRODUCT_SCAN="productscan";

    public static final String CACHE_COMBO_PRODUCT="comboproduct";

    public static final String CACHE_COMBO_PRIMARY_PRODUCT="comboprimaryproduct";
    public static final String CACHE_RAILWAY_OFFLINE_MODE="ralwayofflinemode";


   // public static final String CACHE_FLIGHT_RESPONCE="flightresponce";

    public static final String PRINCIPAL_CACHE="PrincipalsCache";


    public static final String CACHE_PORT="port";
    public static final String CACHE_IPADDRESS="ipaddress";
    public static final String CACHE_PROTOCOL="protocol";
    public static final String CACHE_IPVALID="ipvalid";
    public static final String CACHE_PROMOTION="promotion";


    public static final String CACHE_MAMBERID="memberid";

    public static final String MENU_MAIN_TOP_KEY_FLIGHTNOPROTOCOL="FlightNoProtocol";
    public static final String AVIATION_SALE="aviationsales";
    public static final String MENU_MAIN_TOP_KEY_AWBQUERY="awbquery";


    public static final String MENU_SETTING_VERTICAL_KEY_IPSETTING="ipsetting";
    public static final String MENU_SETTING_VERTICAL_KEY_EXIT="exit";


    public static final String SINGLE_IMAGE_ACTION_USER="clickpicuser";
    public static final String SINGLE_IMAGE_ACTION_SERVER="getserverurlimage";

    public static final int BARCODE_DETECTOR_CODE=1;
    public static final int BARCODE_ZXING_CODE=2;

    public static final int BARCODE_SCANNER=BARCODE_ZXING_CODE;



    public  static String getHttpURL(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        String protocol= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROTOCOL,null);
        String ipAddress= (String)sharedPreferences.getString( ApplicationConstant.CACHE_IPADDRESS,null);
        String port= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PORT,null);

        if(protocol!=null && ipAddress != null && port!=null){
            return protocol+"://"+ipAddress + ":" + port + "/rof/rest/stateless/";
        }else{
            return HTTPURL;
        }

    }


    private static String getServerAddress(){
        if(IS_PRODUCTION_ENVIRONMENT){
            return "http://205.147.111.69/swapp/";
        }else{
            return "http://205.147.111.196/swappuat/";
        }
    }

    private static String getEzetapMerchantname(){
        if(IS_PRODUCTION_ENVIRONMENT){
            return "DEALS_BY_AVA_24";
        }else{
            return "AVA_MERCHANDISING (5280)";
        }
    }


}
