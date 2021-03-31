import mercury
import socket
import threading

class SensorThread(threading.Thread):

    def __init__(self, serial, baudrate, region, protocol, antenna, frequency):
        threading.Thread.__init__(self)
        self.serial = serial
        self.baudrate = baudrate
        self.region = region
        self.protocol = protocol
        self.antenna = antenna
        self.frequency = frequency

    def run(self):
        reader = mercury.Reader(self.serial, baudrate=self.baudrate)
        reader.set_region(self.region)
        reader.set_read_plan([1], self.protocol, read_power=1100)
        print(reader.read())
