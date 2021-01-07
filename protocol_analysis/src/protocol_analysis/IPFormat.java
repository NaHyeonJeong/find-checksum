package protocol_analysis;

import method.Method;

public class IPFormat {

	Method method = new Method();
	private String ip = "";
	String sendToNext = "";
	private int verSize = 1, hLenSize = 1, flagSize = 1;
	private int serviceSize = 2, ttlSize = 2, protocolSize = 2;
	private int totalLenSize = 4, idSize = 4, checkSize = 4;
	private int offsetSize = 3;
	private int sourceSize = 8, desitnationSize = 8;

	// ������
	public IPFormat(String ethernet) {
		this.ip = ethernet;
	}

	// �޼ҵ�
	public void ip() {
		System.out.println("\n2. IP (Layer 3)");
		byte[] bytes = ip.getBytes();
		int mul = 0; // ��� ���� ���ϱ� ���� ����
		
		if(bytes.length > 0) {
			// 1)
			String version = new String(bytes, 0, verSize);
			System.out.print(" 1) Version = " + version);
			switch(version) {
			case "4" : 
				System.out.println(" / IPv4");
				break;
			case "6" : 
				System.out.println(" / IPv6");
				break;
			}

			// 2)
			String hLength = new String(bytes, verSize, hLenSize);
			System.out.print(" 2) Header Length = " + hLength);
			mul = Integer.parseInt(hLength, 16) * 4;
			if(mul == 20) System.out.println(" / " + mul + " bytes : No - Option");
			else System.out.println(" : Option Exist");

			// 3)
			String serviceType = new String(bytes, 2, serviceSize);
			byte[] serviceTypeBytes = serviceType.getBytes();
			String[] serviceTypeA = new String[2];
			int[] serviceTypeTmp = new int[2];
			String[] serviceTypeBinary = new String[2];
			// flag�� ����ȭ �ϱ� ����
			byte[] serviceFlagBytes1 = null;
			byte[] serviceFlagBytes2 = null;

			System.out.print(" 3) Service Type = ");
			method.toBinary(serviceTypeBytes, serviceTypeA, serviceTypeTmp, serviceTypeBinary);
			serviceFlagBytes1 = serviceTypeBinary[0].getBytes();
			serviceFlagBytes2 = serviceTypeBinary[1].getBytes();

			if(serviceTypeBinary[0].equals("0000") && serviceTypeBinary[1].equals("0000"))
				System.out.println("/ No Service Type");
			else {
				System.out.println();
				String precedence = new String(serviceFlagBytes1, 0, 3); // �켱����
				System.out.print("\t- Precedence(�켱����) = " + precedence);
				switch(precedence) {
				case "000" : 
					System.out.println(" / Routinge(Normal)");
					break;
				case "001" : 
					System.out.println(" / Priority");
					break;
				case "010" : 
					System.out.println(" / Immediate");
					break;
				case "011" : 
					System.out.println(" / Flash");
					break;
				case "100" : 
					System.out.println(" / Flash Override");
					break;
				case "101" : 
					System.out.println(" / Critical");
					break;
				case "110" : 
					System.out.println(" / Internetwork Control(OSPF���� ����)");
					break;
				case "111" : 
					System.out.println(" / Network Control");
					break;
				}
				String delay = new String(serviceFlagBytes1, 3, 1); // ����
				System.out.print("\t- Delay(����) = " + delay);
				if(delay.equals("0")) System.out.println(" / ������ ����");
				else System.out.println(" / ���� ����");

				String[] serviceFlag2Bits = new String[4];
				for(int i=0;i<4;i++) 
					serviceFlag2Bits[i] = new String(serviceFlagBytes2, i, 1);

				System.out.print("\t- Throughput(ó����) = " + serviceFlag2Bits[0]); // serviceFlag2Bits[0] = ó����
				if(serviceFlag2Bits[0].equals("0")) System.out.println(" / ������ ó����");
				else System.out.println(" / ���� ó����");
				
				System.out.print("\t- Reliability(�ŷڼ�) = " + serviceFlag2Bits[1]); // serviceFlag2Bits[1] = �ŷڼ�
				if(serviceFlag2Bits[1].equals("0")) System.out.println(" / ������ �ŷڼ�");
				else System.out.println(" / ���� �ŷڼ�");

				System.out.println("\t- Minimum Cost(�ּҺ��) = " + serviceFlag2Bits[2]); // serviceFlag2Bits[2] = �ּҺ�� 
				System.out.println("\t- Not Use = " + serviceFlag2Bits[3] + " / �׻� 0���� ����"); // serviceFlag2Bits[3] = not use
			}

			// 4)
			String totalLength = new String(bytes, 4, totalLenSize);
			int totalLen = Integer.parseInt(totalLength, 16);
			System.out.println(" 4) Total Length = " + totalLength + " / " + totalLen + " bytes -> data = " + (totalLen - mul) + " bytes");

			// 5)
			String identification = new String(bytes, 8, idSize);
			int identifiTmp = Integer.parseInt(identification, 16);
			System.out.println(" 5) Identification = " + identification + " / " + identifiTmp);

			// 6)
			String flag = new String(bytes, 12, flagSize);
			int flagTmp = Integer.parseInt(flag, 16);
			String binary = Integer.toBinaryString(flagTmp);
			if(binary.length() < 4) binary = "000".substring(binary.length() - 1) + binary;// 4bit�� ǥ���ϱ� ���ؼ� �����
			System.out.println(" 6) Flags = " + flag + " (" + binary + ")");

			// flag ����ȭ
			byte[] binaryBytes = binary.getBytes();
			String[] binaryBytesToBits = new String[3];
			int[] binaryBytesToBitsTmp = new int[3];
			for(int i=0;i<3;i++) {
				binaryBytesToBits[i] = new String(binaryBytes, i, 1);
				binaryBytesToBitsTmp[i] = Integer.parseInt(binaryBytesToBits[i], 16);
			}
			System.out.println("\t- Reserve: " + binaryBytesToBits[0] + " (�̻�� - �׻� 0)");
			System.out.print("\t- Don't Fragment: " + binaryBytesToBits[1]); // binaryBytesToBits[1] = ����ȭ ����
			if(binaryBytesToBitsTmp[1]==0) System.out.println(" (����ȭ ����)");
			else System.out.println(" (����ȭ �Ұ���)");
			System.out.print("\t- More: " + binaryBytesToBits[2]); // binaryBytesToBits[2] = ������ �� �ֳ�
			if(binaryBytesToBitsTmp[2]==0) System.out.println(" (���� ������ ������. ������ �� ����)");
			else System.out.println(" (������ �� ����)");

			// 7)
			String offset = new String(bytes, 13, offsetSize);
			System.out.println(" 7) Offset = " + offset);
			
			// 8)
			String ttl = new String(bytes, 16, ttlSize);
			int ttlTmp = Integer.parseInt(ttl, 16);
			System.out.println(" 8) TTL(Time To Leave) = " + ttl + " / " + ttlTmp + " hops");

			// Protocol�� üũ�ϰ� �������� ��������� �κ�
			int want = ip.length() - 40;
			if(bytes.length > want) {
				sendToNext = new String(bytes, 40, want);
			}
			// 9)
			String protocol = new String(bytes, 18, protocolSize);
			int protocolTmp = Integer.parseInt(protocol, 16);
			System.out.print(" 9) Protocol = " + protocol + "(" + protocolTmp + ")");
			
			switch(protocolTmp) {
			case 1 :
				System.out.println(" / ICMP");
				break;
			case 2 :
				System.out.println(" / IGMP");
				break;
			case 6 : 
				System.out.println(" / TCP");
				break;
			case 17 :
				System.out.println(" / UDP");
				break;
			case 89 :
				System.out.println(" / OSPF");
				break;
			}

			// 10)
			String checksum = new String(bytes, 20, checkSize);
			System.out.println("10) Checksum = " + checksum);
			
			// 11)
			String sourceAddress = new String(bytes, 24, sourceSize);
			System.out.print("11) Source Address = " + sourceAddress + " / ");
			byte[] sourceAddressBytes = sourceAddress.getBytes();
			String[] sourceAddressBits = new String[4];
			int[] sourceAddressBitsTmp = new int[4];
			method.toDecimal(sourceAddressBytes, sourceAddressBits, sourceAddressBitsTmp);

			// 12)
			String destinationAddress = new String(bytes, 32, desitnationSize);
			System.out.print("12) Destination Address = " + destinationAddress + " / ");
			byte[] destinationAddressBytes = destinationAddress.getBytes();
			String[] destinationAddressBits = new String[4];
			int[] destinationAddressBitsTmp = new int[4];
			method.toDecimal(destinationAddressBytes, destinationAddressBits, destinationAddressBitsTmp);

			switch(protocolTmp) {
			case 1 :
				ICMP icmp = new ICMP(sendToNext);
				icmp.icmp();
				break;
			case 2 :
				IGMP igmp = new IGMP(sendToNext);
				igmp.igmp();
				break;
			case 6 : 
				TCP tcp = new TCP(sendToNext);
				tcp.tcp();
				break;
			case 17 :
				UDP udp = new UDP(sendToNext);
				udp.udp();
				break;
			case 89 :
				OSPF ospf = new OSPF(sendToNext);
				ospf.ospf();
				break;
			}
			
			
		}
	}
}
