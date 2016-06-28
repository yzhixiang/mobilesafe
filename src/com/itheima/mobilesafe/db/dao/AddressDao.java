package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	
	public static final String path = "/data/data/com.itheima.mobilesafe/files/address.db";
	
	/**
	 * 查询号码的归属地
	 * @param number
	 * @return
	 */
	public static String getAddress(String number){
		String address = number;
		// 把address.db文件拷贝到 /data/data/包名/files/address.db
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		// 判断号码是手机号 还是 固定电话。
		// 11位 13x 15x 18x 14x 全部都是数字。
		if(number.matches("^1[3458]\\d{9}$")){ // 手机号码
			Cursor cursor = db.rawQuery(
					"select location from data2 where id = (select outkey from data1 where id = ?)", 
					new String[]{number.substring(0, 7)});
			if(cursor.moveToNext()){
				address = cursor.getString(0);
			}
			cursor.close();
		}else {
			//119 、110
			switch (number.length()) {
			case 3:
				address = "特殊号码";
				break;
			case 4://5556
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 7:
				address = "本地电话";
				break;
			case 8:
				address = "本地电话";
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
