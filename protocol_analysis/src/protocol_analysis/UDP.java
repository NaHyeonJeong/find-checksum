package protocol_analysis;

import method.Method;

public class UDP {
	
	Method method = new Method();
	private String udp = "";
	private int sourcePortNumSize = 4, destinationPortNumSize = 4, totalLenSize = 4, checksumSize = 4; 

	// 持失切
	public UDP(String ip) {
		this.udp = ip;
	}
	
	// 五社球
	public void udp() {
		System.out.println("\n3. UDP (Layer 4)");
		byte[] bytes = udp.getBytes();
		
		if(bytes.length > 0) {
			// 1)
			String sourcePortNum = new String(bytes, 0, sourcePortNumSize);
			int sourcePortNumTmp = Integer.parseInt(sourcePortNum, 16);
			System.out.print(" 1) Source Port Number = " + sourcePortNum + " / ");
			method.portInfo(sourcePortNumTmp);
			
			// 2)
			String detinationPortNum = new String(bytes, 4, destinationPortNumSize);
			int detinationPortNumTmp = Integer.parseInt(detinationPortNum, 16);
			System.out.print(" 2) Destination Port Number = " + detinationPortNum + " / ");
			method.portInfo(detinationPortNumTmp);
			
			// 3)
			String totalLen = new String(bytes, 8, totalLenSize);
			int totalLenTmp = Integer.parseInt(totalLen, 16);
			System.out.println(" 3) Total Length = " + totalLen + " / " + totalLenTmp + " bytes");
			
			// 4)
			String checksum = new String(bytes, 12, checksumSize);
			System.out.println(" 4) Checksum = " + checksum);
		}
	}
}
