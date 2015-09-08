#class auto

#use "i2c_devices.lib"

//network configuration - this one includes DHCP and cannot be changed in runtime
#define TCPCONFIG 3

//server and port
#define SERVER "theccode.com"
#define PORT 80

//#use "net.lib"
#use "dcrtcp.lib"

//rabit's id
#define MY_ID "rabbit1"

//From left to right, the first one only has digit 2
const char maxAddresses[6] = {0xA0, 0xA2, 0xAC, 0xAE, 0xA8, 0xAA};
const char tempSensorAddress = 0x90;

const char testRegister = 0x07;
const char configRegister = 0x4;
const char intensity1Register = 0x01;
const char intensity2Register = 0x02;

//defaults
const int defaultIntensity = 15;
const int defaultColor = 1; //orange

//0=green, 1=orange, 2=red
int digitColors[11];

char data, temp, countPerC, countRemain;
char output[2048];
int i, j, address, color, waitInt, roundI, roundJ;
unsigned char readArray[2];
double temperature, temperatureRead, temperature2;
char colorString[1];
char intensityString[2];
char string[11];
int intensity;

void wait(){
	for(waitInt = 0; waitInt < 20000; waitInt++){}
}

void runTest(){
	data = 1;
	for(i = 0; i < sizeof(maxAddresses); i++){
		I2CWrite(maxAddresses[i], testRegister, &data, 1);
	}
	
	for(i = 0; i < 20; i++) wait();

	data = 0;
	for(i = 0; i < sizeof(maxAddresses); i++){
		I2CWrite(maxAddresses[i], testRegister, &data, 1);
	}
}

void wakeup(){
	data = 1;
	for(i = 0; i < sizeof(maxAddresses); i++){
		I2CWrite(maxAddresses[i], configRegister, &data, 1);
	}

	//wakeup for temperature sensor
	//puts it in one-shot mode
	I2CRead(tempSensorAddress, 0xAC, &data, 1);
	data = data | (char) 1;
	I2CWrite(tempSensorAddress, 0xAC, &data, 1);

	wait();
}

double readTemperature(){

	temperature = 0;
	for(i=0; i < 1; i++){

		//starts temperature conversion
		data = 0;
		I2CWrite(tempSensorAddress, 0xEE, &data, 0);

		//waits until DONE bit in config register is set to 1
		while(data<(char)128){
			I2CRead(tempSensorAddress, 0xAC, &data, 1);
		}

		I2CRead(tempSensorAddress, 0xAA, readArray, 2);
		temperatureRead = readArray[0];

		if(readArray[1] == 128) temperatureRead += 0.5;

		I2CRead(tempSensorAddress, 0xA9, &countPerC, 1);
		I2CRead(tempSensorAddress, 0xA8, &countRemain, 1);

		//if(i==0) printf("temperature:%f temperatureRead:%f countPerC:%d countRemain:%d\n", temperature, temperatureRead, countPerC, countRemain);
		//else printf("temperature:%f temperatureRead:%f countPerC:%d countRemain:%d\n", temperature/i, temperatureRead, countPerC, countRemain);

		temperature =  temperature + temperatureRead - 0.25 + (double)((double)(countPerC - countRemain) / (double)countPerC);
		//printf("temperature:%f temperatureRead:%f countPerC:%d countRemain:%d\n", temperature, temperatureRead, countPerC, countRemain);
		//wait();
	}
	
	temperature = temperature/1;


	return temperature;
}

//0=green, 1=orange, 2=red
void setAllDigitsColor(int color){
	for(i = 0; i < 11; i++){
		digitColors[i] = color;
	}
}


//sets whole display's intensity/brightnes
//intensity must be from 0 to 15
void setIntensity(char intensity){

	//for security reasons - ensures that intensity is in valid range
	if(intensity < 0) intensity = 0;
	if(intensity > 15) intensity = 15;

	data = intensity * 16 + intensity;

	for(i = 0; i < sizeof(maxAddresses); i++){
		I2CWrite(maxAddresses[i], intensity1Register, &data, 1);
		I2CWrite(maxAddresses[i], intensity2Register, &data, 1);
	}

}

void cleanup(){
	for(i = 0; i < sizeof(maxAddresses); i++){
		I2CWrite(maxAddresses[i], 0x60, " ", 1);
		I2CWrite(maxAddresses[i], 0x61, " ", 1);
		I2CWrite(maxAddresses[i], 0x62, " ", 1);
		I2CWrite(maxAddresses[i], 0x63, " ", 1);
	}
}

void initialize(){
	i2c_init();
	wakeup();
	cleanup();
	setIntensity(defaultIntensity);
	setAllDigitsColor(defaultColor);
	runTest();
	wait();
}


//void to render a ceretain char on a ceretain digit
void renderChar(char character, int digit){
	if(digit==1 || digit==2) address = maxAddresses[1];
	if(digit==3 || digit==4) address = maxAddresses[2];
	if(digit==5 || digit==6) address = maxAddresses[3];
	if(digit==7 || digit==8) address = maxAddresses[4];
	if(digit==9 || digit==10) address = maxAddresses[5];

	color = digitColors[digit];

	if(digit==0){
		if(color == 0){
			I2CWrite(maxAddresses[0], 0x62, " ", 1);
			I2CWrite(maxAddresses[0], 0x63, &character, 1);
		}else if(color == 1){
			I2CWrite(maxAddresses[0], 0x62, &character, 1);
			I2CWrite(maxAddresses[0], 0x63, &character, 1);
		}else if(color == 2){
			I2CWrite(maxAddresses[0], 0x62, &character, 1);
			I2CWrite(maxAddresses[0], 0x63, " ", 1);
		}
	}else{
		digit = digit % 2;
		if(digit==1){
			//if digit is the first one of the two, controlled by 1 maxim

			if(color == 0){
				I2CWrite(address, 0x60, " ", 1);
				I2CWrite(address, 0x61, &character, 1);
			}else if(color == 1){
				I2CWrite(address, 0x60, &character, 1);
				I2CWrite(address, 0x61, &character, 1);
			}else if(color == 2){
				I2CWrite(address, 0x60, &character, 1);
				I2CWrite(address, 0x61, " ", 1);
			}

		}else if(digit == 0){
			//if digit is the second one of the two, controlled by 1 maxim

			if(color == 0){
				I2CWrite(address, 0x62, " ", 1);
				I2CWrite(address, 0x63, &character, 1);
			}else if(color == 1){
				I2CWrite(address, 0x62, &character, 1);
				I2CWrite(address, 0x63, &character, 1);
			}else if(color == 2){
				I2CWrite(address, 0x62, &character, 1);
				I2CWrite(address, 0x63, " ", 1);
			}
		}
	}

}

void renderString(char string[], int stringColor){
	setAllDigitsColor(stringColor);

	j = 0;
	for(i = 0; i < 11; i++){
		if(j < strlen(string)) {
			if(string[j] == (char) 194) {
				renderChar((char) 27, i);
				j=j+2;
			}else if(string[j] == (char)38){
				renderChar((char) 27, i);
				j++;
			}else {
				renderChar(string[j], i);
				j++;
			}
		}else {
			renderChar(" "[0], i);
			j++;
		}
	}
}

void runWithServerProtocol(){
	int status;
	tcp_Socket socket;
	char buffer[16];
	longword ip;

	renderString("Init", 0);
	printf("Init\n");

	sock_init();
	while (ifpending(IF_DEFAULT) == IF_COMING_UP) {
		tcp_tick(NULL);
	}

	if( 0L == (ip = resolve(SERVER)) ) {
		renderString("DNS error", 2);
		printf("DNS error\n");
	}else{
		renderString("Resolved", 0);
		printf("Resolved\n");
	}

	renderString("Connecting", 0);
	printf("Connecting\n");

	tcp_open(&socket,0,ip,PORT,NULL);
	while(!sock_established(&socket) && sock_bytesready(&socket)==-1) {
		tcp_tick(NULL);
	}

	renderString("Connected", 0);
	printf("Connected\n");
	sock_mode(&socket,TCP_MODE_ASCII);

	wait();

	sock_puts(&socket, MY_ID);
	renderString(MY_ID, 1);

	while(tcp_tick(&socket)) {
		if((sock_bytesready(&socket) != -1) || (sock_bytesready(&socket) > 0)){
			sock_gets(&socket, buffer, 16);

			printf("recieved: %s\n", buffer);
		
			if(strlen(buffer) > 0){
				if(buffer[0] == (char) 116){
					printf("temperature pending\n");
					temperature2 = readTemperature();
					sprintf(output, "%f", temperature2);
					sock_puts(&socket, output);
					printf("temperature: %s\n", output);

				}else if(buffer[0] == "g"){
					sock_puts(&socket, "0");
				}else{
					colorString[0] = buffer[0];
					intensityString[0] = buffer[1];
					intensityString[1] = buffer[2];

					color = atoi(colorString);
					intensity = atoi(intensityString);

					for(i = 3;  i < strlen(buffer); i++){
						if(i-3 < 11){
							string[i-3] = buffer[i];
						}
					}

					for(; i < 14; i++){
						string[i - 3] = " "[0];
					}

					setIntensity(intensity);
					renderString(string, color);
					sock_puts(&socket, "1");
				}
			}

		}
	}
}

void main(){
	initialize();
	while(1){
		runWithServerProtocol();
	}
}
