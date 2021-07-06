/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Mqtt;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 *
 * @author natalia
 * 
 * Classe utilizada para que o cliente se inscreva em um tópico e envie mensagens no protocolo MQTT
 */
public class Subscriber implements MqttCallbackExtended{
    
    public MqttClient clienteMqtt;
    private final MqttConnectOptions mqttOptions;
    public String urlBroker;
    public String topic;
    public String mensagem = "";
    public ArrayList tags;
    
    public Subscriber(String url, String topic) throws MqttException {
        this.urlBroker = url;
        this.topic = topic;
        this.tags = new ArrayList();
        this.clienteMqtt = new MqttClient(this.urlBroker, "xx"); //Definir rota e id
        
        mqttOptions = new MqttConnectOptions();
        mqttOptions.setMaxInflight(200);
        mqttOptions.setConnectionTimeout(3);
        mqttOptions.setKeepAliveInterval(10);
        mqttOptions.setAutomaticReconnect(true); //Caso a conexão seja perdida, reconecta automaticamente
        mqttOptions.setCleanSession(false);
        mqttOptions.setUserName("xx"); //Definir username da conexão
        mqttOptions.setPassword("xx".toCharArray()); //Definir password da conexão
        
        this.clienteMqtt.setCallback(this);
        this.clienteMqtt.connect(mqttOptions);
        this.clienteMqtt.subscribe(topic, 0);
        
    }

    public String getUrlBroker() {
        return urlBroker;
    }

    public void setUrlBroker(String urlBroker) {
        this.urlBroker = urlBroker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) throws MqttException { //Seta um tópico para ser ouvido
        this.topic = topic;
        this.clienteMqtt.subscribe(topic, 0);
        System.out.println("O cliente se increveu no tópico:" + topic);
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public void enviaMensagem(String mensagem, String topico) throws MqttException{
        enviaMensagem(mensagem.getBytes(), topico, 0, false);
    }
    
    //Utilizado para enviar mensagens para o broker
    public synchronized void enviaMensagem(byte[] payload, String topic, int qos, boolean retained) throws MqttException{
       try {
            if (this.clienteMqtt.isConnected()) {
                this.clienteMqtt.publish(topic, payload, qos, retained);
                System.out.println(String.format("Tópico %s publicado. %s", topic, payload));
            } else {
                System.out.println("Cliente desconectado, não foi possível publicar o tópico " + topic);
            }
        } catch (MqttException ex) {
            System.out.println("Erro ao publicar " + topic + " - " + ex);
        }
    }
    
    //Informa se a conexão está ativa ou não
    public boolean isConected(){
        return this.clienteMqtt.isConnected();
    }
    
    //Informa quando a conexão é perdida
    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Conexão perdida -" + thrwbl);
    }
    
    //Mostra as mensagens que chegam
    @Override
    public void messageArrived(String topico, MqttMessage mm) throws Exception {
        System.out.println("Mensagem recebida:");
        System.out.println("\tTópico: " + topico);
        this.mensagem =  new String(mm.getPayload());
        System.out.println("\tMensagem: " + this.mensagem);
        System.out.println("");
        
    }

    public ArrayList getTags() {
        return tags;
    }

    public void setTags(ArrayList tags) {
        this.tags = tags;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        System.out.println("Cliente MQTT " + (reconnect ? "reconectado" : "conectado") + " com o broker " + serverURI);
    }
}
