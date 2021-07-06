import paho.mqtt.client as mqtt
from configs import env
import time

class Subscriber:
    
    def __init__(self, topic = "/"):
        self.client = mqtt.Client(client_id=env.var('MQTT_ID'), clean_session=False, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        self.topic = topic
        self.receive_msg = False
        self.msg = ''
        self.topic_msg = ''
        self.client.on_message = self.on_message
        self.client.on_subscribe = self.on_subscribe
        self.client.on_publish = self.on_publish 
        mqtt.Client.connected_flag=False
        self.client.connect(env.var('MQTT_HOST'), port=int(env.var('MQTT_PORT')))
        while not self.connected:
            continue
        self.client.subscribe(self.topic, qos=0)
        self.client.connected_flag = True
        self.client.loop_start()

    def on_subscribe(self, client, userdata, mid, granted_qos):
        print(f"Subscriber se inscreveu no tópico: {self.topic}")
        
    '''
    * Método trata recebimento de mensagens, setando como positivo a variavel para
    * marcar que existe nova mensagem.
    '''
    def on_message(self, client, userdata, message):
        self.msg = message.payload.decode("utf-8")
        self.receive_msg = True
        self.topic_msg = message.topic
        print(f"Mensagem no tópico {self.topic_msg}: {self.msg}")

    def on_publish(self, client,userdata,mid):
        print("Dado publicado\n")

    def has_new_message(self):
        return self.receive_msg

    def connected(self):
        return self.client.is_connected()

    def get_topic_msg(self):
        return self.topic_msg

    def get_message(self):
        '''
        * Seta a váriavel de nova mensagem como negativo para marcar que a
        * já foi lida.
        '''
        self.receive_msg = False
        return self.msg

    def set_topic(self, topic):
        self.topic = topic
        self.client.subscribe(self.topic, qos=0)

    def send_message(self, message="", topic=None, retain=False):
        if topic == None:
            topic = self.topic
        while not self.client.connected_flag:
            time.sleep(1)

        print(f"Mensagem publicando: {message}")
        self.client.publish(topic, payload=message, qos=0, retain=retain)