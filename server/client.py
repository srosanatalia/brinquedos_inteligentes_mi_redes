#!/usr/bin/python3           # This is client.py file

import socket, time

# create a socket object
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

# get local machine name
# host = 'augusto.ddns.net'
# port = 5022
host = '127.0.0.1'
port = 2000
# host = '6.tcp.ngrok.io'
# port = 10203

# connection to hostname on the port.
s.connect((host, port))                               

s.sendall('POST /rfid/config\n{"serial":"tmr:///dev/ttyUSB0", "baudrate":"115200", "region":"NA2", "protocol":"GEN2", "antenna":"1", "frequency":"1500"}'.encode())

time.sleep(5)

# s.sendall('GET /rfid/config\n'.encode())

# time.sleep(3)

# s.sendall('3'.encode())

# time.sleep(3)

# s.sendall('4'.encode())

# time.sleep(3)

# s.sendall('5'.encode())