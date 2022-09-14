package jrain.hash.hashInstance.hashes.immutable;

import java.security.NoSuchAlgorithmException;

public class Hashes_Test {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String s="isrudgfh 48tuae4o 8q 4�tpo83q y49	8	u+9	u +8	y4t t�	o TEUJ�AIOEW FJ480EY	 rriuahuhy4l iseurhifse7 4yrs direhfskeuy7yf";
		Hashes h=new Hashes();
		h.update(s.getBytes(), 0, s.length());
		h.digest();
		System.out.println(h.isActive());
		System.out.println(h.hasHashes());
		System.out.println(h.wasCalculated());
//		System.out.println(h.asTaggedTable());
		System.out.println(h);
		/*
		 * output:
		 * { 
		 * SHA3-512=225264290d8af45c6b1396876df6b70b493f587d276f339511af17e85b06de0579615f8cda6a63eadf1fe09408cf799449a7a0b7a8ac1499447ef666bed9096c ; 
		 * CRC32=fdfc3551 ; 
		 * SHA-384=808f9d2facb9d66fa4a62f2a91b18808f9d559dd62a92e2f321a5af78d1058881fb18274982249537fe5cf20b6573a2a ; 
		 * SHA=9ef058239b097cc461885f775b6259b06049f2a7 ; 
		 * Adler32=6a782939 ; 
		 * XOR8=cd ; 
		 * SHA3-384=e00adc84855fcfc6a5187c5ac6be449ab72c16a47eb745d2d40be641cf4e1fc42524236b9658ddd7e906381e3524c93d ; 
		 * SHA-224=f2ecd81a97c0cbe7edc5dbf1c181d59f8c50e2455c1687b772d74098 ; 
		 * SHA-512/256=7e49843710c4617f4a5f4652ad9dfd8ac389e41b516c5bca1c7602953c886a3a ; 
		 * SHA-256=24e2b8a2f72464f66089dd6e16f5578ea343e6d3929b05f0c1ba9b2291058a37 ; 
		 * MD2=97f85ac79a44ef9677a8cdb182e296b2 ; 
		 * SHA-512/224=a117c281ebbb8657f3b96e078141959813b3117b7d5b82cf744107c4 ; 
		 * SHA3-256=9a458898007e0c9b2eaec15e73e6726e9962fe7eb3b078b4bcfd0fbc16a58a2b ; 
		 * SHA-512=0fff8be43a57dd586242ad38734d0b11f3ba74d41ccce972640cb11396e04122f189638c98704d3359767027a8f771dde11da909b0b5d711cfaa65a6b1e2ebc4 ; 
		 * MD5=a50d42c303ee17838e626be04fb747ae ; 
		 * SHA3-224=be2e37bb1da0dd43fcec62829d77a543ffcf65bcc00b36770521065e 
		 * }
		 */
	}
}
