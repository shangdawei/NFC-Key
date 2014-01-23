/**
* Copyright (C) 2014 Mateusz Szafraniec
* This file is part of NFCKey.
*
* NFCKey is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* NFCKey is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with NFCKey; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*
* Ten plik jest częścią NFCKey.
*
* NFCKey jest wolnym oprogramowaniem; możesz go rozprowadzać dalej
* i/lub modyfikować na warunkach Powszechnej Licencji Publicznej GNU,
* wydanej przez Fundację Wolnego Oprogramowania - według wersji 2 tej
* Licencji lub (według twojego wyboru) którejś z późniejszych wersji.
*
* Niniejszy program rozpowszechniany jest z nadzieją, iż będzie on
* użyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyślnej
* gwarancji PRZYDATNOŚCI HANDLOWEJ albo PRZYDATNOŚCI DO OKREŚLONYCH
* ZASTOSOWAŃ. W celu uzyskania bliższych informacji sięgnij do
* Powszechnej Licencji Publicznej GNU.
*
* Z pewnością wraz z niniejszym programem otrzymałeś też egzemplarz
* Powszechnej Licencji Publicznej GNU (GNU General Public License);
* jeśli nie - napisz do Free Software Foundation, Inc., 59 Temple
* Place, Fifth Floor, Boston, MA  02110-1301  USA
*/
package pl.net.szafraniec.NFCKey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import android.content.Context;

/* Represents the on-disk database info including encrypted password */

public class DatabaseInfo {
	public String database;
	public String keyfile_filename;
	public String password;
	public int config;
	
	public DatabaseInfo(String database, String keyfile_filename, String password, int config)
	{
		this.database = database;
		this.keyfile_filename = keyfile_filename;
		this.password = password;
		this.config = config;
	}
	
	private byte[] encrypt_password(byte[] encryption_bytes, int offset)
	{
		int i;
		int crypted_idx = 0;
		byte[] crypted_password = new byte[Settings.password_length];
		byte[] plaintext_password = password.getBytes();
		SecureRandom rng = new SecureRandom();		
		
		// Password length...
		crypted_password[crypted_idx ++] = (byte)password.length();
		// ... and password itself...
		for (i = 0; i < plaintext_password.length; i++) 
			crypted_password[crypted_idx ++] = plaintext_password[i];
		// ... and random bytes to pad.
		while (crypted_idx < crypted_password.length)
			crypted_password[crypted_idx++] = (byte)rng.nextInt();
		
		// Encrypt everything
		for (i = 0; i < Settings.password_length; i++)
			crypted_password[i] ^= encryption_bytes[i + offset];
		
		return crypted_password;
	}
	
	private static String decrypt_password(byte[] crypted_password, byte[] encryption_bytes, int offset)
	{
		int i, length;

		for (i = 0; i < Settings.password_length; i++)
			crypted_password[i] ^= encryption_bytes[i + offset];
		
		length = (int)crypted_password[0];
		return new String(crypted_password, 1, length);
	}
	
	private byte[] to_short(int i)
	{
		byte[] bytes = new byte[2];
		bytes[0] = (byte)((i & 0xff00) >> 8);
		bytes[1] = (byte)(i & 0xff);
		return bytes;
	}
	
	public boolean serialise(Context ctx, byte[] random_bytes)
	{
		byte encrypted_config;
		byte[] encrypted_password;
		
		encrypted_config = (byte)(((byte)config) ^ random_bytes[0]);
		encrypted_password = encrypt_password(random_bytes, 1);
				
		FileOutputStream nfcinfo;
		try {
			nfcinfo = ctx.openFileOutput(Settings.nfcinfo_filename_template + "_00.txt", Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		try {
			nfcinfo.write(encrypted_config);
			nfcinfo.write(to_short(database.length()));
			nfcinfo.write(database.getBytes());
			nfcinfo.write(to_short(keyfile_filename.length()));
			nfcinfo.write(keyfile_filename.getBytes());
			nfcinfo.write(to_short(encrypted_password.length));
			nfcinfo.write(encrypted_password);
			nfcinfo.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static DatabaseInfo deserialise(Context ctx, byte[] random_bytes, int offset)
	{
		int config = Settings.CONFIG_NOTHING;
		String database = null, keyfile = null, password;
		byte[] buffer = new byte[1024];
		byte[] encrypted_password = new byte[Settings.password_length];
		
		FileInputStream nfcinfo = null;
		
		try {
			nfcinfo = ctx.openFileInput(Settings.nfcinfo_filename_template + "_00.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//return null;
			return new DatabaseInfo(null, null, null, 0);
		}
		
		try {
			config = (((byte)nfcinfo.read()) ^ random_bytes[0 + offset]);
			database = read_string(nfcinfo, buffer);
			keyfile = read_string(nfcinfo, buffer);
			read_bytes(nfcinfo, encrypted_password);
		} catch (Exception e) {
			e.printStackTrace();
			return new DatabaseInfo(null, null, null, 0);
			//return null;
		}
		
		password = decrypt_password(encrypted_password, random_bytes, 1 + offset);
				
		return new DatabaseInfo(database, keyfile, password, config);
	}
	
	private static int read_short(FileInputStream fis, byte[] buffer) throws IOException
	{
		int i;
		
		fis.read(buffer, 0, 2);
		i = (int)(buffer[0] << 8);
		i |= (int)(buffer[1]);
		
		return i;
	}
	
	private static int read_bytes(FileInputStream fis, byte[] buffer) throws IOException
	{
		int length = read_short(fis, buffer);
		
		fis.read(buffer, 0, length);
		return length;		
	}
	
	private static String read_string(FileInputStream fis, byte[] buffer) throws IOException
	{
		int length = read_bytes(fis, buffer);
		return new String(buffer, 0, length);
	}
}
