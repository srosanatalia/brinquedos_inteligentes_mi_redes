import mercury
reader = mercury.Reader("tmr:///dev/ttyUSB0", baudrate=115200)

reader.set_region("NA2")
reader.set_read_plan([1], "GEN2", read_power=1100)
print(reader.read())