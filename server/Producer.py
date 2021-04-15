import time
from datetime import datetime
import threading

class Producer(threading.Thread):
    def __init__(self, buffer, sensor, race_config, type):
        threading.Thread.__init__(self)
        self.buffer = buffer 
        self.sensor = sensor
        self.min_lap_time = race_config['min_time_speedway']
        self.cars = {}

        if type == 'qualification':
            self.max_time = race_config['max_time_qualification']
            for car in race_config['cars']:
                self.cars[car] = {"current_lap": 0, "first_read_lap": None, "next_start_read": None}
        elif type == 'race':
            self.num_laps = race_config['num_laps_race']
        
    
    def start_qualification(self):
        print(self.cars)
        self.sensor.read_data(self.treat_data, 5)

    def treat_data(self, epc, rssi, timestamp):
        print(f"epc: {epc} - rssi: {rssi} - timestamp: {timestamp}")
        if epc in self.cars.keys():
            print("deu certo")