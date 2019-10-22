package server.ssl;

import javax.xml.bind.DatatypeConverter;

/**
 * incase Base64 module is unavailable on a system
 * this class can be used as a substitute 
 * @
 *
 */
public class PEMTokenJava8 extends PEMToken {

	@Override
	public byte[] get(String token) {
		return DatatypeConverter.parseBase64Binary(token);
	}
	
}
