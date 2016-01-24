CitiSense GimVic CO2 Station Project
=============
Sources for CO2 monitor station and its serverside, created as a part of CitiSense projec on Gimnazija Vič. Licenced under <a href="https://github.com/The-C-Code/Raziskovalna/blob/master/LICENSE" target="_blank">the MIT licence</a>.

Client side design (case and circuit) is taken from another application and customized for our own needs. It includes a 11-digit LCD display and Rabbit 2000 microprocessor. CO2 and temperature sensors were added later on.

Main program for client is rabbit.c. All the others are quite usefull for developing and testing I2C protocol. To make I2C work with Rabbit 2000 SDA and SCL pins must be modified in its library (integrated in Dynamic C 9) to match physical pins on circuit.

Notes
=====
- Java and JDBC library is required for server to work.
- Server side uses mysql database to work. Its empyt backup lies somewhere in `Server` folder.
- Client side can only be modified/compiled/edited on windows (Wine and other patches won't work).
- Server side probably cannot be developed/compiled/run on Windows. However, congratulations if you manage to do it.

More detailed (Slovenian) discription provided <a href="https://docs.google.com/document/d/10DHLNeD8tyJaak70AY6y7xMuB0N7Gs9KO1u1_FFPgIU/edit?usp=sharing">here</a>.
