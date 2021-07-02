import paho.mqtt.client as mqtt
from configs import env
import json
import time

class Subscriber:
    
    def __init__(self, topic = "/"):
        self.client = mqtt.Client(client_id=env.var('MQTT_ID'), clean_session=True, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        self.topic = topic
        self.receive_msg = False
        self.msg = ''
        # self.client.on_log = self.on_log
        # self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.on_subscribe = self.on_subscribe
        self.client.connect(env.var('MQTT_HOST'), port=int(env.var('MQTT_PORT')))
        self.client.loop_start()

    def on_log(self, client, userdata, level, buf):
        print(buf)

    def on_subscribe(self, client, userdata, mid, granted_qos):
        print(f"Subscriber se inscreveu no tópico: {self.topic}")

    def on_connect(self, client, userdata, flags, rc):
        if rc == 0:
            client.connected_flag = True
            print("Conexão do subscriber estabelecida")
            self.client.subscribe(self.topic, qos=0)
            return
        
        print(f"Falha ao conectar subscriber, erro, rc={rc}")
        
    def on_message(self, client, userdata, message):
        # print(message.topic+" "+str(message.qos)+" "+str(message.payload))
        message_decode = message.payload.decode("utf-8")
        print(f"Mensagem no tópico {message.topic}: {message_decode}")
        self.receive_msg = True
        self.msg = message_decode
        # msg_json = json.loads(message.payload)
        # print(msg_json)

    def requestRecv(self):
        while not self.receive_msg:
            continue
        self.receive_msg = True
        return self.msg

    def stop(self):
        self.client.disconnect()
        self.client.loop_stop()

    def connected(self):
        return self.client.is_connected()

    def setTopic(self, topic):
        self.topic = topic
        self.client.subscribe(self.topic, qos=0)