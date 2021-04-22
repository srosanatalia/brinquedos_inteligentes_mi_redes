import time
from datetime import datetime, timedelta
import threading
import sys

class Producer(threading.Thread):
    def __init__(self, buffer, sensor, race_config, type):
        threading.Thread.__init__(self)
        self.buffer = buffer 
        self.sensor = sensor
        self.min_lap_time = int(race_config['min_time_speedway'])
        self.cars = {}
        self.num_cars = 0
        self.current_lap = 0
        self.time_next_read = None

        if type == 'qualification':
            self.max_time = race_config['max_time_qualification']
            self.cars_ended_lap = []
            for car in race_config['cars']:
                self.cars[bytes(car, "utf-8")] = {"current_lap": 0, "first_read_lap": None, "next_start_read": None}
                self.num_cars += 1
                self.max_time_end = int(race_config['max_time_qualification'])
        elif type == 'race':
            self.num_laps = race_config['num_laps_race']
        
    
    def start_qualification(self):
        self.time_start = datetime.now()
        self.time_end = self.time_start + timedelta(seconds=self.max_time_end)
        self.time_next_read = self.time_start + timedelta(seconds=self.min_lap_time)
        for car in self.cars:
            self.cars[car]['next_start_read'] = self.time_start + timedelta(seconds=self.min_lap_time)
            self.cars[car]['first_read_lap'] = self.time_start
        timeout = self.time_start + timedelta(seconds=self.max_time_end+self.min_lap_time)
        while datetime.now() <= self.time_end:
            while datetime.now() < self.time_next_read:
                continue
            self.sensor.read_data(self.treat_data)
            while len(self.cars_ended_lap) != self.num_cars or datetime.now() > timeout:
                continue
            self.sensor.stop_read_data()
            self.cars_ended_lap = []

    def treat_data(self, epc, rssi, timestamp):
        try:
            if epc in self.cars.keys():
                if timestamp >= self.cars[epc]['next_start_read']:
                    self.cars[epc]['limit_time_lap'] = self.cars[epc]['next_start_read'] + timedelta(seconds=5)
                    self.cars[epc]['current_lap'] += 1
                    self.cars[epc]['first_read_lap'] = timestamp
                    self.cars[epc]['next_start_read'] = self.cars[epc]['first_read_lap'] + timedelta(seconds=self.min_lap_time)
                    if self.cars[epc]['current_lap'] > self.current_lap:
                        self.current_lap = self.cars[epc]['current_lap']
                        self.time_next_read = self.cars[epc]['next_start_read']
                
                if timestamp <= self.cars[epc]['limit_time_lap']:
                    self.buffer.add({"epc": epc, "rssi": rssi, "timestamp": timestamp, "lap": self.cars[epc]['current_lap']})
                elif epc not in self.cars_ended_lap:
                    self.cars_ended_lap.append(epc)
        except Exception as e:
            self.sensor.stop_read_data()
            print(e)
            sys.exit(0)