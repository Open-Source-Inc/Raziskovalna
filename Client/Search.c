#class auto

#use "i2c_devices.lib"

int waitInt, result, add;

void wait(){
	for(waitInt = 0; waitInt < 2000; waitInt++){}
}

void main(){
	i2c_init();
	wait();
	add=1;
while(1){
		i2c_startw_tx();
		
		i2c_write_char(add);
		result = i2c_check_ack();
		//i2c_read_char(&data1);
		//i2c_send_ack();
		i2c_stop_tx();
		if(result != 1){
			printf("%d: %d; ", add, result);
		}
		add = add +1;
		if (add==255)
		{
			add = 0;
			printf("\n");
		}

		wait();
	}
}