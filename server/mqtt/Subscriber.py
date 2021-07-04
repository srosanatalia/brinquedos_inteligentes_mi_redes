import paho.mqtt.client as mqtt
from configs import env

class Subscriber:
    
    def __init__(self, topic = "/"):
        self.client = mqtt.Client(client_id=env.var('MQTT_ID'), clean_session=False, userdata=None, protocol=mqtt.MQTTv311, transport="tcp")
        self.client.username_pw_set(env.var('MQTT_USER'), env.var('MQTT_PASSWORD'))
        self.topic = topic
        self.receive_msg = False
        self.msg = ''
        self.topic_msg = ''
        # self.client.on_log = self.on_log
        # self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.on_subscribe = self.on_subscribe
        self.client.connect(env.var('MQTT_HOST'), port=int(env.var('MQTT_PORT')))
        while not self.connected:
            continue
        self.client.subscribe(self.topic, qos=0)
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
        self.msg = message.payload.decode("utf-8")
        self.receive_msg = True
        self.topic_msg = message.topic
        print(f"Mensagem no tópico {self.topic_msg}: {self.msg}")

    # def requestRecv(self):
    #     while not self.receive_msg:
    #         continue
    #     self.receive_msg = False
    #     return self.msg

    # def stop(self):
    #     self.client.disconnect()
    #     self.client.loop_stop()

    def has_new_message(self):
        return self.receive_msg

    def connected(self):
        return self.client.is_connected()

    def get_topic_msg(self):
        return self.topic_msg

    def get_message(self):
        self.receive_msg = False
        return self.msg

    def set_topic(self, topic):
        self.topic = topic
        self.client.subscribe(self.topic, qos=0)