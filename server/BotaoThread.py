# Código enviado pelo Professor Augusto
from threading import Thread
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.IN, pull_up_down=GPIO.PUD_UP)

class Botao:
    def _init_(self):
        self.estado = False

    def setEstado(self, estado):
        self.estado = estado

    def getEstado(self):
        return self.estado

bt = Botao()

def monitor_botao():
        while True:
                input_state = GPIO.input(18)
                if input_state == False:
                        bt.setEstado(True)
                        return
                time.sleep(0.2)

if __name__ == "__main__":

        bt.setEstado(False)
        thread_botao = Thread(target=monitor_botao)
        thread_botao.start()
        print("Esperando apertar o botao.")
        while True:
                if bt.getEstado() == True:
                        print("Botao acionado!")
                        bt.setEstado(False)
                        thread_botao = Thread(target=monitor_botao)
                        thread_botao.start()
                        print("Esperando apertar o botao.")
                time.sleep(1)