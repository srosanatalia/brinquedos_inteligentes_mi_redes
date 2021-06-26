import mercury
import sys
import socket
import threading
import time
from datetime import datetime

from bcolors import bcolors

class Sensor():

    def __init__(self, controller, serial, baudrate, region, protocol, antenna, frequency):
        threading.Thread.__init__(self)
        self.controller = controller
        self.serial = serial
        self.baudrate = int(baudrate)
        self.region = region
        self.protocol = protocol
        self.antenna = int(antenna)
        self.frequency = int(frequency)

    # Método para iniciar conexão com o módulo de leitura
    def run(self):
        try:
            reader = mercury.Reader(self.serial, baudrate=self.baudrate)
            reader.set_region(self.region)
            reader.set_read_plan([self.antenna], self.protocol, read_power=self.frequency)
            self.reader = reader
            self.controller.set_sensor(self)
        except:
            print(f"{bcolors.RED}Não foi possível conectar à placa...{bcolors.COLOR_OFF}")

    # Obtém as TAGs que estão abaixo do leitor
    def get_tags(self):
        tags = list(map(lambda t: t.epc, self.reader.read()))
        return '{"tags":'+str(tags)+'}!'

    # Inicia leitura assíncrona
    def read_data(self, handle_data):
        self.reader.start_reading(lambda tag: handle_data(tag.epc, tag.rssi, datetime.fromtimestamp(tag.timestamp)))

    # Para leitura assincrona
    def stop_read_data(self):
        self.reader.stop_reading()
