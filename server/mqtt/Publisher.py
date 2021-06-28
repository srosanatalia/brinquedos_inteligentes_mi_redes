import paho.mqtt.client as mqtt
from configs import env

class Publisher:
    
    def __init__(self, topic = "/"):
        self.client = mqtt.Client(env.var('MQTT_ID'), True)
        self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        self.topic = topic
        self.receiveMsg = False
        self.client.on_log = self.on_log
        self.client.on_connect = self.on_connect
        self.client.on_disconnect = self.on_disconnect
        self.client.on_publish = self.on_publish  
        self.client.on_message = self.on_message    
        mqtt.Client.connected_flag=False
        self.client.connect(env.var('MQTT_HOST'), env.var('MQTT_PORT'))
        self.client.loop_start()

    def on_log(self, client, userdata, level, buf):
        print('a')

    def on_connect(self, client, userdata, flags, rc):
        print('a')

    def on_disconnect(self, client, userdata, rc):
        print('a')

    def on_publish(self, client,userdata,mid):
        print('a')
   
    def on_message(self, client, userdata, msg):
        print('a')