package com.pyruz.dydx;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Boarding {

    public static void main(String[] args) {
        //pkDisplay();
        System.out.println();
        signDYDXMessage();
    }

    private static String getSignedData(SignatureData signatureData) {
        String r = Numeric.toHexString(signatureData.getR());
        String s = Numeric.toHexString(signatureData.getS()).substring(2);
        String v = Numeric.toHexString(signatureData.getV()).substring(2);
        return r + s + v;
    }


    private static void signDYDXMessage() {
        String privateKey1 = "a31967405f780ebd34835de20fe442d58cebae4700a95ddaa61c5a4c0b789bb2";
        Credentials credentials = Credentials.create(privateKey1);
        System.out.println("PrivateKey: " + credentials.getEcKeyPair().getPrivateKey());
        System.out.println("PublicKey: " + credentials.getEcKeyPair().getPublicKey());
        System.out.println("Address: " + credentials.getAddress());

        MessageBean messageBean = new MessageBean("DYDX-ONBOARDING", "https://trade.dydx.exchange");

        System.out.println(messageBean);
        System.out.println(getSignedData(Signer.signPrefixedMessage(messageBean.toString().getBytes(), credentials.getEcKeyPair())));
        System.out.println(getSignedData(Signer.signMessage(messageBean.toString().getBytes(), credentials.getEcKeyPair())));
    }

    private static void pkDisplay() {
        String privateKey1 = "a31967405f780ebd34835de20fe442d58cebae4700a95ddaa61c5a4c0b789bb2";
        Credentials credentials = Credentials.create(privateKey1);
        System.out.println("pk:" + Numeric.toHexStringNoPrefix(credentials.getEcKeyPair().getPublicKey()));

        JSONObject dydxSignMessage = new JSONObject();
        dydxSignMessage.put("action", "DYDX-ONBOARDING");
        dydxSignMessage.put("onlySignOn", "https://trade.dydx.exchange");

        MessageBean messageBean = new MessageBean("DYDX-ONBOARDING", "https://trade.dydx.exchange");

        System.out.println(messageBean);

        String label = "\u0019Ethereum Signed Message:\n" + (messageBean.toString().getBytes().length) + messageBean.toString();
        System.out.println("hash:" + Hash.sha3String(label));

        ByteBuffer buffer = ByteBuffer.allocate(label.getBytes().length);
        buffer.put(label.getBytes());
        byte[] array = buffer.array();
        Sign.SignatureData signature = Sign.signMessage(array, credentials.getEcKeyPair(), true);

        ByteBuffer sigBuffer = ByteBuffer.allocate(signature.getR().length + signature.getS().length + 1);
        sigBuffer.put(signature.getR());
        sigBuffer.put(signature.getS());
        sigBuffer.put(signature.getV());
        System.out.println("sig:" + Numeric.toHexString(sigBuffer.array()));

        ECDSASignature esig = new ECDSASignature(Numeric.toBigInt(signature.getR()), Numeric.toBigInt(signature.getS()));
        BigInteger res = Sign.recoverFromSignature(0, esig, Hash.sha3(label.getBytes()));
        System.out.println("public Ethereum address: 0x" + Keys.getAddress(res));
    }

    public static class MessageBean {
        private String action;
        private String onlySignOn;

        public MessageBean(String action, String onlySignOn) {
            this.action = action;
            this.onlySignOn = onlySignOn;
        }

        @Override
        public String toString() {
            return "MessageBean{" +
                    "action='" + action + '\'' +
                    ", onlySignOn='" + onlySignOn + '\'' +
                    '}';
        }
    }

}

