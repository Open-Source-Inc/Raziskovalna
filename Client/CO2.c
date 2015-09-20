#class auto

#use "i2c_devices.lib"

const char CO2_WR_ADDRESS = 0x2A;
const char CO2_RD_ADDRESS = 0x2B;
const char CO2_FUNCTION_CODE = 0x04;
char co2Data0, co2Data1, co2Data2, co2Data3;
char co2Status0, co2Status1, co2Status2, co2Status3;
int waitInt, result;

void wait(){
	for(waitInt = 0; waitInt < 2000; waitInt++){}
}


void printCo2SensorStatus(){
	i2c_start_tx();
	i2c_write_char(CO2_WR_ADDRESS);
	i2c_write_char(CO2_FUNCTION_CODE);
	i2c_write_char(0x13);
	i2c_write_char(0x8B);
	i2c_write_char(0);
	i2c_write_char(1);
	
	i2c_stop_tx();
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
	printf("%d, %d, %d, %d: ", co2Status0, co2Status1, co2Status2, co2Status3);
	result = 256*co2Status2 + co2Status3;
	printf("%d\n", result);
}

void main(){
	i2c_init();
	wait();
	wait();
	while(1){
		printCo2SensorStatus();
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
