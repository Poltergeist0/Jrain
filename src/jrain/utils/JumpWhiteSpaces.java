package jrain.utils;



public class JumpWhiteSpaces {
	public static boolean isWhiteSpace(String s){
		if(" ".equals(s)) return true;
		if("\t".equals(s)) return true;
		if("\r".equals(s)) return true;
		if("\n".equals(s)) return true;
		return false;
	}
	
	public static int jumpWhiteSpaces(String s, int currentPosition){
		while(isWhiteSpace(s.substring(currentPosition, currentPosition+1))) currentPosition++;
		return currentPosition;
	}
	
	public static int xmlJump(String s, int currentPosition){
		//jump over white spaces and XML comments
		//return new position or -1 if there is no comment closing tag
		int pos=jumpWhiteSpaces(s, currentPosition);
		//jump comments
		if(s.startsWith("<!--", pos)){
			pos+=4;
			pos=s.indexOf("-->",pos);
			if(pos<0){
				return -1;
			}
			pos+=3;
			pos=JumpWhiteSpaces.jumpWhiteSpaces(s, pos);
		}
		return pos;
	}
	
}
