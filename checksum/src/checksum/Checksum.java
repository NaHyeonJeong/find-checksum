package checksum;

import java.util.Arrays;
import java.util.Scanner;

public class Checksum {

	public static void main(String[] args) {
		// 1)입력: 영문 이름과 주소
		Scanner input = new Scanner(System.in);
		System.out.println("1)입력: 영문 이름과 주소");
		System.out.print("입력하세요>> ");
		String info = input.nextLine();
		System.out.println("입력>> " + info);
		
		// 2)4바이트로 Parsing(0: Padding)
		int maxrow = 6;//행
		int maxcol = 4;//열
		int maxrow2 = maxrow+2;//16진수 행
		int maxcol2 = maxcol*2;//16진수 열
		
		char [][] dataString = new char[maxrow][maxcol];//입력받은 문자열을 문자로 쪼개서 배열에 넣기
		for(char[] row: dataString) Arrays.fill(row, '0');//빈 공간을 '0'으로 채움
		//문자를 10진수로 바꾼 후에 16진수로 변환해야 함
		int [][] dataInteger = new int[maxrow][maxcol];//문자를 int형으로 변환 후에 임시 저장 배열
		for(int[] row: dataInteger) Arrays.fill(row, 0);
		char [][] dataHex = new char[maxrow2][maxcol2];//16진수를 char형으로 쪼개서 저장(열을 더해야 하니깐)
		for(char[] row: dataHex) Arrays.fill(row, '0');
		
		System.out.println("\n2)4바이트로 Parsing(0: Padding)");
		for(int row=0;row<maxrow;row++) {//최대 저장 가능 글자수를 정해줌
			for(int col=0;col<maxcol;col++) {//4바이트씩 자름
				int pos = row*maxcol+col;//문자 위치에 따라(커서 비슷한 역할)
				if(info.length()>pos) {//문자열의 총 길이가 문자 위치값 보다 클 때 까지만?
					dataString[row][col] = info.charAt(pos);//입력받은 문자열을 문자로 변환해서 문자 배열에 넣어줌
					dataInteger[row][col] = (int)dataString[row][col];//문자를 int형으로 변환해서 int형 배열에 임시로 저장
					//int 값을 16진수로 변환해서 넣어줌
					// 3)16진수로 변환 
					dataHex[row][col*2] = Integer.toHexString(dataInteger[row][col]).charAt(0);//짝수번째칸
					dataHex[row][col*2+1] = Integer.toHexString(dataInteger[row][col]).charAt(1);//홀수번째칸
				}
			}
			System.out.println(dataString[row]);//입력받은 문자열 한 행씩 출력
		}
		
		System.out.println("\n3)16진수로 변환");
		for(int row=0;row<maxrow;row++) {//16진수로 변환한 값 한 행씩 출력
			System.out.println(dataHex[row]);
		}
		
		int[] sumVal = new int[maxcol2];//열 별로 합한 값을 저장하는 배열(16진수에서 int형으로 바꿔서 더해야 함)
		Arrays.fill(sumVal, 0);//빈칸은 0으로 채움
		int[] carVal = new int[maxcol2];//캐리 값을 저장하는 배열
		Arrays.fill(carVal, 0);
		
		char [] sumValHexQuot = new char[maxcol2];//결과값을 16진수로 바꾼후 저장하는 배열
		Arrays.fill(sumValHexQuot, '0');
		char [] sumValHex = new char[maxcol2];//carry값을 16진수로 바꾼후 저장하는 배열
		Arrays.fill(sumValHex, '0');
		char [] sumValHex2 = new char[maxcol2];
		Arrays.fill(sumValHex2, '0');

		int tempCarVal=0;//carry값이 발생하면 임시 보관(초기값은 0)
		//열 별로 더해야 함, 뒤에서 부터 더해갈거임
		for(int col=maxcol2-1;col>=0;col--) {
			for(int row=0;row<maxrow;row++)	{//16진수 값을 int형으로 바꿔서 배열에 누적
				sumVal[col] += Integer.parseInt(Character.toString(dataHex[row][col]), 16);
			}
			if (col > 0) {//열 값이 0보다 클 때 까지만 캐리 값을 구한다.
				carVal[col-1] = (sumVal[col] + carVal[col]) / 16;
			} 
			else if (col==0) {//마지막 열의 carry값은 전체 합의 마지막 자리와 더해야 하므로 따로 저장해야 한다.
				tempCarVal = (sumVal[col] + carVal[col]) / 16;
			}
			sumVal[col] += carVal[col];//마지막에 나온 carry값을 이전까지의 합계에 더해준다
			//나온 값을 16진수 문자로 바꿔줌
			sumValHexQuot[col] = Integer.toHexString(sumVal[col] / 16).charAt(0);
			sumValHex[col] = Integer.toHexString(sumVal[col]%16).charAt(0);
		}
		System.out.println(String.copyValueOf(sumValHex));//열 별로 더한 값을 보여줌
		System.out.println(String.copyValueOf(sumValHexQuot));//열 별로 더했을때 발생하는 carry값을 보여줌
		
		System.out.println("\n5)Verification");
		for(int row=0;row<maxrow;row++) {
			System.out.println(dataHex[row]);
		}
		char [] sumValHexCompl = new char[maxcol2];//열 별로 더한 값들을 16진수로 바꾼 후 저장하는 배열
		Arrays.fill(sumValHexCompl, '0');
		for(int col=maxcol2-1;col>=0;col--) {//뒤의 열부터 더해준다
			int tempIntVal = Integer.parseInt(Character.toString(sumValHex[col]), 16) + tempCarVal;//캐리 값(초기값은 0)도 구해주야 한다.
			tempCarVal = tempIntVal / 16;//carry값은 열의 값을 더한 것을 16으로 나눈 몫이다.
			sumValHex2[col] = Integer.toHexString(tempIntVal%16).charAt(0);//더한 값을 16진수 문자로 바꿔서 배열에 저장
			
			// 4)checksum 1의 보수화(0 > 1, 1 > 0)
			String kk=Integer.toBinaryString(Integer.parseInt(Character.toString(sumValHex2[col]), 16));
			kk= String.format("%1$4s",kk).replace(' ', '0');//자리수를 맞춰야 하니깐 빈칸은 0으로 채워주기
			//값 2개를 동시에 변경은 불가하므로 중간에 임시 값으로 바꿨다가 변경해주기
			kk= kk.replace('0', 'z');
			kk= kk.replace('1', '0');
			kk= kk.replace('z', '1');
			//2진수를 16진수로 바꿔주기
			sumValHexCompl[col] = Integer.toHexString(Integer.parseInt(kk, 2)).charAt(0);
		}
		// 5)Sum
		System.out.println(String.copyValueOf(sumValHexCompl));//합의 16진수 값 출력
		
		char [] vrfySumValHexQuot = new char[maxcol2];//carry값 저장 
		Arrays.fill(sumValHexQuot, '0');
		char [] vrfySumValHex = new char[maxcol2];
		Arrays.fill(sumValHex, '0');
		tempCarVal=0;
		for(int col=maxcol2-1;col>=0;col--) {//가장 마지막으로 나오는 carry값을 tempCarVal이 가지고 있어야 하므로 col이 0일때 까지 반복함
			int iii = sumVal[col] + Integer.parseInt(Character.toString(sumValHexCompl[col]), 16);//16진수 값을 int형으로 변환해서 열을 더하기
			tempCarVal = iii/16;//carry값은 열 합을 16으로 나눈 몫
			vrfySumValHexQuot[col] = Integer.toHexString(iii/16).charAt(0);//몫은 carry
			vrfySumValHex[col] = Integer.toHexString(iii%16).charAt(0);//나머지는 ?값?
		}
		//최종 결과값은 마지막으로 저장되어 있는 carry값과 sum값을 더한 값이다.
		// 5)Sum
		int lastVrfy =Integer.parseInt(Character.toString(vrfySumValHex[maxcol2-1]), 16) + tempCarVal; 
		vrfySumValHex[maxcol2-1] = Integer.toHexString(lastVrfy).charAt(0);//int를 16진수로 다시 변환해줌
		System.out.println(String.copyValueOf(vrfySumValHex));//마지막 carry값도 더한 최종 값
	} // end of main()
} // end of checksum 
