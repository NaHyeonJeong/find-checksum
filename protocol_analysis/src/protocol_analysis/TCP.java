package protocol_analysis;

import method.Method;

public class TCP {
	
	Method method = new Method();
	private String tcp = "";
	private int sourcePSize = 4, destinationPSize = 4, winSize = 4, checkSumSize = 4, urgentPSize = 4;
	private int seqNumSize = 8, ackNumSize = 8;
	private int headLenSize = 1;
	private int controlSize = 2;
	private int optionSize = 0;
	
	public TCP(String ip) {
		this.tcp = ip;
	}

	public void tcp() {
		System.out.println("\n3. TCP (Layer 4)");
		byte[] bytes = tcp.getBytes();

		if(bytes.length > 8) {
			// 1)
			String sourcePort = new String(bytes, 0, sourcePSize);
			int sourcePortTmp = Integer.parseInt(sourcePort, 16);
			System.out.print(" 1) Source Port = " + sourcePort + " / ");
			method.portInfo(sourcePortTmp);			
			
			// 2)
			String destinationPort = new String(bytes, 4, destinationPSize);
			int destinationPortTmp = Integer.parseInt(destinationPort, 16);
			System.out.print(" 2) Destination Port = " + destinationPort + " / " + destinationPortTmp + " ");
			method.portInfo(destinationPortTmp);
			
			// 3)
			String seqNumber = new String(bytes, 8, seqNumSize);
			System.out.println(" 3) Sequence Number = " + seqNumber);

			// 4)
			String ackNumber = new String(bytes, 16, ackNumSize);
			System.out.println(" 4) ACK Number = " + ackNumber);
			
			// 5)
			String headerLength = new String(bytes, 24, headLenSize); 
			int headerLenTmp = Integer.parseInt(headerLength, 16) * 4;
			optionSize = headerLenTmp - 20;
			System.out.println(" 5) Header Length = " + headerLength + " / " + headerLenTmp + " bytes : option " + optionSize + " bytes");
			
			// 6)
			String controlBits = new String(bytes, 26, controlSize); 
			byte[] controlBitsBytes = controlBits.getBytes();
			String[] count = new String[2];
			int[] countTmp = new int[2];
			String[] controlBitsBinary = new String[2];
			
			byte[] fragmentBytes1 = null;
			byte[] fragmentBytes2 = null;
			
			System.out.print(" 6) Control Bits = " + controlBits + " / " );
			if(controlBitsBytes.length > 0) {	
				for(int i=0;i<count.length;i++) {
					count[i] = new String(controlBitsBytes, i, 1);
					countTmp[i] = Integer.parseInt(count[i], 16);
					controlBitsBinary[i] = Integer.toBinaryString(countTmp[i]);
					if(controlBitsBinary[i].length() < 4) controlBitsBinary[i] = "000".substring(controlBitsBinary[i].length() - 1) + controlBitsBinary[i];
					System.out.print(controlBitsBinary[i] + " ");
				}
			}
			fragmentBytes1 = controlBitsBinary[0].getBytes(); // 쪼개서 세분화 하기 위해 미리 담아둠
			fragmentBytes2 = controlBitsBinary[1].getBytes();
			System.out.println();

			// fragmentBytes1
			String reserveBit = new String(fragmentBytes1, 0, 2);
			System.out.println("\t- Reserved Bits = " + reserveBit + " : 예비 필드");
			String urgentBit = new String(fragmentBytes1, 2, 1);
			System.out.print("\t- Urgent = " + urgentBit);
			if(urgentBit.equals("0")) System.out.println(" / Not Urgent");
			else System.out.println(" / Urgent pointer 필드 값 유효! 순서에 상관 없이 먼저 송신");
			String ackBit = new String(fragmentBytes1, 3, 1);
			System.out.print("\t- ACK = " + ackBit);
			if(ackBit.equals("1")) System.out.println(" / Acknowledgment 유효. 연결 설정(ACK 세그먼트)");
			else System.out.println(" / Acknowledgment 미포함");
			// fragmentBytes2
			String[] fragmentBytes2ToBits = new String[4];
			for(int i=0;i<fragmentBytes2ToBits.length;i++)
				fragmentBytes2ToBits[i] = new String(fragmentBytes2, i, 1);
			// fragmentBytes2ToBits[0] = Push
			System.out.print("\t- Push = " + fragmentBytes2ToBits[0]); 
			if(fragmentBytes2ToBits[0].equals("1")) System.out.println(" / Push 가능");
			else System.out.println(" / Normal");	
			// fragmentBytes2ToBits[1] = Reset
			System.out.print("\t- Reset = " + fragmentBytes2ToBits[1]); 
			if(fragmentBytes2ToBits[1].equals("1"))System.out.println(" / 강제 리셋");
			else System.out.println(" / Normal");
			// fragmentBytes2ToBits[2] = Sync
			System.out.print("\t- Sync = " + fragmentBytes2ToBits[2]); 
			if(fragmentBytes2ToBits[2].equals("1")) System.out.println(" / Connection Setup");
			else System.out.println(" / Normal");
			// fragmentBytes2ToBits[3] = Fin
			System.out.print("\t- Fin = " + fragmentBytes2ToBits[3]);  
			if(fragmentBytes2ToBits[3].equals("1")) System.out.println(" / 더이상 보낼 데이터 없음. 종결 요청(FIN 세그먼트)");
			else System.out.println(" / 보낼 데이터 더 있음.");
			
			if(fragmentBytes2ToBits[2].equals("1") && ackBit.equals("0")) System.out.println("\t- 연결 요청(SYNC 세그먼트)");
			else if(fragmentBytes2ToBits[2].equals("1") && ackBit.equals("1")) System.out.println("\t- 연결 허락(SYNC + ACK 세그먼트)");
			else if(fragmentBytes2ToBits[3].equals("1") && ackBit.equals("1")) System.out.println("\t- 종결 응답(FIN + ACK 세그먼트)");
			else System.out.println();
			
			// 7)
			String windowSize = new String(bytes, 28, winSize);
			int windowTmp = Integer.parseInt(windowSize, 16);
			System.out.println(" 7) Window Size = " + windowSize + " / " + windowTmp + " bytes");
			
			// 8)
			String checkSum = new String(bytes, 32, checkSumSize);
			System.out.println(" 8) Checksum = " + checkSum);
			
			// 9)
			String urgentPoint = new String(bytes, 36, urgentPSize);
			int urgentPointTmp = Integer.parseInt(urgentPoint, 16);
			System.out.print(" 9) Urgent Point = " + urgentPoint);
			if(urgentPointTmp!=0) System.out.println(" / Urgent Exist");
			else System.out.println(" / Not Urgent");
		}

		if(bytes.length > optionSize) {
			String option = new String(bytes, 40, optionSize*2);
			System.out.println("10) Option = " + option + " / " + optionSize + " bytes");
		}
	}

}
