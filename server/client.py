import socket, time, sys
from datetime import datetime, timedelta

from mqtt.Subscriber import Subscriber
from mqtt.Publisher import Publisher

# publisher.send_message("Fala")
# time.sleep(5)
# sys.exit(0)
#                               
subscriber = Subscriber('response/#')
publisher = Publisher('autorama')

publisher.send_message('{"serial":"tmr:///dev/ttyUSB0", "baudrate":"230400", "region":"NA2", "protocol":"GEN2", "antenna":"1", "frequency":"1800"}'.encode("utf-8"), 'autorama/rfid/config')
subscriber.set_topic('response/rfid/config')
while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/rfid/config':
    continue
print(subscriber.get_message())
time.sleep(5)

publisher.send_message('{"teste":true}'.encode("utf-8"), 'autorama/rfid/tags')
subscriber.set_topic('response/rfid/config')
while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/rfid/config':
    continue
print(subscriber.get_message())
time.sleep(5)

publisher.send_message('{"min_time_speedway":"5", "max_time_qualification":"15", "num_laps_race":"3", "cars":["E2000017221101241890547C", "E20000172211012518905484"], "teste":true}'.encode("utf-8"), 'autorama/race/config')
subscriber.set_topic('response/race/config')
while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/race/config':
    continue
print(subscriber.get_message())
time.sleep(5)

publisher.send_message(''.encode("utf-8"), 'autorama/race/qualification/start')
subscriber.set_topic('response/qualification')

while True:
    while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/qualification':
        continue
    data = subscriber.get_message()
    print(data)
    if data == 'QUALIFICATION_COMPLETED!':
        break

time.sleep(5)


# publisher.send_message(''.encode("utf-8"), 'autorama/race/start')
# data = s.recv(2048).decode("utf-8")
# print(data)

# while True:
#     data = s.recv(2048).decode("utf-8")
#     print(data)
#     if data == 'RACE_COMPLETED!':
#         break 