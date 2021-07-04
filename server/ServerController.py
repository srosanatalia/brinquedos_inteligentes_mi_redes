import json
import os.path

from ClientThread import ClientThread
from Sensor import Sensor
from Producer import Producer
from Consumer import Consumer
from bcolors import bcolors
from mqtt.Subscriber import Subscriber
from mqtt.Publisher import Publisher

class ServerController:
    # clients = []
    sensor = None
    
    def __init__(self):
        '''
        * Quando iniciado, o controller verifica se já existe arquivo de configuração do módulo de leitura,
        * caso positivo, pergunta ao usuário se ele deseja iniciar a conexão com o leitor.
        '''
        self.publisher = Publisher('autorama')
        self.subscriber = Subscriber('autorama/#')
        self.subscriber.set_topic('autorama/#')
        # self.subscriber.setTopic('teste/laercio')
        # print(self.subscriber.requestRecv())
        # if os.path.isfile('configs/rfid.json'):
        #     start_rfid = input(f"{bcolors.YELLOW}Arquivo de configuração do RFID existente. Deseja iniciar conexão? (Y/n) {bcolors.COLOR_OFF}")
        #     if start_rfid == 'Y' or start_rfid == 'y':
        #         with open('configs/rfid.json', 'r') as file:
        #             data = json.load(file)
        #             self.__start_connection_rfid(data)
        print('')

    # def add_client(self, ip, port, clientsock):
    #     new_client = ClientThread(ip, port, clientsock, self)
    #     new_client.start()

    #     self.clients.append(new_client)
    
    # def remove_client(self, client):
    #     self.clients.remove(client)

    def set_sensor(self, sensor):
        self.sensor = sensor

    # Método com as definições das rotas existentes
    def routes(self,  url, body=''):
        url = url.replace('/#', '')

        if body != '':
            try:
                body = json.loads(body)
            except:
                return

        # Condicionais para chamar o método referente a rota chamada
        if url == '/rfid/config':
            response = self.__post_rfid_config(body)
        elif url == '/rfid/tags':
            response = self.__get_rfid_tags(body)
        elif url == '/race/config':
            response = self.__post_race_config(body)
        elif url == '/race/qualification/start':
            response = self.__start_qualification()
        elif url == '/race/start':
            response = self.__start_race()
        else:
            response = '{"status":"NOT_FOUND"}'

        self.publisher.send_message(response.encode("utf-8"), f"response{url}")
        # else: # Caso seja chamado uma rota que não existe, é enviado uma mensagem de 'NOT_FOUND'
        #     client.clientsock.sendall(f"NOT_FOUND\n".encode("utf-8"))
        #     return
        
        # # Envia para o cliente mensagem de 'OK' com resposta da rota
        # client.clientsock.sendall(f"OK\n{response}".encode("utf-8"))

    def check_new_messages(self):
        if self.subscriber.has_new_message():
            body = self.subscriber.get_message()
            route = self.subscriber.get_topic_msg()
            print(route)
            self.routes(route.replace('autorama', ''), body)

    def __post_rfid_config(self, data):
        with open('configs/rfid.json', 'w') as file:
            json.dump(data, file, indent=2)

        self.__start_connection_rfid(data)
        return ''
    
    def __start_connection_rfid(self, data):
        sensor = Sensor(
            self,
            data['serial'], 
            data['baudrate'], 
            data['region'], 
            data['protocol'], 
            data['antenna'], 
            data['frequency']
        )
        sensor.run()
        if self.sensor != None:
            print(f"{bcolors.GREEN}Conexão com RFID iniciado... {bcolors.COLOR_OFF}")
    
    def __get_rfid_tags(self, data):
        if data['teste'] == None:
            data['teste'] = False
        return self.sensor.get_tags(data['teste'])

    def __post_race_config(self, data):
        self.race = data

        return ''

    def __start_qualification(self):
        self.buffer = __buffer_sensor__()
        self.producer_qualification = Producer(self.buffer, self.sensor, self.race, 'qualification')
        self.consumer_qualification = Consumer(self.buffer, self.race, self.publisher, 'qualification')
        self.producer_qualification.start()
        self.consumer_qualification.start()
        return ''

    def __start_race(self):
        self.buffer = __buffer_sensor__()
        self.producer_race = Producer(self.buffer, self.sensor, self.race, 'race')
        self.consumer_race = Consumer(self.buffer, self.race, self.publisher, 'race')
        self.producer_race.start()
        self.consumer_race.start()
        return ''

# Classe de apoio para o modelo Produtor-Consumidor
class __buffer_sensor__:
    buffer = []

    def add(self, data):
        self.buffer.append(data)

    def remove(self):
        return self.buffer.pop(0)

    def is_empty(self):
        return not self.buffer