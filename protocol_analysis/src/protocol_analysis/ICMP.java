package protocol_analysis;

public class ICMP {
	
	private String icmp = "";
	private int typeLen = 2, codeLen = 2;
	private int checksumLen = 4, identifierLen = 4, sequenceNumLen = 4;

	public ICMP(String ip) {
		this.icmp = ip;
	}

	public void icmp() {
		System.out.println("\n3. ICMP (Layer 3)");
		byte[] bytes = icmp.getBytes();

		if(bytes.length > 0) {
			// 1)
			String type = new String(bytes, 0, typeLen);
			int typeTmp = Integer.parseInt(type, 16);
			System.out.print(" 1) Type = " + type + " / " + typeTmp);
			if(typeTmp >= 0 && typeTmp < 128) {
				System.out.print(" : ICMP 오류 보고 메세지");
				// 주요 오류 보고(error-reporting) 메시지
				switch(typeTmp) {
				case 3 : 
					System.out.println(" >> Destination Unreachable 에러");
					break;
				case 4 : 
					System.out.println(" >> Source Quench 에러");
					break;
				case 5 : 
					System.out.println(" >> Redirect 에러");
					break;
				case 11 : 
					System.out.println(" >> Time Exceeded 에러");
					break;
				case 12 : 
					System.out.println(" >> Parameter Problem 에러");
					break;
				// 주요 조회(query) 메시지 : 이 경우에만 identifier, sequence number 존재
				case 0 : 
					System.out.println(" >> Echo Reply");
					break;
				case 8 : 
					System.out.println(" >> Echo Request");
					break;
				case 13 : 
					System.out.println(" >> Timestamp Request");
					break;
				case 14 : 
					System.out.println(" >> Timestamp Reply");
					break;
				case 17 : 
					System.out.println(" >> Address Mask Request");
					break;
				case 18 : 
					System.out.println(" >> Address Mask Reply");
					break;
				case 9 : 
					System.out.println(" >> Router Advertisement");
					break;
				case 10 : 
					System.out.println(" >> Router Solicitation");
					break;
				}
			}
			else if(typeTmp >= 128 && typeTmp < 256)
				System.out.println(" : ICMP 정보성 메세지");
			else System.out.println();

			// 2)
			String code = new String(bytes, 2, codeLen);
			System.out.println(" 2) Code = " + code);

			// 3)
			String checksum = new String(bytes, 4, checksumLen);
			System.out.println(" 3) Checksum = " + checksum);

			if(typeTmp==0 || typeTmp==8 || typeTmp==13 || typeTmp==14 || typeTmp==17 || typeTmp==18 || typeTmp==9 || typeTmp==10) {
				// 4)
				String identifier = new String(bytes, 8, identifierLen);
				System.out.println(" 4) Identifier = " + identifier);

				// 5)
				String sequenceNum = new String(bytes, 12, sequenceNumLen);
				System.out.println(" 5) Sequence Number = " + sequenceNum);	
			}

		}
	}
}
