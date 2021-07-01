import paho.mqtt.client as mqtt
from configs import env
import logging
import json
import time

class Subscriber:
    
    def __init__(self, topic = "/"):
        # self.client = mqtt.Client(client_id=env.var('MQTT_ID'), clean_session=True, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        # self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        self.client = mqtt.Client(client_id=env.var('MQTT_ID2'), clean_session=True, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        self.client.username_pw_set(env.var('MQTT_USER2'), env.var('MQTT_PASSWORD2'))
        self.topic = topic
        self.receiveMsg = False
        self.client.on_log = self.on_log
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.on_subscribe = self.on_subscribe
        self.client.connect(env.var('MQTT_HOST'), port=int(env.var('MQTT_PORT')))
        self.client.loop_start()
        self.client.subscribe(self.topic, qos=0)

    def on_log(self, client, userdata, level, buf):
        logging.info(buf)

    def on_subscribe(self, client, userdata, mid, granted_qos):
        print(f"Subscriber se inscreveu no tópico: {self.topic}")

    def on_connect(self, client, userdata, flags, rc):
        if rc == 0:
            client.connected_flag = True
            logging.info("Conexão estabelecida")
            print("Conexão do subscriber estabelecida")
            return
        
        logging.info(f"Falha ao conectar, erro, rc={rc}")
        print(f"Falha ao conectar subscriber, erro, rc={rc}")
        
    def on_message(self, client, userdata, message):
        print('aaaaaaaaa')
        message_decode = message.payload.decode("utf-8")
        print(message_decode)
        logging.info(f"Mensagem no tópico {message.topic}: {message}")
        self.receiveMsg = True
        self.msg = message_decode
        # msg_json = json.loads(message.payload)
        # print(msg_json)

    def requestRecv(self):
        while not self.receiveMsg:
            time.sleep(0.5)
        self.receiveMsg = False
        return self.msg

    def stop(self):
        self.client.disconnect()
        self.client.loop_stop()