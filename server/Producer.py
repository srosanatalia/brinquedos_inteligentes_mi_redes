import time
from datetime import datetime, timedelta
import threading

class Producer(threading.Thread):
    def __init__(self, buffer, sensor, race_config, type):
        threading.Thread.__init__(self)
        self.buffer = buffer 
        self.sensor = sensor
        self.min_lap_time = int(race_config['min_time_speedway'])
        self.cars = {}
        self.num_cars = 0

        if type == 'qualification':
            self.max_time = race_config['max_time_qualification']
            self.cars_ended_lap = []
            for car in race_config['cars']:
                self.cars[bytes(car, "utf-8")] = {"current_lap": 0, "first_read_lap": None, "next_start_read": None}
                self.num_cars += 1
                self.time_end = int(race_config['max_time_qualification'])
        elif type == 'race':
            self.num_laps = race_config['num_laps_race']
        
    
    def start_qualification(self):
        self.time_start = datetime.now()
        print(self.time_start)
        self.time_end = self.time_start + timedelta(seconds=self.time_end)
        print(self.time_end)
        time.sleep(self.min_lap_time)
        print(datetime.now())
        while datetime.now() <= self.time_end:
            self.sensor.read_data(self.treat_data)
            while self.cars_ended_lap.count() != self.num_cars:
                self.sensor.stop_read_data()
                self.cars_ended_lap = []

    def treat_data(self, epc, rssi, timestamp):
        if epc in self.cars.keys():
            limit_time = self.cars[epc]['next_start_read'] + timedelta(seconds=5)
            if timestamp >= self.cars[epc]['next_start_read'] and timestamp <= limit_time:
                if timestamp >= (self.cars[epc]['first_read_lap'] + timedelta(seconds=self.min_lap_time)):
                    self.cars[epc]['current_lap'] += 1
                    self.cars[epc]['first_read_lap'] += timestamp
                self.buffer.add({"epc": epc, "rssi": rssi, "timestamp": timestamp, "lap": self.cars[epc]['current_lap']})
            elif epc not in self.cars_ended_lap:
                self.cars_ended_lap.append(epc)