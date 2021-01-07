package method;
import protocol_analysis.ServicesDB;
public class Method {

	ServicesDB sdb1 = new ServicesDB();
	ServicesDB sdb2 = new ServicesDB();

	public void portInfo(int tmp) {
		if(tmp >= 0 && tmp <= 1023) {// 0 ~ 1023
			sdb1.selectAll(tmp);
			System.out.println("[Well-Known Port]");
		}
		else if(tmp > 1023 && tmp <= 49151) {// 1024 ~ 49151
			sdb1.selectAll(tmp);
			System.out.println("[Registered Port]");
		}
		else if(tmp > 49151 && tmp <= 65535) // 49152 ~ 65535
			System.out.println("[Dynamic(Client) Port]");
	}

	public StringBuffer colon(String source) { // :
		StringBuffer buffer = new StringBuffer(source);
		for(int i=2;i<buffer.length();i++) {
			buffer.insert(i, ":");
			i+=2;
		}
		return buffer;
	}

	public void toDecimal(byte[] bytes, String[] addressBits, int[] addressBitsTmp) {
		if(bytes.length > 2) {
			for(int i=0;i<4;i++) {
				addressBits[i] = new String(bytes, i*2, 2);
				addressBitsTmp[i] = Integer.parseInt(addressBits[i], 16);
			}
			System.out.println(addressBitsTmp[0] + "." + addressBitsTmp[1] + "." + addressBitsTmp[2] + "." + addressBitsTmp[3]);
		}
	}
	
	public void toBinary(byte[] bytes, String[] string, int[] stringTmp, String[] binary) {
		if(bytes.length > 0) {
			for(int i=0;i<string.length;i++) {
				string[i] = new String(bytes, i, 1);
				stringTmp[i] = Integer.parseInt(string[i], 16);
				binary[i] = Integer.toBinaryString(stringTmp[i]);
				if(binary[i].length() < 4) binary[i] = "000".substring(binary[i].length() - 1) + binary[i];
				System.out.print(binary[i] + " ");
			}
		}
	}
}
