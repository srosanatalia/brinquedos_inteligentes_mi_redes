import mercury
import sys
import socket
import threading
import time
import random
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
            # self.controller.set_sensor(self)
        except:
            print(f"{bcolors.RED}Não foi possível conectar à placa...{bcolors.COLOR_OFF}")
        finally:
            self.controller.set_sensor(self)

    # Obtém as TAGs que estão abaixo do leitor
    def get_tags(self, teste=False):
        if not teste:
            tags = list(map(lambda t: t.epc, self.reader.read()))
        else:
            tags = ["E2000017221101241890547C", "E20000172211012518905484"]
        return '{"tags":'+str(tags)+'}!'

    # Inicia leitura assíncrona
    def read_data(self, handle_data, teste=False, cars=[]):
        if teste:
            self.simulate_data = __SimulateData__(handle_data, cars)
            self.simulate_data.start()
        else:
            self.reader.start_reading(lambda tag: handle_data(tag.epc, datetime.fromtimestamp(tag.timestamp)))

    # Para leitura assincrona
    def stop_read_data(self, teste=False):
        if teste:
            self.simulate_data.stop()
        else:
            self.reader.stop_reading()

    def finished_read_data(self):
        self.simulate_data.finished()

class __SimulateData__(threading.Thread):
    def __init__(self, handle_data, epcs):
        threading.Thread.__init__(self)
        self.handle_data = handle_data
        self.epcs = epcs
        self.runner = False 
        self.finish = False 

    def run(self):
        self.runner = True
        while not self.finish:
            if self.runner:
                epc_random = random.choice(self.epcs)
                time_random = datetime.now()
                self.handle_data(epc_random, time_random)
            time.sleep(1)

    def stop(self):
        self.runner = False 

    def finished(self):
        self.finish = True 