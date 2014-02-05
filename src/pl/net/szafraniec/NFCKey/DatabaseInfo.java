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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.util.Log;

/* Represents the on-disk database info including encrypted password */

public class DatabaseInfo {
	private static String decrypt_password(byte[] crypted_password, byte[] key)
			throws CryptoFailedException {
		byte[] decrypted;
		Cipher cipher = get_cipher(key, Cipher.DECRYPT_MODE);
		try {
			decrypted = cipher.doFinal(crypted_password);
		} catch (javax.crypto.IllegalBlockSizeException e) {
			Log.d(LOG_TAG, "IllegalBlockSize");
			throw new CryptoFailedException();
		} catch (javax.crypto.BadPaddingException e) {
			Log.d(LOG_TAG, "BadPadding");
			throw new CryptoFailedException();
		}

		int length = decrypted[0];
		if ((length < 0) || (length > NFCKEYSettings.max_password_length)) {
			Log.d(LOG_TAG, "BadPasswordLength:" + length);
			throw new CryptoFailedException();
		}
		return new String(decrypted, 1, length);
	}

	private static Cipher get_cipher(byte[] key, int mode)
			throws CryptoFailedException {
		try {
			SecretKeySpec sks = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance(CIPHER);
			// No IV as key is never re-used
			byte[] iv_bytes = new byte[cipher.getBlockSize()]; // zeroes
			IvParameterSpec iv = new IvParameterSpec(iv_bytes);
			cipher.init(mode, sks, iv);
			return cipher;
		} catch (java.security.NoSuchAlgorithmException e) {
			Log.d(LOG_TAG, "NoSuchAlgorithm");
			throw new CryptoFailedException();
		} catch (java.security.InvalidKeyException e) {
			Log.d(LOG_TAG, "InvalidKey");
			throw new CryptoFailedException();
		} catch (javax.crypto.NoSuchPaddingException e) {
			Log.d(LOG_TAG, "NoSuchPadding");
			throw new CryptoFailedException();
		} catch (java.security.InvalidAlgorithmParameterException e) {
			Log.d(LOG_TAG, "InvalidAlgorithmParameter");
			throw new CryptoFailedException();
		}
	}

	public String database;
	public String keyfile_filename;
	public String password;
	public int config;

	private static final String CIPHER = "AES/CTR/NoPadding";

	public static final String LOG_TAG = "nfckey";

	public static DatabaseInfo deserialise(Context ctx, byte[] key)
			throws CryptoFailedException {
		int config = NFCKEYSettings.CONFIG_NOTHING;
		String database = null, keyfile = null, password;
		byte[] buffer = new byte[1024];
		byte[] encrypted_password = new byte[NFCKEYSettings.max_password_length];
		FileInputStream nfcinfo = null;
		try {
			nfcinfo = ctx
					.openFileInput(NFCKEYSettings.nfcinfo_filename_template
							+ "_00.txt");
		} catch (FileNotFoundException e) {
			Log.w(LOG_TAG, "DatabaseNotFound");
			e.printStackTrace();
			return new DatabaseInfo(null, null, null, 0);
		}

		try {
			config = nfcinfo.read();
			database = read_string(nfcinfo, buffer);
			keyfile = read_string(nfcinfo, buffer);
			read_bytes(nfcinfo, encrypted_password);
		} catch (Exception e) {
			Log.w(LOG_TAG, "Exception:nfcinfo.read");
			e.printStackTrace();
			return new DatabaseInfo(null, null, null, 0);
		}
		password = decrypt_password(encrypted_password, key);

		return new DatabaseInfo(database, keyfile, password, config);
	}

	private static int read_bytes(FileInputStream fis, byte[] buffer)
			throws IOException {
		int length = read_short(fis);
		fis.read(buffer, 0, length);
		return length;
	}

	private static short read_short(FileInputStream fis) throws IOException {
		byte[] bytes = new byte[2];
		short[] shorts = new short[1];
		fis.read(bytes, 0, 2);
		ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
				.get(shorts);
		return shorts[0];
	}

	private static String read_string(FileInputStream fis, byte[] buffer)
			throws IOException {
		int length = read_bytes(fis, buffer);
		return new String(buffer, 0, length);
	}

	public DatabaseInfo(String database, String keyfile_filename,
			String password, int config) {
		this.database = database;
		this.keyfile_filename = keyfile_filename;
		this.password = password;
		this.config = config;
	}

	private byte[] encrypt_password(byte[] key) throws CryptoFailedException

	{
		int i;
		int idx = 0;
		byte[] padded_password = new byte[NFCKEYSettings.max_password_length];
		byte[] plaintext_password = password.getBytes();
		SecureRandom rng = new SecureRandom();
		// Password length...
		padded_password[idx++] = (byte) password.length();
		// ... and password itself...
		for (i = 0; i < plaintext_password.length; i++)
			padded_password[idx++] = plaintext_password[i];
		// ... and random bytes to pad.
		while (idx < padded_password.length)
			padded_password[idx++] = (byte) rng.nextInt();
		// Encrypt everything
		Cipher cipher = get_cipher(key, Cipher.ENCRYPT_MODE);
		try {
			return cipher.doFinal(padded_password);
		} catch (javax.crypto.IllegalBlockSizeException e) {
			Log.d(LOG_TAG, "IllegalBlockSize");
			throw new CryptoFailedException();
		} catch (javax.crypto.BadPaddingException e) {
			Log.d(LOG_TAG, "BadPadding");
			throw new CryptoFailedException();
		}
	}

	public boolean serialise(Context ctx, byte[] key)
			throws CryptoFailedException {
		/*
		 * Encrypt the data and store it on the Android device.
		 * 
		 * The encryption key is stored on the NFC tag.
		 */
		byte[] encrypted_password = encrypt_password(key);
		FileOutputStream nfcinfo;
		try {
			nfcinfo = ctx.openFileOutput(
					NFCKEYSettings.nfcinfo_filename_template + "_00.txt",
					Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			Log.w(LOG_TAG, "DatabaseNotFound");
			e.printStackTrace();
			return false;
		}
		try {
			nfcinfo.write(config);
			nfcinfo.write(to_short(database.length()));
			nfcinfo.write(database.getBytes());
			nfcinfo.write(to_short(keyfile_filename.length()));
			nfcinfo.write(keyfile_filename.getBytes());
			nfcinfo.write(to_short(encrypted_password.length));
			nfcinfo.write(encrypted_password);
			nfcinfo.close();
		} catch (IOException e) {
			Log.w(LOG_TAG, "IOException while writing database file");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private byte[] to_short(int i) {
		return to_short((short) i);
	}

	private byte[] to_short(short i) {
		byte[] bytes = new byte[2];
		short[] shorts = { i };
		ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
				.put(shorts);
		return bytes;
	}
}
