# NFC Key

Use your NFC-equipped Android phone to unlock your KeePass database.
You can use any NFC Tag, including very popular Mifare Ultralight or Sony SmartTag.

![screenshot-1](https://raw2.github.com/mateusz-szafraniec/NFC-Key/master/screenshot-en/device-2014-02-07-003803.png) 
![screenshot-2](https://raw2.github.com/mateusz-szafraniec/NFC-Key/master/screenshot-en/device-2014-02-07-003908.png)
![screenshot-3](https://raw2.github.com/mateusz-szafraniec/NFC-Key/master/screenshot-en/device-2014-02-07-003840.png)

Security:

	NFC tag stores only random numbers, and the password is encrypted with those numbers (AES-128 algorithm), stored in protected 
	Android device memory. An attacker would have to scan(copy) your NFC tag and either steal and root your Android
	device 	to get your password. If you suspect that your NFC tag has been read, you can use NFC Key again to 
	re-write it with new random values, rendering the previous information useless.
	
Requires:

* A phone which can write to, and read from, NFC tags (such as the Nexus 4,Xperia S, Xperia Z, Galaxy S3)
* Keepass2Android online or/and
* Keepass2Android offline or/and
* KeePassDroid

## Build

1. ``ant debug``

## Credits 
Developed by Mateusz Szafraniec (https://github.com/mateusz-szafraniec)

Based on conception and code KeePassNFC by Nicholas FitzRoy-Dale (https://github.com/nfd/KeePassNFC)

# Licenses

NFCKey:
	Copyright (C) 2014 Mateusz Szafraniec
	
	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA<br>
	
	Niniejszy program jest wolnym oprogramowaniem; możesz go
	rozprowadzać dalej i/lub modyfikować na warunkach Powszechnej
	Licencji Publicznej GNU, wydanej przez Fundację Wolnego
	Oprogramowania - według wersji 2 tej Licencji lub (według twojego
	wyboru) którejś z późniejszych wersji.
	
	Niniejszy program rozpowszechniany jest z nadzieją, iż będzie on
	użyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyślnej
	gwarancji PRZYDATNOŚCI HANDLOWEJ albo PRZYDATNOŚCI DO OKREŚLONYCH
	ZASTOSOWAŃ. W celu uzyskania bliższych informacji sięgnij do
	Powszechnej Licencji Publicznej GNU.
	
	Z pewnością wraz z niniejszym programem otrzymałeś też egzemplarz
	Powszechnej Licencji Publicznej GNU (GNU General Public License);
	jeśli nie - napisz do Free Software Foundation, Inc., 59 Temple
	Place, Fifth Floor, Boston, MA  02110-1301  USA

aFileChooser library:
   
    Copyright (C) 2011 - 2013 Paul Burke

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Portions of FileUtils.java:

    Copyright (C) 2007-2008 OpenIntents.org
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

LocalStorageProvider.java:

	Copyright (c) 2013, Ian Lake
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	- Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.	
