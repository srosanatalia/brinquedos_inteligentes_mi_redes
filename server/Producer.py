import time
from datetime import datetime

class Producer:
    def __init__(self, buffer, sensor):
        self.buffer = buffer 
        self.sensor = sensor
    
    def start_qualification(self, tags, min_lap_time, max_time):
        self.sensor.read_data(teste, 5)

def teste(epc, rssi, timestamp):
    print(f"epc: {epc} - rssi: {rssi} - timestamp: {timestamp}")