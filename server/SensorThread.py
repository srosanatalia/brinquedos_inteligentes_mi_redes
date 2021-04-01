import mercury
import sys
import socket
import threading

from bcolors import bcolors

class SensorThread(threading.Thread):

    def __init__(self, controller, serial, baudrate, region, protocol, antenna, frequency):
        threading.Thread.__init__(self)
        self.controller = controller
        self.serial = serial
        self.baudrate = int(baudrate)
        self.region = region
        self.protocol = protocol
        self.antenna = antenna
        self.frequency = int(frequency)

    def run(self):
        try:
            reader = mercury.Reader(self.serial, baudrate=self.baudrate)
            reader.set_region(self.region)
            reader.set_read_plan([1], self.protocol, read_power=self.frequency)
            print(reader.read())
            self.controller.set_sensor(self)
        except:
            print(f"{bcolors.RED}Não foi possível conectar à placa...{bcolors.COLOR_OFF}")
