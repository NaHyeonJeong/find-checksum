package protocol_analysis;

import java.util.Scanner;

public class AnalysisTest {
	
	public static void main(String[] args) {
		// DB 연결
		new ServicesDB();
		// Ethernet Frame 입력 받기(16진수)
		Scanner input = new Scanner(System.in);
		System.out.print("Ethernet Frame Data를 입력하세요... >> ");
		String ethernetFrame = input.next();
		System.out.println("Protocol 별로 분석을 완료했습니다. 결과를 보여줍니다...>>");
		EthernetFormat ethernetFormat = new EthernetFormat(ethernetFrame);
		ethernetFormat.ethernet();
	}
}
