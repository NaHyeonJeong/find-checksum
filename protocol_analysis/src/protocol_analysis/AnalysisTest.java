package protocol_analysis;

import java.util.Scanner;

public class AnalysisTest {
	
	public static void main(String[] args) {
		// DB ����
		new ServicesDB();
		// Ethernet Frame �Է� �ޱ�(16����)
		Scanner input = new Scanner(System.in);
		System.out.print("Ethernet Frame Data�� �Է��ϼ���... >> ");
		String ethernetFrame = input.next();
		System.out.println("Protocol ���� �м��� �Ϸ��߽��ϴ�. ����� �����ݴϴ�...>>");
		EthernetFormat ethernetFormat = new EthernetFormat(ethernetFrame);
		ethernetFormat.ethernet();
	}
}
