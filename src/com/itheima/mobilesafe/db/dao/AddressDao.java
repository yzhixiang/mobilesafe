package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	
	public static final String path = "/data/data/com.itheima.mobilesafe/files/address.db";
	
	/**
	 * ��ѯ����Ĺ�����
	 * @param number
	 * @return
	 */
	public static String getAddress(String number){
		String address = number;
		// ��address.db�ļ������� /data/data/����/files/address.db
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		// �жϺ������ֻ��� ���� �̶��绰��
		// 11λ 13x 15x 18x 14x ȫ���������֡�
		if(number.matches("^1[3458]\\d{9}$")){ // �ֻ�����
			Cursor cursor = db.rawQuery(
					"select location from data2 where id = (select outkey from data1 where id = ?)", 
					new String[]{number.substring(0, 7)});
			if(cursor.moveToNext()){
				address = cursor.getString(0);
			}
			cursor.close();
		}else {
			//119 ��110
			switch (number.length()) {
			case 3:
				address = "�������";
				break;
			case 4://5556
				address = "ģ����";
				break;
			case 5:
				address = "�ͷ��绰";
				break;
			case 7:
				address = "���ص绰";
				break;
			case 8:
				address = "���ص绰";
				break;
			default:
				if(number.length()>10 && number.startsWith("0")){
					//010 12345678
					Cursor cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});
					if(cursor.moveToNext()){
						String location = cursor.getString(0);
						address = location.substring(0, location.length()-2);
					}
					cursor.close();
					
					//0855 12345678
					cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});
					if(cursor.moveToNext()){
						String location = cursor.getString(0);
						address = location.substring(0, location.length()-2);
					}
				}
				break;
			}
		}
		db.close();
		return address;
	}
	
}
