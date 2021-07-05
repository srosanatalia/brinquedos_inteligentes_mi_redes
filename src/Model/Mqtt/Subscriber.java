/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 *
 * @author natalia
 */
public class Subscriber implements MqttCallback{
    
    public MqttClient clienteMqtt;
    public String urlBroker;
    public String topic;
    public String mensagem = "";
    
    public Subscriber(String url, String topic) throws MqttException {
        this.urlBroker = url;
        this.topic = topic;
        this.clienteMqtt = new MqttClient(this.urlBroker, "ClienteSubscriber");
        this.clienteMqtt.setCallback(this);
        this.clienteMqtt.connect();
        this.clienteMqtt.subscribe(topic);
        
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

    public void setTopic(String topic) throws MqttException {
        this.topic = topic;
        this.clienteMqtt.subscribe(topic);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public void enviaMensagem(String mensagem, String topico) throws MqttException{
        MqttMessage messageSend = new MqttMessage((mensagem).getBytes());
        System.out.println("Publicando: "+mensagem);
        clienteMqtt.publish(topico, messageSend);
    }
    
    public boolean isConected(){
        return this.clienteMqtt.isConnected();
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Conexão perdida.");
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        System.out.println("Mensagem recebida: Tópico-" + string+ " Mensagem- "+ mm.toString());
        this.mensagem = mm.toString();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
