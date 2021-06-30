import paho.mqtt.client as mqtt
from configs import env
import logging
import json

class Subscriber:
    
    def __init__(self, topic = "/#"):
        self.client = mqtt.Client(env.var('MQTT_ID'), True)
        self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        self.topic = topic

        self.client.on_log = self.on_log
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.connect(env.var('MQTT_HOST'), env.var('MQTT_PORT'))
        self.client.loop_start()

    def on_log(self, client, userdata, level, buf):
        logging.info(buf)

    def on_connect(self, client, userdata, flags, rc):
        if rc == 0:
            client.connected_flag = True
            logging.info("Conexão estabelecida")
            return
        
        logging.info("Falha ao conectar %s, erro, rc=%s" % rc)
        
    def on_message(self, client, userdata, msg):
        msg = msg.payload.decode("utf-8")
        logging.info(f"Mensagem no tópico {msg.topic}: {msg}")
        msg_json = json.loads(msg.payload)
        print(msg_json)