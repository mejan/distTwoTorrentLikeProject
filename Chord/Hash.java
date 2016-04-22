package Chord;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mejan on 2016-04-22.
 */
public class Hash {
    private static final String DEFAULT_HASH_METHOD = "SHA-1";
    private static MessageDigest md;
    public static int m = 16; //hash # bits
    private Hash(){}

    public static int hash(String toHash) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(DEFAULT_HASH_METHOD);
        md.reset();
        md.update(toHash.getBytes());
        return toId(md.digest());
    }

    public static int toId(byte[] hashBytes){
        /*ArrayList tmp = new ArrayList<Integer>();
        for(int i=0; i<m/8; i++){
            tmp.add(hashBytes[i]);
            if(tmp.get(i)<0){
                tmp.get(i).equals(tmp.get(i) & 0xFF);
            }
        }
        int toRet=0;
        for(int i = 0; i<m/8;i++){
            toRet += (int) tmp.get(i);
        }*/
        //Byte tmp = hashBytes[0];
        //Byte tmp2 = hashBytes[1];
        return ( ( (hashBytes[0] & 0xFF) << 8) | (hashBytes[1] & 0x00FF));
        //Ful Lösning (försök finna bättre).
        /*if(test<0){
            test = (tmp & 0xFF);
        }*/
        //Slut på ful lösning.e

        //return test;
        ///return test;
    }
}