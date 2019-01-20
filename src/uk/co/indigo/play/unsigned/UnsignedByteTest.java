/*
 * Created on 07-Jun-2005
 *
 */
package uk.co.indigo.play.unsigned;

import junit.framework.TestCase;

/**
 * @author milbuw
 *
 */
public class UnsignedByteTest extends TestCase {
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnsignedByteTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testAnd() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0x00);
		result = usb.and(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0x00);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.and(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0x55);
		result = usb.and(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0x55);
		usb = new UnsignedByte((byte)0xAA);
		result = usb.and(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xAA);
	}

	public final void testLrotate() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0x80);
		result = usb.lrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x01);
		usb = new UnsignedByte((byte)0x01);
		result = usb.lrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x02);
		usb = new UnsignedByte((byte)0x02);
		result = usb.lrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x04);
		usb = new UnsignedByte((byte)0x81);
		result = usb.lrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x03);
		usb = new UnsignedByte((byte)0xF0);
		result = usb.lrotate(4);
		assertEquals(result.getWrappedByte(),(byte)0x0F);
	}

	public final void testLshift() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0xFF);
		result = usb.lshift(1);
		assertEquals(result.getWrappedByte(),(byte)0xFE);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.lshift(2);
		assertEquals(result.getWrappedByte(),(byte)0xFC);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.lshift(3);
		assertEquals(result.getWrappedByte(),(byte)0xF8);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.lshift(4);
		assertEquals(result.getWrappedByte(),(byte)0xF0);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.lshift(8);
		assertEquals(result.getWrappedByte(),(byte)0x00);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.lshift(16);
		assertEquals(result.getWrappedByte(),(byte)0x00);
	}

	public final void testComplement() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0xFF);
		result = usb.complement();
		assertEquals(result.getWrappedByte(),(byte)0x00);
		usb = new UnsignedByte((byte)0x00);
		result = usb.complement();
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0xF0);
		result = usb.complement();
		assertEquals(result.getWrappedByte(),(byte)0x0F);
		usb = new UnsignedByte((byte)0x0F);
		result = usb.complement();
		assertEquals(result.getWrappedByte(),(byte)0xF0);
		usb = new UnsignedByte((byte)0xAA);
		result = usb.complement();
		assertEquals(result.getWrappedByte(),(byte)0x55);
	}

	public final void testOr() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0x00);
		result = usb.or(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.or(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0x55);
		result = usb.or(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0xAA);
		result = usb.or(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0x00);
		result = usb.or(new UnsignedByte(((byte)0x00)));
		assertEquals(result.getWrappedByte(),(byte)0x00);
	}

	public final void testRrotate() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0x80);
		result = usb.rrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x40);
		usb = new UnsignedByte((byte)0x01);
		result = usb.rrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x80);
		usb = new UnsignedByte((byte)0x02);
		result = usb.rrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0x01);
		usb = new UnsignedByte((byte)0x81);
		result = usb.rrotate(1);
		assertEquals(result.getWrappedByte(),(byte)0xC0);
		usb = new UnsignedByte((byte)0xF0);
		result = usb.rrotate(4);
		assertEquals(result.getWrappedByte(),(byte)0x0F);
	}

	public final void testRshift() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0xFF);
		result = usb.rshift(1);
		assertEquals(result.getWrappedByte(),(byte)0x7F);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.rshift(2);
		assertEquals(result.getWrappedByte(),(byte)0x3F);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.rshift(3);
		assertEquals(result.getWrappedByte(),(byte)0x1F);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.rshift(4);
		assertEquals(result.getWrappedByte(),(byte)0x0F);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.rshift(8);
		assertEquals(result.getWrappedByte(),(byte)0x00);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.rshift(16);
		assertEquals(result.getWrappedByte(),(byte)0x00);
	}

	public final void testXor() {
		UnsignedByte usb;
		UnsignedByte result;
		usb = new UnsignedByte((byte)0x00);
		result = usb.xor(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xFF);
		usb = new UnsignedByte((byte)0xFF);
		result = usb.xor(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0x00);
		usb = new UnsignedByte((byte)0x55);
		result = usb.xor(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0xAA);
		usb = new UnsignedByte((byte)0xAA);
		result = usb.xor(new UnsignedByte(((byte)0xFF)));
		assertEquals(result.getWrappedByte(),(byte)0x55);
		usb = new UnsignedByte((byte)0x00);
		result = usb.xor(new UnsignedByte(((byte)0x00)));
		assertEquals(result.getWrappedByte(),(byte)0x00);
	}

}
