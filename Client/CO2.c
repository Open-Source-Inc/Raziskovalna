#class auto

#use "i2c_devices.lib"

/*char tempData1, tempData2, data1, data2;
int waitInt;*/

const char WR_ADDRESS = 0x2A;
const char RD_ADDRESS = 0x2B;
char add;
const char functionCode = 0x04;
const char msbAdd = 0x13;
const char lsbAdd = 0x8A;
char data1, data2, data3, data4;
int returnCode, i, waitInt;
char result;
char array[4];
int error;
const char ModBusRequest[3] = {0x13, 0x8A, 0x00};

void wait(){
	for(waitInt = 0; waitInt < 20000; waitInt++){}
}

void main(){
	i2c_init();
	wait();
	wait();

	add = 20;
while(1){
	//I2CWrite(WR_ADDRESS, functionCode, ModBusRequest, 3);
	//returnCode = I2CRead(WR_ADDRESS, functionCode, array, 4);
	i2c_startw_tx();
	i2c_write_char(WR_ADDRESS);
	i2c_write_char(functionCode);
	i2c_write_char(msbAdd);
	i2c_write_char(lsbAdd);
	i2c_write_char(0);
	i2c_write_char(1);

	i2c_start_tx();
	i2c_write_char(RD_ADDRESS);
	i2c_read_char(&data1);
	i2c_send_ack();
	i2c_read_char(&data2);
	i2c_send_ack();
	i2c_read_char(&data3);
	i2c_send_ack();
	i2c_read_char(&data4);
	i2c_send_nak();
	i2c_stop_tx();
	printf("%d, %d, %d, %d\n", data1, data2, data3, data4);
}
	//printf("error: %d\n",error);
	//printf("%d, %d, %d, %d\n", array[0], array[1], array[2], array[3]);

	/*while(1){
		i2c_startw_tx();
		
		i2c_write_char(add);
		result = i2c_check_ack();
		//i2c_read_char(&data1);
		//i2c_send_ack();
		i2c_stop_tx();
		//if(result != 1){
			printf("%d: %d\n", add, result);
		//}
		add = add +1;
		if (add==50)
		{
			add = 20;
		}
		wait();
	}*/

	/*while(add < 160){
	i2c_startw_tx();
	i2c_write_char(0x2A);
	i2c_write_char(functionCode);
	i2c_write_char(msbAdd);
	i2c_write_char(lsbAdd);
	i2c_write_char(0);
	i2c_write_char(1);
	i2c_startw_tx();
	i2c_write_char(0x2B);
	i2c_read_char(&data1);
	i2c_read_char(&data2);
	i2c_read_char(&data3);
	i2c_read_char(&data4);
	i2c_stop_tx();
	add = add + 1;
	if(data1 != 255 || data2 != 255 || data3 != 255 || data4 != 255){
	printf("%d: %d\n", add, data1);
	printf("%d\n", data2);
	printf("%d\n", data3);
	printf("%d\n", data4);
	}
	wait();
	}*/

	

	//I2CRead(0x15, 0x1389, &data1, 4);
	/*i2c_startw_tx();
	i2c_write_char((char) 0x15);
	i2c_write_char((char) 0x04);
	i2c_write_char((char) 0x13);
	i2c_write_char((char) 0x89);
	i2c_write_char((char) 0x00);
	i2c_write_char((char) 0x01);
	i2c_read_char(&tempData1);
	i2c_read_char(&tempData2);
	i2c_read_char(&data1);
	i2c_read_char(&data2);
	i2c_stop_tx();*/

	//printf("%d\n", tempData1);
	//printf("%d\n", tempData2);
	
	//printf("%d\n", data2);
}
