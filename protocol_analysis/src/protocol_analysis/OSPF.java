package protocol_analysis;

import method.Method;

public class OSPF {
	
	Method method = new Method();

	private String ospf = "";
	private int verSize = 2, typeSize = 2, optionsSize = 2, routerPriSize = 2, dbdOptionSize = 2, dbDescriptionSize = 2;
	private int packetLenSize = 4, checksumSize = 4, authenticationTypeSize = 4, helloIntervalSize = 4, 
			interfaceMtuSize = 4;
	private int routerIdSize = 8, areaIdSize = 8, netMaskSize = 8, routerDeadIntervalSize = 8, desigRouterSize = 8, 
			backupDesigRouterSize = 8, dbSequenceSize = 8;
	private int authenticationSize = 16;
	
	public OSPF(String ip) {
		this.ospf = ip;
	}

	public void ospf() {
		System.out.println("\n3. OSPF");
		byte[] bytes = ospf.getBytes();
		
		if(bytes.length > 0) {
			System.out.println(" 1) [ OSPF Header ] ");
			// 1)
			String version = new String(bytes, 0, verSize);
			int versionTmp = Integer.parseInt(version, 16);
			System.out.println("  1-1) Version = " + version + " / version " + versionTmp);
			
			// 2)
			String type = new String(bytes, 2, typeSize);
			int typeTmp = Integer.parseInt(type, 16);
			System.out.print("  1-2) Type = " + type + "(" + typeTmp + ")" + " / ");
			
			switch(typeTmp) {
			// 이웃 라우터의 발견, 인접관계 확립 및 유지
			case 1 : 
				System.out.println("Hello Packet");
				break;
			// LSDB 정보 유지관리	
			case 2 : 
				System.out.println("Database Description Packet");
				break;
			case 3 : 
				System.out.println("Link State Request Packet");
				break;
			case 4 : 
				System.out.println("Link State Update Packet");
				break;
			case 5 : 
				System.out.println("Link State Acknowledgement");
				break;
			default : 
				System.out.println();
				break;
			}
			
			// 3)
			String packetLength = new String(bytes, 4, packetLenSize);
			int packetLengthTmp = Integer.parseInt(packetLength, 16);
			System.out.println("  1-3) Packet Length = " + packetLength + " / " + packetLengthTmp + " bytes");
			
			// 4)
			String routerID = new String(bytes, 8, routerIdSize);
			System.out.print("  1-4) Router ID(발신지 라우터 IP주소) = " + routerID + " / ");
			byte[] routerIdBytes = routerID.getBytes();
			String[] routerIdBits = new String[4];
			int[] routerIdBitsTmp = new int[4];
			method.toDecimal(routerIdBytes, routerIdBits, routerIdBitsTmp);
			
			// 5) OSPF 패킷을 생성, 발송하는 라우터가 속한 OSPF Area의 식별 ID
			String areaID = new String(bytes, 16, areaIdSize);
			System.out.print("  1-5) Area ID = " + areaID + " / ");
			byte[] areaIdBytes = areaID.getBytes();
			String[] areaIdBits = new String[4];
			int[] areaIdBitsTmp = new int[4];
			method.toDecimal(areaIdBytes, areaIdBits, areaIdBitsTmp);
			
			// 6) 
			String checksum = new String(bytes, 24, checksumSize);
			System.out.println("  1-6) Checksum = " + checksum);
			
			// 7)
			String authenticationType = new String(bytes, 28, authenticationTypeSize);
			int authenticationTypeTmp = Integer.parseInt(authenticationType, 16);
			System.out.print("  1-7) Authentication Type = " + authenticationType + "(" + authenticationTypeTmp + ")");
			if(authenticationTypeTmp==0) System.out.println(" / NULL");
			else if(authenticationTypeTmp==1) System.out.println(" / Simple Password Authentication");
			else if(authenticationTypeTmp==2) System.out.println(" / MD5"); // MD는 데이터 무결성 위해 메세지 압축/축약을 하는 해쉬 알고리즘
			else System.out.println();
			
			// 8) 
			String authentication = new String(bytes, 32, authenticationSize);
			System.out.println("  1-8) Authentication = " + authentication);
			// OSPF Header의 Message Type에 따라서...
			// Hello Packet
			if(typeTmp==1) {
				System.out.println(" 2) [ OSPF Hello Packet ] ");
				// 2-1)
				String networkMask = new String(bytes, 48, netMaskSize);
				System.out.print("  2-1) Network Mask = " + networkMask + " / ");
				byte[] netMaskByte = networkMask.getBytes();
				String[] netMaskBits = new String[4];
				int[] netMaskBitsTmp = new int[4];
				method.toDecimal(netMaskByte, netMaskBits, netMaskBitsTmp);
				
				// 2-2)
				String helloInterval = new String(bytes, 56, helloIntervalSize);
				int helloIntervalTmp = Integer.parseInt(helloInterval, 16);
				System.out.println("  2-2) Hello Interval = " + helloInterval + " / " + helloIntervalTmp + "[sec]");
				
				// 2-3)
				String option = new String(bytes, 60, optionsSize);
				// 10진수 값을 각각 2진수로 변환하기 위함 (ex: 01 --> 0000 0001)
				byte[] optionsBytes = option.getBytes();
				String[] options = new String[2];
				int[] optionsTmp = new int[2];
				String[] optionsTmpBinary = new String[2];
				System.out.print("  2-3) Options = " + option + " / ");
				method.toBinary(optionsBytes, options, optionsTmp, optionsTmpBinary);
				System.out.println();
				// options의 8비트 중 6비트는 사용 X, 뒤의 2비트만 사용
				byte[] optionsFlagByte2 = optionsTmpBinary[1].getBytes();
				String eFlag = new String(optionsFlagByte2, 2, 1); // E 비트
				System.out.print("\t- External Routing = " + eFlag);
				if(eFlag.equals("1")) // 스터브 영역이므로 굳이 외부 정보가 필요 없음
					System.out.println(" / Capable!");
				else System.out.println(" / Incapable...");
				String tFlag = new String(optionsFlagByte2, 3, 1); // T 비트
				System.out.print("\t- Multi-Topology Routing = " + tFlag);
				if(tFlag.equals("1")) // 라우터가 다중 메트릭을 지원
					System.out.println(" / Yes(다중 메트릭 지원)");
				else System.out.println(" / No");
				
				// 2-4)
				String routerPrioirty = new String(bytes, 62, routerPriSize);
				int routerPrioirtyTmp = Integer.parseInt(routerPrioirty, 16);
				System.out.println("  2-4) Router Prioirty = " + routerPrioirty + "(" + routerPrioirtyTmp + ")");
				
				// 2-5) 라우터가 죽은 것으로 선언하기까지의 시간(Hello Interval의 4배가 기본)
				String routerDeadInterval = new String(bytes, 64, routerDeadIntervalSize);
				int routerDeadIntervalTmp = Integer.parseInt(routerDeadInterval, 16);
				System.out.println("  2-5) Router Dead Interval = " + routerDeadInterval + " / " + routerDeadIntervalTmp + "[sec]");
				
				// 2-6)
				String designatedRouter = new String(bytes, 72, desigRouterSize);
				System.out.print("  2-6) Designated Router = " + designatedRouter + " / ");
				byte[] desigRouterBytes = designatedRouter.getBytes();
				String[] desigRouterBits = new String[4];
				int[] desigRouterBitsTmp = new int[4];
				method.toDecimal(desigRouterBytes, desigRouterBits, desigRouterBitsTmp);
				
				// 2-7)
				String backupDesigRouter = new String(bytes, 80, backupDesigRouterSize);
				System.out.print("  2-7) Backup Designated Router = " + backupDesigRouter + " / ");
				byte[] backupDesigRouterBytes = backupDesigRouter.getBytes();
				String[] backupDesigRouterBits = new String[4];
				int[] backupDesigRouterBitsTmp = new int[4];
				method.toDecimal(backupDesigRouterBytes, backupDesigRouterBits, backupDesigRouterBitsTmp);
			}
			// DB Description
			else if(typeTmp==2) {
				System.out.println(" 2) [ OSPF DB Description ] ");
				// 2-1)
				String interfaceMtu = new String(bytes, 48, interfaceMtuSize);
				int interfaceMtuTmp = Integer.parseInt(interfaceMtu, 16);
				System.out.println("  2-1) Interface MTU = " + interfaceMtu + " / " + interfaceMtuTmp);
				
				// 2-2)
				String options = new String(bytes, 52, dbdOptionSize);
				System.out.print("  2-2) Options = " + options + " / ");
				byte[] dbOptionsBytes = options.getBytes();
				String[] dbOptions = new String[2];
				int[] dbOPtionsTmp = new int[2];
				String[] dbOptionsBinary = new String[2];
				method.toBinary(dbOptionsBytes, dbOptions, dbOPtionsTmp, dbOptionsBinary);
				System.out.println();
				// 8비트 중 6비트 사용 X, 뒤의 2비트 사용
				byte[] dboptionsFlagBytes = dbOptionsBinary[1].getBytes();
				String dbEFlag = new String(dboptionsFlagBytes, 2, 1); // E 비트
				System.out.print("\t- External Routing = " + dbEFlag);
				if(dbEFlag.equals("1")) System.out.println(" / Capable");
				else System.out.println(" / Incapable");
				String dbTFlag = new String(dboptionsFlagBytes, 3, 1); // T 비트
				System.out.print("\t- Multi-Topology Routing = " + dbTFlag);
				if(dbTFlag.equals("1")) System.out.println(" / Yes");
				else System.out.println(" / No");
				
				// 2-3)
				String dbDescription = new String(bytes, 54, dbDescriptionSize);
				System.out.print("  2-3) DB Description = " + dbDescription + " / ");
				byte[] dbDesBytes = dbDescription.getBytes();
				String[] dbDes = new String[2];
				int[] dbDesTmp = new int[2];
				String[] dbDesBinary = new String[2];
				method.toBinary(dbDesBytes, dbDes, dbDesTmp, dbDesBinary);
				System.out.println();
				// 8비트 중 뒤의 4비트만 사용(그 중에서도 첫번째 비트는 사용X)
				byte[] dbDescriptionFlagBytes = dbDesBinary[1].getBytes();
				String[] dbDescriptionFlag = new String[4];
				for(int i=0;i<dbDescriptionFlagBytes.length;i++)
					dbDescriptionFlag[i] = new String(dbDescriptionFlagBytes, i, 1);
				System.out.println("\t- Resync = " + dbDescriptionFlag[0] + " / Not Set"); // R 비트는 사용 X
				System.out.print("\t- Init = " + dbDescriptionFlag[1]); // I 비트
				if(dbDescriptionFlag[1].equals("1")) System.out.println(" / Set");
				else System.out.println(" / No");
				System.out.print("\t- More = " + dbDescriptionFlag[2]); // M 비트
				if(dbDescriptionFlag[2].equals("1")) System.out.println(" / Set");
				else System.out.println(" / No");
				System.out.print("\t- Master = " + dbDescriptionFlag[3]); // MS 비트
				if(dbDescriptionFlag[3].equals("1")) System.out.println(" / Yes");
				else System.out.println(" / No");
				// 2-4)
				String dbSequence = new String(bytes, 56, dbSequenceSize);
				System.out.println("  2-4) DB Sequence = " + dbSequence);
			}
			else if(typeTmp==3) System.out.println(" 2) [ Link State Request ] ");
			else if(typeTmp==4) System.out.println(" 2) [ Link State Update ] ");
			else if(typeTmp==5) System.out.println(" 2) [ Link State Acknowledgement ] ");
			else System.out.println();
		}
	}
}
