#!/usr/bin/python3           # This is client.py file

import socket, time, sys
from datetime import datetime, timedelta

# time_end = datetime.now() + timedelta(seconds=10)
# while datetime.now() <= time_end:
#     print(datetime.now())
# sys.exit(0)

# create a socket object
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

# get local machine name
host = 'augusto.ddns.net'
port = 5022
# host = '127.0.0.1'
# port = 2000
# host = '6.tcp.ngrok.io'89iop
# port = 10203

# connection to hostname on the port.
s.connect((host, port))                               

s.sendall('POST /rfid/config\n{"serial":"tmr:///dev/ttyUSB0", "baudrate":"230400", "region":"NA2", "protocol":"GEN2", "antenna":"1", "frequency":"1500"}'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(3)

s.sendall('GET /rfid/tags\n'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(3)

s.sendall('POST /race/config\n{"min_time_speedway":"45", "max_time_qualification":"150", "num_laps_race":"5", "cars":["E2000017221101241890547C", "E20000172211012518905484"]}'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(3)

s.sendall('POST /race/qualification/start\n'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(3)
