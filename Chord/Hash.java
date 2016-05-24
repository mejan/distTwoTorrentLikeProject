package Chord;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mejan on 2016-04-22.
 */

//
//If we need bigger HASH we need to use long types int is not enough.
//

public class Hash {
    private static final String DEFAULT_HASH_METHOD = "SHA-1";
    private static MessageDigest md;
    public static int HASH_LENGTH = 24; //hash # bits

    private Hash(){}

    public static int hash(String toHash) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(DEFAULT_HASH_METHOD);
        md.reset();
        md.update(toHash.getBytes());
        //return new BigInteger(1, md.digest()).intValue();
        return toId(md.digest());
    }


    public static int toId(byte[] hashBytes){

        return ((hashBytes[1] & 0xFF) << 16) | ((hashBytes[2] & 0xFF) << 8) | (hashBytes[3] & 0xFF);

    }
}