#class auto

#use "i2c_devices.lib"

const char CO2_WR_ADDRESS = 0x2A;
const char CO2_RD_ADDRESS = 0x2B;
const char CO2_FUNCTION_CODE = 0x04;
const char CO2_FIRMWARE_ADD_MSB= 0x13;
const char CO2_FIRMWARE_ADD_LSB = 0x89;
const char CO2_STATUS_ADD_MSB= 0x13;
const char CO2_STATUS_ADD_LSB = 0x8A;
char co2Status0, co2Status1, co2Status2, co2Status3;
char co2Data0, co2Data1, co2Data2, co2Data3;
char tempChar0, tempChar1, tempChar2, tempChar3;
int waitInt, result, co2;

void wait(){
	for(waitInt = 0; waitInt < 20000; waitInt++){}
}

/*void printCo2SensorStatus(){
	i2c_startw_tx();
	i2c_write_char(CO2_WR_ADDRESS);
	i2c_write_char(CO2_FUNCTION_CODE);
	i2c_write_char(CO2_STATUS_ADD_MSB);
	i2c_write_char(CO2_STATUS_ADD_LSB);
	i2c_write_char(0);
	i2c_write_char(1);

	i2c_start_tx();
	i2c_write_char(CO2_RD_ADDRESS);
	i2c_read_char(&co2Status0);
	i2c_send_ack();
	i2c_read_char(&co2Status1);
	i2c_send_ack();
	i2c_read_char(&co2Status2);
	i2c_send_ack();
	i2c_read_char(&co2Status3);
	i2c_send_nak();
	i2c_stop_tx();
	printf("%d, %d, %d, %d\n", co2Status0, co2Status1, co2Status2, co2Status3);
}*/

/*a nasty and wrongly formated method, that turns out to
	make co2 sensor start responding right to i2c requests. Without
	running this method before reading co2, sensor will return only zeros.*/
/*void initCo2Com(){
	i2c_startw_tx();
	//i2c_write_char(CO2_WR_ADDRESS); - outcommented line that makes this method do its purpose
	i2c_write_char(CO2_FUNCTION_CODE);
	i2c_write_char(0x13);
	i2c_write_char(0x8B);
	i2c_write_char(0);
	i2c_write_char(1);

	i2c_start_tx();
	i2c_write_char(CO2_RD_ADDRESS);
	i2c_read_char(&tempChar0);
	i2c_send_ack();
	i2c_read_char(&tempChar1);
	i2c_send_ack();
	i2c_read_char(&tempChar2);
	i2c_send_ack();
	i2c_read_char(&tempChar3);
	i2c_send_nak();
	i2c_stop_tx();
	printf("temp: %d, %d, %d, %d\n", tempChar0, tempChar1, tempChar2, tempChar3);
	wait();
	wait();
	wait();
	wait();
	wait();
	wait();
}*/

int readCo2(){
	i2c_startw_tx();
	//i2c_wr_wait(CO2_WR_ADDRESS);
	i2c_write_char(CO2_WR_ADDRESS);
	i2c_write_char(CO2_FUNCTION_CODE);
	i2c_write_char(0x13);
	i2c_write_char(0x8B);
	i2c_write_char(0);
	i2c_write_char(1);

	i2c_start_tx();
	//i2c_wr_wait(CO2_RD_ADDRESS);
	i2c_write_char(CO2_RD_ADDRESS);
	i2c_read_char(&co2Data0);
	i2c_send_ack();
	i2c_read_char(&co2Data1);
	i2c_send_ack();
	i2c_read_char(&co2Data2);
	i2c_send_ack();
	i2c_read_char(&co2Data3);
	i2c_send_nak();
	i2c_stop_tx();
	printf("%d, %d, %d, %d\n", co2Data0, co2Data1, co2Data2, co2Data3);
	return 256 * (int) co2Data2 + (int) co2Data3;
}

void main(){
	i2c_init();
	wait();
	wait();
	while(1){
		co2 = readCo2();
		printf("%d\n", co2);
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
		wait();
	}
}
