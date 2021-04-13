import time

class Producer:
    def __init__(self, buffer, sensor):
        self.buffer = buffer 
        self.sensor = sensor
    
    def start_qualification(self, tags, min_lap_time, max_time):
        self.sensor.reader.enable_stats(stats_received)
        self.sensor.reader.start_reading(lambda tag: print(tag))
        time.sleep(2.4)
        self.sensor.reader.stop_reading()

def stats_received(stats):
    print({"temp" : stats.temperature})
    print({"antenna" : stats.antenna})
    print({"protocol" : stats.protocol})
    print({"frequency" : stats.frequency})