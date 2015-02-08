#class auto

#use "i2c_devices.lib"

char tempData1, tempData2, data1, data2;
int waitInt;

void wait(){
	for(waitInt = 0; waitInt < 20000; waitInt++){}
}

void main(){
	i2c_init();

	wait();


	I2CRead(0x15, 0x1389, &data1, 2);

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
	i2c_send_nak();
	i2c_stop_tx(); 

	printf("%d\n", tempData1);
	printf("%d\n", tempData2);
	printf("%d\n", data1);
	printf("%d\n", data2);*/

	printf("%d\n", data1);
}
