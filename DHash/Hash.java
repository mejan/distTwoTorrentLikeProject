/**
 * Created by heka1203 on 2016-04-20.
 */

package DHash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {


    private static final String DEFAULT_HASH_METHOD = "SHA-1";
    private static MessageDigest md;

    private Hash(){}
    public static String hash(String toHash) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(DEFAULT_HASH_METHOD);
        md.reset();
        md.update(toHash.getBytes());
        md.digest();
        byte[] hashBytes = md.toString().getBytes();
        return toHexString(hashBytes);
    }
    private static String toHexString(byte[] hashBytes){
        StringBuilder sb = new StringBuilder();
        for(Byte b : hashBytes){
            String hex = Integer.toHexString(0xFF & b);
            if(hex.length() == 0)
                sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }



}
