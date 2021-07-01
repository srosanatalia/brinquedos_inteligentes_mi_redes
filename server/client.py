import socket, time, sys
from datetime import datetime, timedelta

from mqtt.Subscriber import Subscriber
from mqtt.Publisher import Publisher

# subscriber = Subscriber('laercio')
publisher = Publisher('laercio')
publisher.send_message("Teste, por favor, vai")
time.sleep(5)
sys.exit(0)

# create a socket object
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

# get local machine name
# host = 'augusto.ddns.net'
port = 5022
host = '127.0.0.1'
# port = 2000
# host = '6.tcp.ngrok.io'89iop
# port = 10203

# connection to hostname on the port.
s.connect((host, port))                               

s.sendall('POST /rfid/config\n{"serial":"tmr:///dev/ttyUSB0", "baudrate":"230400", "region":"NA2", "protocol":"GEN2", "antenna":"1", "frequency":"1800"}'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(2)

# s.sendall('GET /rfid/tags\n'.encode("utf-8"))
# data = s.recv(2048).decode("utf-8")
# print(data)
# time.sleep(2)

s.sendall('POST /race/config\n{"min_time_speedway":"5", "max_time_qualification":"15", "num_laps_race":"3", "cars":["E2000017221101241890547C", "E20000172211012518905484"], "teste":true}'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)
time.sleep(2)

# s.sendall('POST /race/qualification/start\n'.encode("utf-8"))
# data = s.recv(2048).decode("utf-8")
# print(data)

# while True:
#     data = s.recv(2048).decode("utf-8")
#     print(data)
#     if data == 'QUALIFICATION_COMPLETED!':
#         break

# time.sleep(2)
s.sendall('POST /race/start\n'.encode("utf-8"))
data = s.recv(2048).decode("utf-8")
print(data)

while True:
    data = s.recv(2048).decode("utf-8")
    print(data)
    if data == 'RACE_COMPLETED!':
        break 