import json
import paho.mqtt.client as mqtt
from configs import env
import time

class Publisher:
    
    def __init__(self, topic = "/"):
        self.client = mqtt.Client(client_id=env.var('MQTT_ID'), clean_session=True, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        # self.client = mqtt.Client(client_id=env.var('MQTT_ID2'), clean_session=True, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        # self.client.username_pw_set(env.var('MQTT_USER2'), env.var('MQTT_PASSWORD2'))
        self.topic = topic
        # self.client.on_log = self.on_log
        self.client.on_connect = self.on_connect
        self.client.on_disconnect = self.on_disconnect
        self.client.on_publish = self.on_publish  
        self.client.on_message = self.on_message    
        mqtt.Client.connected_flag=False
        self.client.connect(env.var('MQTT_HOST'), port=int(env.var('MQTT_PORT')))
        self.client.loop_start()

    def on_log(self, client, userdata, level, buf):
        print(buf)

    def on_connect(self, client, userdata, flags, rc):
        if rc == 0:
            client.connected_flag = True
            print("Conexão do publisher estabelecida")
            return
        
        print(f"Falha ao conectar publisher, erro, rc={rc}")

    def on_disconnect(self, client, userdata, rc):
        print("Publisher disconectou")

    def on_publish(self, client,userdata,mid):
        print("Dado publicado\n")
   
    def on_message(self, client, userdata, msg):
        msg = msg.payload.decode("utf-8")
        print(f"Mensagem no tópico {msg.topic}: {msg}")
        msg_json = json.loads(msg.payload)
        print(msg_json)

    def send_message(self, message="", retain=False):
        while not self.client.connected_flag:
            time.sleep(1)

        print(f"Mensagem publicando: {message}")
        self.client.publish(self.topic, payload=message, qos=0, retain=retain)
        # ret = self.client.publish(self.topic, message)

    def stop(self):
        self.client.disconnect()
        self.client.loop_stop()