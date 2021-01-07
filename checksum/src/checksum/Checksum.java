package checksum;

import java.util.Arrays;
import java.util.Scanner;

public class Checksum {

	public static void main(String[] args) {
		// 1)�Է�: ���� �̸��� �ּ�
		Scanner input = new Scanner(System.in);
		System.out.println("1)�Է�: ���� �̸��� �ּ�");
		System.out.print("�Է��ϼ���>> ");
		String info = input.nextLine();
		System.out.println("�Է�>> " + info);
		
		// 2)4����Ʈ�� Parsing(0: Padding)
		int maxrow = 6;//��
		int maxcol = 4;//��
		int maxrow2 = maxrow+2;//16���� ��
		int maxcol2 = maxcol*2;//16���� ��
		
		char [][] dataString = new char[maxrow][maxcol];//�Է¹��� ���ڿ��� ���ڷ� �ɰ��� �迭�� �ֱ�
		for(char[] row: dataString) Arrays.fill(row, '0');//�� ������ '0'���� ä��
		//���ڸ� 10������ �ٲ� �Ŀ� 16������ ��ȯ�ؾ� ��
		int [][] dataInteger = new int[maxrow][maxcol];//���ڸ� int������ ��ȯ �Ŀ� �ӽ� ���� �迭
		for(int[] row: dataInteger) Arrays.fill(row, 0);
		char [][] dataHex = new char[maxrow2][maxcol2];//16������ char������ �ɰ��� ����(���� ���ؾ� �ϴϱ�)
		for(char[] row: dataHex) Arrays.fill(row, '0');
		
		System.out.println("\n2)4����Ʈ�� Parsing(0: Padding)");
		for(int row=0;row<maxrow;row++) {//�ִ� ���� ���� ���ڼ��� ������
			for(int col=0;col<maxcol;col++) {//4����Ʈ�� �ڸ�
				int pos = row*maxcol+col;//���� ��ġ�� ����(Ŀ�� ����� ����)
				if(info.length()>pos) {//���ڿ��� �� ���̰� ���� ��ġ�� ���� Ŭ �� ������?
					dataString[row][col] = info.charAt(pos);//�Է¹��� ���ڿ��� ���ڷ� ��ȯ�ؼ� ���� �迭�� �־���
					dataInteger[row][col] = (int)dataString[row][col];//���ڸ� int������ ��ȯ�ؼ� int�� �迭�� �ӽ÷� ����
					//int ���� 16������ ��ȯ�ؼ� �־���
					// 3)16������ ��ȯ 
					dataHex[row][col*2] = Integer.toHexString(dataInteger[row][col]).charAt(0);//¦����°ĭ
					dataHex[row][col*2+1] = Integer.toHexString(dataInteger[row][col]).charAt(1);//Ȧ����°ĭ
				}
			}
			System.out.println(dataString[row]);//�Է¹��� ���ڿ� �� �྿ ���
		}
		
		System.out.println("\n3)16������ ��ȯ");
		for(int row=0;row<maxrow;row++) {//16������ ��ȯ�� �� �� �྿ ���
			System.out.println(dataHex[row]);
		}
		
		int[] sumVal = new int[maxcol2];//�� ���� ���� ���� �����ϴ� �迭(16�������� int������ �ٲ㼭 ���ؾ� ��)
		Arrays.fill(sumVal, 0);//��ĭ�� 0���� ä��
		int[] carVal = new int[maxcol2];//ĳ�� ���� �����ϴ� �迭
		Arrays.fill(carVal, 0);
		
		char [] sumValHexQuot = new char[maxcol2];//������� 16������ �ٲ��� �����ϴ� �迭
		Arrays.fill(sumValHexQuot, '0');
		char [] sumValHex = new char[maxcol2];//carry���� 16������ �ٲ��� �����ϴ� �迭
		Arrays.fill(sumValHex, '0');
		char [] sumValHex2 = new char[maxcol2];
		Arrays.fill(sumValHex2, '0');

		int tempCarVal=0;//carry���� �߻��ϸ� �ӽ� ����(�ʱⰪ�� 0)
		//�� ���� ���ؾ� ��, �ڿ��� ���� ���ذ�����
		for(int col=maxcol2-1;col>=0;col--) {
			for(int row=0;row<maxrow;row++)	{//16���� ���� int������ �ٲ㼭 �迭�� ����
				sumVal[col] += Integer.parseInt(Character.toString(dataHex[row][col]), 16);
			}
			if (col > 0) {//�� ���� 0���� Ŭ �� ������ ĳ�� ���� ���Ѵ�.
				carVal[col-1] = (sumVal[col] + carVal[col]) / 16;
			} 
			else if (col==0) {//������ ���� carry���� ��ü ���� ������ �ڸ��� ���ؾ� �ϹǷ� ���� �����ؾ� �Ѵ�.
				tempCarVal = (sumVal[col] + carVal[col]) / 16;
			}
			sumVal[col] += carVal[col];//�������� ���� carry���� ���������� �հ迡 �����ش�
			//���� ���� 16���� ���ڷ� �ٲ���
			sumValHexQuot[col] = Integer.toHexString(sumVal[col] / 16).charAt(0);
			sumValHex[col] = Integer.toHexString(sumVal[col]%16).charAt(0);
		}
		System.out.println(String.copyValueOf(sumValHex));//�� ���� ���� ���� ������
		System.out.println(String.copyValueOf(sumValHexQuot));//�� ���� �������� �߻��ϴ� carry���� ������
		
		System.out.println("\n5)Verification");
		for(int row=0;row<maxrow;row++) {
			System.out.println(dataHex[row]);
		}
		char [] sumValHexCompl = new char[maxcol2];//�� ���� ���� ������ 16������ �ٲ� �� �����ϴ� �迭
		Arrays.fill(sumValHexCompl, '0');
		for(int col=maxcol2-1;col>=0;col--) {//���� ������ �����ش�
			int tempIntVal = Integer.parseInt(Character.toString(sumValHex[col]), 16) + tempCarVal;//ĳ�� ��(�ʱⰪ�� 0)�� �����־� �Ѵ�.
			tempCarVal = tempIntVal / 16;//carry���� ���� ���� ���� ���� 16���� ���� ���̴�.
			sumValHex2[col] = Integer.toHexString(tempIntVal%16).charAt(0);//���� ���� 16���� ���ڷ� �ٲ㼭 �迭�� ����
			
			// 4)checksum 1�� ����ȭ(0 > 1, 1 > 0)
			String kk=Integer.toBinaryString(Integer.parseInt(Character.toString(sumValHex2[col]), 16));
			kk= String.format("%1$4s",kk).replace(' ', '0');//�ڸ����� ����� �ϴϱ� ��ĭ�� 0���� ä���ֱ�
			//�� 2���� ���ÿ� ������ �Ұ��ϹǷ� �߰��� �ӽ� ������ �ٲ�ٰ� �������ֱ�
			kk= kk.replace('0', 'z');
			kk= kk.replace('1', '0');
			kk= kk.replace('z', '1');
			//2������ 16������ �ٲ��ֱ�
			sumValHexCompl[col] = Integer.toHexString(Integer.parseInt(kk, 2)).charAt(0);
		}
		// 5)Sum
		System.out.println(String.copyValueOf(sumValHexCompl));//���� 16���� �� ���
		
		char [] vrfySumValHexQuot = new char[maxcol2];//carry�� ���� 
		Arrays.fill(sumValHexQuot, '0');
		char [] vrfySumValHex = new char[maxcol2];
		Arrays.fill(sumValHex, '0');
		tempCarVal=0;
		for(int col=maxcol2-1;col>=0;col--) {//���� ���������� ������ carry���� tempCarVal�� ������ �־�� �ϹǷ� col�� 0�϶� ���� �ݺ���
			int iii = sumVal[col] + Integer.parseInt(Character.toString(sumValHexCompl[col]), 16);//16���� ���� int������ ��ȯ�ؼ� ���� ���ϱ�
			tempCarVal = iii/16;//carry���� �� ���� 16���� ���� ��
			vrfySumValHexQuot[col] = Integer.toHexString(iii/16).charAt(0);//���� carry
			vrfySumValHex[col] = Integer.toHexString(iii%16).charAt(0);//�������� ?��?
		}
		//���� ������� ���������� ����Ǿ� �ִ� carry���� sum���� ���� ���̴�.
		// 5)Sum
		int lastVrfy =Integer.parseInt(Character.toString(vrfySumValHex[maxcol2-1]), 16) + tempCarVal; 
		vrfySumValHex[maxcol2-1] = Integer.toHexString(lastVrfy).charAt(0);//int�� 16������ �ٽ� ��ȯ����
		System.out.println(String.copyValueOf(vrfySumValHex));//������ carry���� ���� ���� ��
	} // end of main()
} // end of checksum 
