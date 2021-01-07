package protocol_analysis;

import method.Method;

public class IGMP {
	
	Method method = new Method();
	
	private String igmp = "";
	private int typeSize = 2, maxResponseTimeSize = 2;
	private int checksumSize = 4;
	private int ipv4MulticastAddressSize = 8;
	
	public IGMP(String ip) {
		this.igmp = ip;
	}

	public void igmp() {
		System.out.println("\n3. IGMP (Layer 3)");
		byte[] bytes = igmp.getBytes();
		
		if(bytes.length > 0) {
			// 1)
			String type = new String(bytes, 0, typeSize);
			System.out.print(" 1) Type = " + type + " / ");
			
			switch(type) {
			case "11" : 
				System.out.println("Membership Query");
				break;
			case "12" : 
				System.out.println("IGMP v1 Membership Report");
				break;
			case "16" : 
				System.out.println("IGMP v2 Membership Report");
				break;
			case "17" : 
				System.out.println("IGMP v2 Leave Group");
				break;
			case "22" : 
				System.out.println("IGMP v3 Membership Report");
				break;
			default : 
				System.out.println();	
			}
			
			// 2)
			String maxResponseTime = new String(bytes, 2, maxResponseTimeSize);
			System.out.println(" 2) Max Response Time = " + maxResponseTime);
			
			// 3)
			String checksum = new String(bytes, 4, checksumSize);
			System.out.println(" 3) Checksum = " + checksum);
			
			// 4) 단, ip의 detination 값이 multicast일 때만 필요
			// 일반 질의(query)성 메세지(11)면 -- 모든 그룹 대상이면 그냥 0으로 셋팅, 특정 그룹만이면 해당 멀티캐스트 주소로 셋팅
			String ipv4MulticastAddress = new String(bytes, 8, ipv4MulticastAddressSize);
			System.out.print(" 4) IPv4 Multicase Address = " + ipv4MulticastAddress + " / ");
			byte[] ipv4MulticastAddressBytes = ipv4MulticastAddress.getBytes();
			String[] ipv4MulticastAddressBits = new String[4];
			int[] ipv4MulticastAddressBitsTmp = new int[4];
			if(type.equals("11"))
				System.out.println();
			else
				method.toDecimal(ipv4MulticastAddressBytes, ipv4MulticastAddressBits, ipv4MulticastAddressBitsTmp);
			
		}
	}
}
