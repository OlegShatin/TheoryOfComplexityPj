package main.java;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Oleg Shatin
 *         11-501
 */
public class XMLSerializer {
    public static void write(Object f, String filename, boolean addToFile) throws Exception{
        XMLEncoder encoder =
                new XMLEncoder(
                        new BufferedOutputStream(
                                new FileOutputStream(filename, addToFile)));
        encoder.writeObject(f);
        encoder.close();
    }

    public static Object read(String filename) throws Exception {
        XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(filename)));
        Object o = (Object)decoder.readObject();
        decoder.close();
        return o;
    }
}
