package com.infotrends.in.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	  @Bean
	   public ConsumerFactory<String, String> consumerFactory() {
	      Map<String, Object> props = new HashMap<>();
	      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "dory-01.srvs.cloudkafka.com:9094");
	      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
	      props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
	      props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(ScramLoginModule.class.getName() + " required username=\"qujoosad\" password=\"eUtZc2CecbLP7JVC7e17D9qUu5_GP9RB\";"));
	      
	      props.put(ConsumerConfig.GROUP_ID_CONFIG, "qujoosad-consumers");
	      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
	      props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	      props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	      return new DefaultKafkaConsumerFactory<>(props);
	   }
	   @Bean
	   public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
	      ConcurrentKafkaListenerContainerFactory<String, String> 
	      factory = new ConcurrentKafkaListenerContainerFactory<>();
	      factory.setConsumerFactory(consumerFactory());
	      return factory;
	   }
}
