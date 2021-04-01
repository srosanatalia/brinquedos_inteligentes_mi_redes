import json
import os.path

from ClientThread import ClientThread
from SensorThread import SensorThread
from bcolors import bcolors

class ServerController:
    clients = []
    sensor = None
    
    def __init__(self):
        if os.path.isfile('configs/rfid.json'):
            start_rfid = input(f"{bcolors.YELLOW}Arquivo de configuração do RFID existente. Deseja iniciar conexão? (Y/n) {bcolors.COLOR_OFF}")
            if start_rfid == 'Y' or start_rfid == 'y':
                with open('configs/rfid.json', 'r') as file:
                    data = json.load(file)
                    self.__start_connection_rfid__(data)
        print('')

    def add_client(self, ip, port, clientsock):
        new_client = ClientThread(ip, port, clientsock, self)
        new_client.start()

        self.clients.append(new_client)
    
    def remove_client(self, client):
        self.clients.remove(client)

    def set_sensor(self, sensor):
        self.sensor = sensor

    def routes(self, request, body):
        method, url = request.split(' ')

        if method == 'POST' and url == '/rfid/config':
            self.__post_rfid_config__(body)

    def __post_rfid_config__(self, body):
        data = json.loads(body)
        with open('configs/rfid.json', 'w') as file:
            json.dump(data, file, indent=2)

        self.__start_connection_rfid__(data)
    
    def __start_connection_rfid__(self, data):
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