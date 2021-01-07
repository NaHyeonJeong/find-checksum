package protocol_analysis;

import method.Method;

public class EthernetFormat {

	Method method = new Method();
	private String ethernet = "";
	private String afterEthernet = "";
	private int destiAddSize = 12, sourceAddSize = 12, typeSize = 4; // 28

	// 생성자
	public EthernetFormat(String ethernetFrame) {
		this.ethernet = ethernetFrame;
	}

	// 메소드
	public void ethernet() {
		System.out.println("\n1. Ethernet (Layer 2)");
		byte[] bytes = ethernet.getBytes();

		if(bytes.length > 0) { // 일정 길이 만큼 자르기 위함
			// 1)
			String destinationA = new String(bytes, 0, destiAddSize);
			StringBuffer bufferD = method.colon(destinationA);
			byte[] destinationBytes = destinationA.getBytes(); // destination address부분을 가지고 라우팅방식을 체크하기 위함
			
			System.out.print(" 1) Destination Address = " + bufferD);
			String desRouting = new String(destinationBytes, 1, 1);
			int desRoutingTmp = Integer.parseInt(desRouting, 16);
			String desRoutingBinary = Integer.toBinaryString(desRoutingTmp);
			if(destinationA.equals("ffffffffffff")) System.out.println(" / Broadcast");
			else {
				if(desRoutingBinary.endsWith("0")) System.out.println(" / Unicast");
				else System.out.println(" / Multicast"); 
			}
			
			// 2)
			String sourceA = new String(bytes, 12, sourceAddSize);
			StringBuffer bufferS = method.colon(sourceA); 
			byte[] sourceBytes = sourceA.getBytes(); // source address부분을 가지고 라우팅방식을 체크하기 위함
			System.out.print(" 2) Source Address = " + bufferS);
			String routing = new String(sourceBytes, 1, 1);
			int tmp = Integer.parseInt(routing, 16);
			String binary = Integer.toBinaryString(tmp);
			
			if(sourceA.equals("ffffffffffff")) // broadcast
				System.out.println(" / Broadcast");
			else {
				if(binary.endsWith("0")) System.out.println(" / Unicast");
				else System.out.println(" / Multicast");
			}
			
			// 다음 단계에 전달해야 하는 부분
			int want = ethernet.length() - (destiAddSize + sourceAddSize + typeSize);
			if(bytes.length > want)
				afterEthernet = new String(bytes, destiAddSize + sourceAddSize + typeSize, want);
			// 3) 0800:IP, 0806:ARP, 8035:RARP 등...
			String type = new String(bytes, destiAddSize+sourceAddSize, typeSize);
			System.out.print(" 3) Type = " + type);
			if(type.equals("0800")) { // IP
				System.out.println(" / IP");
				IPFormat ipFormat = new IPFormat(afterEthernet);
				ipFormat.ip();
			}
			else if(type.equals("0806")) { // ARP
				System.out.println(" / ARP");
				ARPFormat arpFormat = new ARPFormat(afterEthernet);
				arpFormat.arp();
			}
			else if(type.equals("8035")) { // ARP와 비교했을 때 option 필드만 다름(나머지는 동일)
				System.out.println(" / RARP");
				ARPFormat arpFormat = new ARPFormat(afterEthernet);
				arpFormat.arp();
			}
			else System.out.println();
		}
	}

}
