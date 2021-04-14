#!/usr/bin/python3           # This is client.py file

import socket, time

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

s.sendall('POST /rfid/config\n{"serial":"tmr:///dev/ttyUSB0", "baudrate":"230400", "region":"NA2", "protocol":"GEN2", "antenna":"1", "frequency":"1500"}'.encode())
data = s.recv(2048).decode()
print(data)
time.sleep(5)
s.sendall('GET /rfid/tags\n'.encode())
data = s.recv(2048).decode()
print(data)
time.sleep(5)
s.sendall('GET /teste\n'.encode())
data = s.recv(2048).decode()
print(data)

time.sleep(5)
