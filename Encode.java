import java.util.*;
import java.lang.Math;

class Encode{

	public static int[][] stateArray;
	public static int[][] keyArray;

	public static int[][] sBox = {
		{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76}, 
		{0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0}, 
		{0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15}, 
		{0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75}, 
		{0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84}, 
		{0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf}, 
		{0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8}, 
		{0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2}, 
		{0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73}, 
		{0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb}, 
		{0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79}, 
		{0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08}, 
		{0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a}, 
		{0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e}, 
		{0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf}, 
		{0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
	}; 


	//Constructor to encode this
	Encode(int[][] s, int[][] k){
		//constructor receives original state and key arrays
		this.stateArray = s;
		this.keyArray = k;
		System.out.println("Encode!\n");
	}


	//==================================subBytes=====================
	public static void subBytes(){

		for(int row = 0; row < 4; row++){
			for(int column = 0; column < 4; column++){

				String hexStr = String.format("%x",stateArray[row][column]).toString();
				//System.out.println("hexstr = " + hexStr);
				char[] charLookup = hexStr.toCharArray();
				//Lookup
				if (charLookup.length == 1){
					//System.out.println("0 " + charLookup[0]);
					int j = Character.getNumericValue(charLookup[0]);
					stateArray[row][column] = sBox[0][j];
				}
				else {
					//System.out.println(charLookup[0] + " " + charLookup[1]);
					int i = Character.getNumericValue(charLookup[0]);
					int j = Character.getNumericValue(charLookup[1]);
					//System.out.println("i = " + i + ", j = " + j);
					//System.out.println("stateArray["+row+"]["+column+"] = " + String.format("%x",sBox[i][j]).toString());
					stateArray[row][column] = sBox[i][j];
				}

			}
		}
		printState();
		System.out.println("SubBytes!");
	}


	//==================================shiftRows=====================
	public static void shiftRows(){
		int offset = 0;
		int[] temp = new int[4];
		int t;
		for(int row = 0; row < 4; row++){
			//collect current values of this row's columns
			temp[0] = stateArray[row][0];
			temp[1] = stateArray[row][1];
			temp[2] = stateArray[row][2];
			temp[3] = stateArray[row][3];
			for(int column = 0; column < 4; column++){
				t = (column-offset)%4;
				if (Math.signum(t) == -1){
					t=4+t;
				}
				stateArray[row][t] = temp[column];
			}
			//offset is incremented after each row
			offset++;
		}

		printState();
		System.out.println("ShiftRows!");
	}


	//==================================mixColumns=====================
	public static void mixColumns(){
		System.out.println("mixColumns!");
	}


	//==================================addRoundKey=====================
	public static void addRoundKey(int[][] keyArray){


		keyArray = nextRoundKey(keyArray);

		for(int column = 0; column < 4; column++){
			//Get column of interest from both key and state
			for(int row = 0; row < 4; row++){
				//XOR the current columns
				stateArray[row][column] = stateArray[row][column] ^ keyArray[row][column];
				//System.out.println("result["+row+"]["+column+"] = " + String.format("%x",stateArray[row][column]).toString());
			}
		}
		printState();
		System.out.println("addRoundKey!");
	}



	//==================================keyScheduling=====================

	public static int[][] rCon = {
		{0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};

	public static int[][] nextRoundKey(int[][] keyArray){
		int[] firstCol = new int[4];
		int[] prevLastCol = new int[4];

		for (int column= 0; column < 4; column++){
			firstCol[column] = keyArray[column][1];
			prevLastCol[column] =keyArray[column][3]; 
		}

		makeFirstColumn(firstCol, prevLastCol);

		return keyArray;
	}

	public static void makeFirstColumn(int[] firstCol, int[] prevLastCol){

		//rotate last column of previous key to create RotWord
		for(int i = 0; i < 4; i++){
			if(i==3)
				firstCol[i] = prevLastCol[0];
			else
				firstCol[i] = prevLastCol[i + 1];
			System.out.println("firstCol["+i+"] = prevLastCol["+ (i+1)+"]");
		}

		//subBytes of the RotWord
		for(int k = 0; k < 4; k++){

			String hexStr = String.format("%x",firstCol[k]).toString();
			//System.out.println("hexstr = " + hexStr);
			char[] charLookup = hexStr.toCharArray();
			//Lookup
			if (charLookup.length == 1){
				//System.out.println("0 " + charLookup[0]);
				int j = Character.getNumericValue(charLookup[0]);
				firstCol[k] = sBox[0][j];
			}
			else {
				//System.out.println(charLookup[0] + " " + charLookup[1]);
				int i = Character.getNumericValue(charLookup[0]);
				int j = Character.getNumericValue(charLookup[1]);
				//System.out.println("i = " + i + ", j = " + j);
				//System.out.println("firstCol["+k+"] = " + String.format("%x",sBox[i][j]).toString());
				firstCol[k] = sBox[i][j];
			}
		}

		// XOR first column of previous key & Rotword & Rcon
		for(int k = 0; k < 4; k++){
			//XOR the current columns
			firstCol[k] = firstCol[k] ^ keyArray[k][1] ^ rCon[k][1];
			// System.out.println("result["+row+"]["+column+"] = " + String.format("%x",stateArray[row][column]).toString());
		}
	}

	public static void printState(){
		for(int row = 0; row < 4; row++){
			for(int column = 0; column < 4; column++){
				String hexStr = String.format("%x",stateArray[row][column]).toString();
				System.out.print(hexStr + " ");
			}
			System.out.println("");
		}
	}
}