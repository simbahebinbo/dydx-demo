package com.pyruz.dydx;

import org.json.JSONObject;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.engine.storage.AbsApiState;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Numeric;

import java.security.SignatureException;

public class Boarding {

    public static void main(String[] args) {
        String privateKey1 = "0xa31967405f780ebd34835de20fe442d58cebae4700a95ddaa61c5a4c0b789bb2";
        Credentials credentials = Credentials.create(privateKey1);
        System.out.println("PrivateKey: " + credentials.getEcKeyPair().getPrivateKey());
        System.out.println("PublicKey: " + credentials.getEcKeyPair().getPublicKey());
        System.out.println("Address: " + credentials.getAddress());

        JSONObject dydxSignMessage = new JSONObject();
        dydxSignMessage.put("action", "DYDX-ONBOARDING");
        dydxSignMessage.put("onlySignOn", "https://trade.dydx.exchange");
        System.out.println(dydxSignMessage);
        System.out.println(getSignedData(Signer.signPrefixedMessage(dydxSignMessage.toString().getBytes(), credentials.getEcKeyPair())));
        System.out.println(getSignedData(Signer.signMessage(dydxSignMessage.toString().getBytes(), credentials.getEcKeyPair())));
    }

    private static String getSignedData(SignatureData signatureData) {
        String r = Numeric.toHexString(signatureData.getR());
        String s = Numeric.toHexString(signatureData.getS()).substring(2);
        String v = Numeric.toHexString(signatureData.getV()).substring(2);
        return r + s + v;
    }

}

