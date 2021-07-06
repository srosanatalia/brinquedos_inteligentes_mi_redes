'''
    Arquivo client.py para testes
'''
import socket, time, sys
from datetime import datetime, timedelta

from mqtt.Subscriber import Subscriber
from mqtt.Publisher import Publisher

# publisher.send_message("Fala")
# time.sleep(5)
# sys.exit(0)
                   
subscriber = Subscriber('response/#')
# publisher = Publisher('autorama')

subscriber.set_topic('response/rfid/config')
time.sleep(2)
subscriber.send_message('{"serial":"tmr:///dev/ttyUSB0", "baudrate":"230400", "region":"NA2", "protocol":"GEN2", "antenna":"1", "frequency":"1800"}'.encode("utf-8"), 'autorama/rfid/config')
while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/rfid/config':
    continue
print(subscriber.get_message())
time.sleep(5)

subscriber.set_topic('response/rfid/tags')
time.sleep(2)
# subscriber.send_message(''.encode("utf-8"), 'autorama/rfid/tags')
subscriber.send_message('{"teste":true}'.encode("utf-8"), 'autorama/rfid/tags')
while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/rfid/tags':
    continue
print(subscriber.get_message())
time.sleep(5)

subscriber.set_topic('response/race/config')
time.sleep(2)
subscriber.send_message('{"min_time_speedway":"5", "max_time_qualification":"15", "num_laps_race":"3", "cars":["E2000017221101241890547C", "E20000172211012518905484"]}'.encode("utf-8"), 'autorama/race/config')
# subscriber.send_message('{"min_time_speedway":"5", "max_time_qualification":"15", "num_laps_race":"3", "cars":["E2000017221101241890547C", "E20000172211012518905484"], "teste":true}'.encode("utf-8"), 'autorama/race/config')
while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/race/config':
    continue
print(subscriber.get_message())
time.sleep(5)

subscriber.set_topic('response/qualification')
time.sleep(2)
subscriber.send_message(''.encode("utf-8"), 'autorama/race/qualification/start')
while True:
    while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/qualification':
        continue
    data = subscriber.get_message()
    print(data)
    if data == 'QUALIFICATION_COMPLETED!':
        break
time.sleep(5)

subscriber.set_topic('response/race')
time.sleep(2)
subscriber.send_message(''.encode("utf-8"), 'autorama/race/start')
while True:
    while not subscriber.has_new_message() and subscriber.get_topic_msg() != 'response/race':
        continue
    data = subscriber.get_message()
    print(data)
    if data == 'RACE_COMPLETED!':
        break
time.sleep(5)
