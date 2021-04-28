#!/usr/bin/python3           # This is client.py file

import socket, time, sys
from datetime import datetime, timedelta

# format_time = '%M:%S.%f'
# now = datetime.now()
# new_now = datetime.now()
# few_seconds = new_now + timedelta(seconds=3)
# print(str(few_seconds - now)[2:])

# a = []
# a.insert(0, 'a')
# a.insert(2, 'c')
# a.insert(1, 'b')
# a[0] = '*'
# print(a)

# teste = [
#     {'name':'Homer', 'age':39, 'time':datetime.now()+ timedelta(seconds=3)}, 
#     {'name':'Bart', 'age':10, 'time':datetime.now()+ timedelta(seconds=5)}, 
#     {'name':'Marge', 'age':35, 'time':datetime.now()}
# ]
# print(teste)
# newlist = sorted(teste, key=lambda k: k['time'], reverse=True)
# print(newlist)
# for a in newlist:
#     a['a'] = 1
# print(newlist)
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
time.sleep(2)

s.sendall('GET /rfid/tags\n'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(2)

s.sendall('POST /race/config\n{"min_time_speedway":"20", "max_time_qualification":"60", "num_laps_race":"5", "cars":["E2000017221101241890547C", "E20000172211012518905484", "E20000172211013118905493"]}'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(2)

s.sendall('POST /race/qualification/start\n'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)

while True:
    data = s.recv(2048).decode("utf-8")
    print(data)
    if data == 'OK\n{"msg": "QUALIFICATION_COMPLETED"}!':
        break