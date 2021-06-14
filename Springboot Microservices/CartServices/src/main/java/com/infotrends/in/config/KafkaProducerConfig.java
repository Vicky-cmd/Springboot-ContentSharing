package com.infotrends.in.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaProducerConfig {
	
	 @Bean
	   public ProducerFactory<String, String> producerFactory() {
	      Map<String, Object> configProps = new HashMap<>();
	      configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "dory-01.srvs.cloudkafka.com:9094");
	      configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
	      configProps.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
	      configProps.put(SaslConfigs.SASL_JAAS_CONFIG, ScramLoginModule.class.getName() + " required username=\"qujoosad\" password=\"eUtZc2CecbLP7JVC7e17D9qUu5_GP9RB\";");
	      configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	      configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	      return new DefaultKafkaProducerFactory<>(configProps);
	   }
	   @Bean
	   public KafkaTemplate<String, String> kafkaTemplate() {
	      return new KafkaTemplate<>(producerFactory());
	   }
}
