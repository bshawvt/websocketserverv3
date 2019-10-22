package server.ssl;

import java.util.Base64;

public class PEMTokenJava11 extends PEMToken {

	@Override
	public byte[] get(String token) {
	
	   	return Base64.getMimeDecoder().decode(token);

	}

}
