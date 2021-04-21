import json
import os.path

from ClientThread import ClientThread
from SensorThread import SensorThread
from Producer import Producer
from bcolors import bcolors

class ServerController:
    clients = []
    sensor = None
    
    def __init__(self):
        # if os.path.isfile('configs/rfid.json'):
        #     start_rfid = input(f"{bcolors.YELLOW}Arquivo de configuração do RFID existente. Deseja iniciar conexão? (Y/n) {bcolors.COLOR_OFF}")
        #     if start_rfid == 'Y' or start_rfid == 'y':
        #         with open('configs/rfid.json', 'r') as file:
        #             data = json.load(file)
        #             self.__start_connection_rfid(data)
        print('')

    def add_client(self, ip, port, clientsock):
        new_client = ClientThread(ip, port, clientsock, self)
        new_client.start()

        self.clients.append(new_client)
    
    def remove_client(self, client):
        self.clients.remove(client)

    def set_sensor(self, sensor):
        self.sensor = sensor

    def routes(self, client, request, body):
        method, url = request.split(' ')

        if body != '':
            body = json.loads(body)

        if method == 'POST' and url == '/rfid/config':
            response = self.__post_rfid_config(body)
        elif method == 'GET' and url == '/rfid/tags':
            response = self.__get_rfid_tags()
        elif method == 'POST' and url == '/race/config':
            response = self.__post_race_config(body)
        elif method == 'POST' and url == '/race/qualification/start':
            response = self.__start_qualification()
        else:
            client.clientsock.sendall(f"NOT_FOUND\n".encode("utf-8"))
            return
        
        client.clientsock.sendall(f"OK\n{response}".encode("utf-8"))

    def __post_rfid_config(self, data):
        with open('configs/rfid.json', 'w') as file:
            json.dump(data, file, indent=2)

        self.__start_connection_rfid(data)
        return ''
    
    def __start_connection_rfid(self, data):
        sensor = SensorThread(
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
    
    def __get_rfid_tags(self):
        return self.sensor.get_tags()

    def __post_race_config(self, data):
        self.race = data
        self.buffer = __buffer_sensor__()
        self.producer = Producer(self.buffer, self.sensor, self.race, 'qualification')

        return ''

    def __start_qualification(self):
        self.producer.start_qualification()
        return ''

class __buffer_sensor__:
    buffer = []

    def add(self, data):
        print(data)
        self.buffer.append(data)

    def remove(self):
        return self.buffer.pop(0)