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
* Ten plik jest czêœci¹ NFCKey.
*
* NFCKey jest wolnym oprogramowaniem; mo¿esz go rozprowadzaæ dalej
* i/lub modyfikowaæ na warunkach Powszechnej Licencji Publicznej GNU,
* wydanej przez Fundacjê Wolnego Oprogramowania - wed³ug wersji 2 tej
* Licencji lub (wed³ug twojego wyboru) którejœ z póŸniejszych wersji.
*
* Niniejszy program rozpowszechniany jest z nadziej¹, i¿ bêdzie on
* u¿yteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyœlnej
* gwarancji PRZYDATNOŒCI HANDLOWEJ albo PRZYDATNOŒCI DO OKREŒLONYCH
* ZASTOSOWAÑ. W celu uzyskania bli¿szych informacji siêgnij do
* Powszechnej Licencji Publicznej GNU.
*
* Z pewnoœci¹ wraz z niniejszym programem otrzyma³eœ te¿ egzemplarz
* Powszechnej Licencji Publicznej GNU (GNU General Public License);
* jeœli nie - napisz do Free Software Foundation, Inc., 59 Temple
* Place, Fifth Floor, Boston, MA  02110-1301  USA
*/
package pl.net.szafraniec.NFCKey;

public class Settings {
	public static final String nfc_mime_type = "application/x-nfck";
	public static final String nfc_mime_type_hidden = "text/plain";
	public static final String nfcinfo_filename_template = "nfcinfo";
	public static final int password_length = 22; // including length byte original was 33, ultraligth 22, 113 NTAG203
	public static final int index_length = 2; // Password filename number
	public static final int config_length = 1; // Config byte, currently set if ask for password
	public static final int random_bytes_length = password_length + config_length;
	public static final int nfc_length = index_length + random_bytes_length;
	public static final int CONFIG_NOTHING = 0;
	public static final int CONFIG_PASSWORD_ASK = 1;
}
