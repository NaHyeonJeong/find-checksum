package protocol_analysis;

import method.Method;

public class ARPFormat {

	Method method = new Method();
	private String arp = "";
	private int hardwareTSize = 4, protocolTSize = 4, operationSize = 4;
	private int hardwareLenSize = 2, protocolLenSize = 2;
	private int senderMASize = 12, targetMASize = 12;
	private int senderIPASize = 8, targetIPASize = 8;

	// 생성자
	public ARPFormat(String ethernet) {
		this.arp = ethernet;
	}

	public void arp() {
		System.out.println("\n2. ARP (Layer 3)");
		byte[] bytes = arp.getBytes();

		if(bytes.length > 12) {
			// 1)
			String hardwareType = new String(bytes, 0, hardwareTSize);
			int tmp = Integer.parseInt(hardwareType, 16);
			System.out.print(" 1) H/W Type = " + hardwareType);
			switch(tmp) {
			case 1 : 
				System.out.println(" / Ethernet");
				break;
			case 2 : 
				System.out.println(" / Experimental Ethernet");
				break;
			case 3 : 
				System.out.println(" / Amateur Radio AX.25");
				break;
			case 4 : 
				System.out.println(" / Proteon ProNET Token Ring");
				break;
			case 5 : 
				System.out.println(" / Chaos");
				break;
			case 6 : 
				System.out.println(" / IEEE 802.3 networks");
				break;
			case 7 : 
				System.out.println(" / ARCNET");
				break;
			case 8 : 
				System.out.println(" / Hyperchannel");
				break;
			case 9 : 
				System.out.println(" / Lanstar");
				break;
			case 10 : 
				System.out.println(" / Autonet Short Address");
				break;
			case 11 : 
				System.out.println(" / LocalTalk");
				break;
			case 12 : 
				System.out.println(" / LocalNet(IBM PCNet or SYTEK LocalNET");
				break;
			}

			// 2)
			String protocolType = new String(bytes, 4, protocolTSize);
			System.out.print(" 2) Protocol Type = " + protocolType);
			if(protocolType.equals("0800")) System.out.println(" / IP");

			// 3)
			String hardwareLength = new String(bytes, 8, hardwareLenSize);
			int tmp1 = Integer.parseInt(hardwareLength, 16);
			int tmpMul1 = tmp1 * 8;
			System.out.println(" 3) H/W Length = " + hardwareLength + " / " + tmp1 + " bytes (" + tmpMul1 + " bits)");

			// 4)
			String protocolLength = new String(bytes, 10, protocolLenSize);
			int tmp2 = Integer.parseInt(protocolLength, 16);
			int tmpMul2 = tmp2 * 8;
			System.out.println(" 4) Protocol Length = " + protocolLength + " / " + tmp2 + " bytes (" + tmpMul2 + " bits)");

			// 5)
			String operation = new String(bytes, 12, operationSize);
			int tmpO = Integer.parseInt(operation, 16);
			System.out.print(" 5) Operation = " + operation);
			switch(tmpO) {
			case 1 : 
				System.out.println(" / ARP Request(요청)");
				break;
			case 2 : 
				System.out.println(" / ARP Reply(응답)");
				break;
			case 3 : 
				System.out.println(" / RARP Request(요청)");
				break;
			case 4 : 
				System.out.println(" / RARP Reply(응답)");
				break;
			}

			// 6)
			String sendMacAddress = new String(bytes, 16, senderMASize);
			StringBuffer bufferSender = method.colon(sendMacAddress); 
			System.out.print(" 6) Sender MAC Address = " + bufferSender);
			String routingS = new String(bytes, 1, 1);
			int routingSTmp = Integer.parseInt(routingS, 16);
			String binaryS = Integer.toBinaryString(routingSTmp);
			
			if(sendMacAddress.equals("000000000000")) System.out.println(" / Unknown MAC");
			else if(sendMacAddress.equals("ffffffffffff")) System.out.println(" / Broadcast");
			else { // 2진수의 마지막 자리가 0이면 unicast, 1이면 multicast 
				if(binaryS.endsWith("0")) System.out.println(" / Unicast");
				else System.out.println(" / Multicast");	
			}
			
			// 7)
			String sendIPAddress = new String(bytes, 28, senderIPASize);
			System.out.print(" 7) Sender IP Address = " + sendIPAddress + " / ");
			byte[] sendIPAddressBytes = sendIPAddress.getBytes();
			String[] sendIPAddressBits = new String[4];
			int[] sendIPAddressBitsTmp = new int[4];
			method.toDecimal(sendIPAddressBytes, sendIPAddressBits, sendIPAddressBitsTmp);
			
			// 8) 
			String targetMacAddress = new String(bytes, 36, targetMASize);
			StringBuffer targetBuffer = method.colon(targetMacAddress);
			System.out.print(" 8) Target MAC Address = " + targetBuffer);
			String routingT = new String(bytes, 1, 1);
			int routingTTmp = Integer.parseInt(routingT, 16);
			String binaryT = Integer.toBinaryString(routingTTmp);

			if(targetMacAddress.equals("000000000000")) System.out.println(" / Unknown MAC");
			else if(targetMacAddress.equals("ffffffffffff")) System.out.println(" / Broadcast");
			else { // 2진수의 마지막 자리가 0이면 unicast, 1이면 multicast
				if(binaryT.endsWith("0")) System.out.println(" / Unicast");
				else System.out.println(" / Multicast");	
			}
			
			// 9)
			String targetIPAddress = new String(bytes, 48, targetIPASize);
			System.out.print(" 9) Target IP Address = " + targetIPAddress + " / ");
			byte[] targetIPAddressBytes = targetIPAddress.getBytes();
			String[] targetIPAddressBits = new String[4];
			int[] targetIPAddressBitsTmp = new int[4];
			method.toDecimal(targetIPAddressBytes, targetIPAddressBits, targetIPAddressBitsTmp);
		}

	}
}
