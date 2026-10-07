/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
package jrain.hash.hashInstance.hashes.immutable;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jrain.utils.HexadecimalUtils;

public class Hashes_Test {
	
	private final String s="isrudgfh 48tuae4o 8q 4�tpo83q y49	8	u+9	u +8	y4t t�	o TEUJ�AIOEW FJ480EY	 rriuahuhy4l iseurhifse7 4yrs direhfskeuy7yf";
	private Hashes h=new Hashes();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		h.update(s.getBytes(), 0, s.length());
		h.digest();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashesOutput() {
		
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA3-512")).equals("225264290d8af45c6b1396876df6b70b493f587d276f339511af17e85b06de0579615f8cda6a63eadf1fe09408cf799449a7a0b7a8ac1499447ef666bed9096c"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-384")).equals("808f9d2facb9d66fa4a62f2a91b18808f9d559dd62a92e2f321a5af78d1058881fb18274982249537fe5cf20b6573a2a"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("CRC32")).equals("fdfc3551"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-1")).equals("9ef058239b097cc461885f775b6259b06049f2a7"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("Adler32")).equals("6a782939"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("XOR8")).equals("cd"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA3-384")).equals("e00adc84855fcfc6a5187c5ac6be449ab72c16a47eb745d2d40be641cf4e1fc42524236b9658ddd7e906381e3524c93d"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-224")).equals("f2ecd81a97c0cbe7edc5dbf1c181d59f8c50e2455c1687b772d74098"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-512/256")).equals("7e49843710c4617f4a5f4652ad9dfd8ac389e41b516c5bca1c7602953c886a3a"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-256")).equals("24e2b8a2f72464f66089dd6e16f5578ea343e6d3929b05f0c1ba9b2291058a37"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("MD2")).equals("97f85ac79a44ef9677a8cdb182e296b2"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-512/224")).equals("a117c281ebbb8657f3b96e078141959813b3117b7d5b82cf744107c4"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA3-256")).equals("9a458898007e0c9b2eaec15e73e6726e9962fe7eb3b078b4bcfd0fbc16a58a2b"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA-512")).equals("0fff8be43a57dd586242ad38734d0b11f3ba74d41ccce972640cb11396e04122f189638c98704d3359767027a8f771dde11da909b0b5d711cfaa65a6b1e2ebc4"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("MD5")).equals("a50d42c303ee17838e626be04fb747ae"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHA3-224")).equals("be2e37bb1da0dd43fcec62829d77a543ffcf65bcc00b36770521065e"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHAKE128-256")).equals("2bfc5b5c8c15dd126b254d1e79ad8fd572a75c98b7b306dc1595202c17f592bc"));
assertTrue(HexadecimalUtils.convertToHex(h.hash("SHAKE256-512")).equals("8f37592128bee1db98089c0fed09bd6a37b4de4505ada5af952b936a3bc89ec4d723046cffcf289b941815dda17045d62e047f8839013250a7cdfb6dcd5e85f2"));

	}


//	public static void main(String[] args) throws NoSuchAlgorithmException {
//		
//		System.out.println(h.isActive());
//		System.out.println(h.hasHashes());
//		System.out.println(h.wasCalculated());
////		System.out.println(h.asTaggedTable());
//		System.out.println(h);
//		/*
//		 * output:
//		 * { 
//		 * SHA3-512=225264290d8af45c6b1396876df6b70b493f587d276f339511af17e85b06de0579615f8cda6a63eadf1fe09408cf799449a7a0b7a8ac1499447ef666bed9096c ; 
//		 * CRC32=fdfc3551 ; 
//		 * SHA-384=808f9d2facb9d66fa4a62f2a91b18808f9d559dd62a92e2f321a5af78d1058881fb18274982249537fe5cf20b6573a2a ; 
//		 * SHA=9ef058239b097cc461885f775b6259b06049f2a7 ; 
//		 * Adler32=6a782939 ; 
//		 * XOR8=cd ; 
//		 * SHA3-384=e00adc84855fcfc6a5187c5ac6be449ab72c16a47eb745d2d40be641cf4e1fc42524236b9658ddd7e906381e3524c93d ; 
//		 * SHA-224=f2ecd81a97c0cbe7edc5dbf1c181d59f8c50e2455c1687b772d74098 ; 
//		 * SHA-512/256=7e49843710c4617f4a5f4652ad9dfd8ac389e41b516c5bca1c7602953c886a3a ; 
//		 * SHA-256=24e2b8a2f72464f66089dd6e16f5578ea343e6d3929b05f0c1ba9b2291058a37 ; 
//		 * MD2=97f85ac79a44ef9677a8cdb182e296b2 ; 
//		 * SHA-512/224=a117c281ebbb8657f3b96e078141959813b3117b7d5b82cf744107c4 ; 
//		 * SHA3-256=9a458898007e0c9b2eaec15e73e6726e9962fe7eb3b078b4bcfd0fbc16a58a2b ; 
//		 * SHA-512=0fff8be43a57dd586242ad38734d0b11f3ba74d41ccce972640cb11396e04122f189638c98704d3359767027a8f771dde11da909b0b5d711cfaa65a6b1e2ebc4 ; 
//		 * MD5=a50d42c303ee17838e626be04fb747ae ; 
//		 * SHA3-224=be2e37bb1da0dd43fcec62829d77a543ffcf65bcc00b36770521065e 
//		 * }
//		 */
//	}
}
