/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.util.BcdHelper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GenericDigitsTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private ByteBuf getEvenData() {
        return Unpooled.wrappedBuffer(new byte[] { 3, 0x21, 0x43, 0x65 }); // "123456" EVEN
    }

    private ByteBuf getOddData() {
        return Unpooled.wrappedBuffer(new byte[] { 35, 0x21, 0x43, 0x65, 0x07 }); // "1234567" ODD
    }

    private ByteBuf getEncodedEvenData() {
        return Unpooled.wrappedBuffer(new byte[] { 0x21, 0x43, 0x65 });
    }

    private ByteBuf getEncodedOddData() {
        return Unpooled.wrappedBuffer(new byte[] { 0x21, 0x43, 0x65, 0x07 });
    }

    private ByteBuf getIA5Data() {
        return Unpooled.wrappedBuffer(new byte[] { 67, 65, 66, 97, 98, 49, 50 }); // "ABab12"
    }

    private String digitsEvenString = "123456";

    private String digitsOddString = "1234567";

    private String digitsIA5String = "ABab12";

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecodeEven() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.decode(getEvenData());

        assertEquals(prim.getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BCD_EVEN);
        assertEquals(prim.getTypeOfDigits(), GenericDigits._TOD_BGCI);
        assertEquals(prim.getEncodedDigits(), getEncodedEvenData());
        assertEquals(prim.getDecodedDigits(), "123456");
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecodeOdd() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.decode(getOddData());

        assertEquals(prim.getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BCD_ODD);
        assertEquals(prim.getTypeOfDigits(), GenericDigits._TOD_BGCI);
        assertEquals(prim.getEncodedDigits(), getEncodedOddData());
        assertEquals(prim.getDecodedDigits(), "1234567");
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncodeEven() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_EVEN, GenericDigits._TOD_BGCI,
                                                       getEncodedEvenData());
        // int encodingScheme, int typeOfDigits, byte[] digits

        ByteBuf data = getEvenData();
        ByteBuf encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));


        prim = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_EVEN, GenericDigits._TOD_BGCI, "123456");
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);
        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncodeOdd() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_ODD, GenericDigits._TOD_BGCI,
                                                       getEncodedOddData());
        // int encodingScheme, int typeOfDigits, byte[] digits

        ByteBuf data = getOddData();
        ByteBuf encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));


        prim = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_ODD, GenericDigits._TOD_BGCI, "1234567");
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);
        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testSetDecodedDigits() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.setDecodedDigits(GenericDigits._ENCODING_SCHEME_BCD_EVEN, digitsEvenString );
        prim.setTypeOfDigits(GenericDigits._TOD_BGCI);
        assertTrue(digitsEvenString.equals(prim.getDecodedDigits()));

        ByteBuf data = getEvenData();
        ByteBuf encodedData=Unpooled.buffer();
        prim.encode(encodedData);
        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim.setDecodedDigits(GenericDigits._ENCODING_SCHEME_BCD_ODD, digitsOddString );
        prim.setTypeOfDigits(GenericDigits._TOD_BGCI);
        assertTrue(digitsOddString.equals(prim.getDecodedDigits()));
        data = getOddData();
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);
        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testSetDecodedHexDigits() throws Exception {
        String hexString = "0123456789abcdef*#";
        System.out.println("Test input digits: " + hexString);
        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.setDecodedDigits(GenericDigits._ENCODING_SCHEME_BCD_EVEN, hexString );
        prim.setTypeOfDigits(GenericDigits._TOD_BGCI);
        String decodedDigitsString = prim.getDecodedDigits();
        System.out.println("Decoded  digits: " + decodedDigitsString);
        String convertedDigits = BcdHelper.convertTelcoCharsToHexDigits(decodedDigitsString);
        assertTrue(BcdHelper.convertTelcoCharsToHexDigits(hexString).equals(convertedDigits));
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncodingIA5() throws Exception {
        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.setDecodedDigits(GenericDigits._ENCODING_SCHEME_IA5, digitsIA5String);
        prim.setTypeOfDigits(GenericDigits._TOD_BGCI);
        
        ByteBuf data=Unpooled.buffer();
        prim.encode(data);
        assertEquals(getIA5Data(), data);
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecodingIA5() throws Exception {
        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.decode(getIA5Data());

        assertEquals(prim.getEncodingScheme(), GenericDigits._ENCODING_SCHEME_IA5);
        assertEquals(prim.getTypeOfDigits(), GenericDigits._TOD_BGCI);
        assertEquals(prim.getDecodedDigits(), digitsIA5String);
    }
}
