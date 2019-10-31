package com.volleynet.utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class ApiSignature {
    /**
     * API auth signature creation method using token and
     * secret key. Uses Hash HMAC algorithm with SHA256
     *
     * @param token         Unique auth token provided to user
     * @param secret        Secret key of the user
     * @param parameters    Request parameters as key value pair
     * @param url           Complete API url without query string or request params
     * @param requestMethod HTTP request method for this request (GET|POST|PUT|DELETE|OPTIONS)
     * @return base64 encoded signature
     */
    public static String createSignature(String token, String secret, HashMap<String, String> parameters, String url, String requestMethod) {
        AppLog.d("API trace: X-Api-AuthToken : " + token);
        AppLog.d("API trace: Requested Url : " + url + "; Method Type : " + requestMethod);

        String longStr = requestMethod + '|' + url + "\n";

        // if mapData is present the sort it and add key value to the string
        if (parameters != null) {
            TreeMap<String, String> sortedData = new TreeMap<>(parameters);
            for (Map.Entry<String, String> entry : sortedData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                longStr += (key + value);
            }
        }

        // trim the string
        longStr = (longStr + token).trim();

        // if secret is null then there is no need of creating th signature since signature creating needs
        // api secret
        if (secret != null) {
            try {
                final String algorithm = "HmacSHA256";
                SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), algorithm);
                Mac mac = Mac.getInstance(algorithm);
                mac.init(signingKey);
                byte[] rawHmac = mac.doFinal(longStr.getBytes());
                String hexHmacString = Utility.bytesToHex(rawHmac);
                byte[] hexHmacByte = hexHmacString.trim().toLowerCase().getBytes();
                return Base64.encodeToString(hexHmacByte, Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException exception) {
                AppLog.e(exception);
                return null;
            } catch (InvalidKeyException exception) {
                AppLog.e(exception);
                return null;
            }
        }
        return null;
    }
}
