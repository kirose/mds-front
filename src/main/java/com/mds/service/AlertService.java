package com.mds.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.mds.model.Alert;
import com.mds.repository.AlertRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertService {

	@Autowired private AlertRepository alertRepository;
	@Autowired private ElasticsearchOperations elasticsearchTemplate;
	
	public Alert update(Alert alert) {
		log.info("Index updated:{}",alert);
		Optional<Alert> o = alertRepository.findById(alert.getId());
		if (!o.isPresent()) {
			throw new RuntimeException("Cannot update alert, invalid id:"+alert.getId());
		}
		Alert entity = o.get();
		entity.setEmpleado(alert.getEmpleado());
		entity.setDispositivo(alert.getDispositivo());
		entity.setJustificacion(alert.getJustificacion());
		entity.setHost(alert.getHost());
		entity.setSensor(alert.getSensor());
		entity.setVigencia(alert.getVigencia());
		entity.setArea(alert.getArea());
		entity.setMotivo(alert.getMotivo());
		entity.setEstatus(alert.getEstatus());
		return alertRepository.save(entity);
	}
	public List<Alert> findAll() {
		List<Alert> alerts = new ArrayList<>();
		Iterable<Alert> i = alertRepository.findAll();
		i.forEach(alerts::add);
		return alerts;
	}
	public Optional<Alert> findById(String id) {
		return alertRepository.findById(id);
	}
	public Alert insert(Alert alert) {
		log.info("Index insert:{}",alert);
		return alertRepository.save(alert);
	}
	public void deleteById(String id) {
		alertRepository.deleteById(id);
	}
	public Alert update_(Alert alert) {
		Query searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(QueryBuilders.matchQuery("id", alert.getId()))
				  .build();
		SearchHits<Alert> articles = 
				   elasticsearchTemplate.search(searchQuery, Alert.class, IndexCoordinates.of("aiops_excepciones_gaby"));
		Alert entity = articles.getSearchHit(0).getContent();
		entity.setEmpleado(alert.getEmpleado());
		entity.setDispositivo(alert.getDispositivo());
		entity.setJustificacion(alert.getJustificacion());
		entity.setHost(alert.getHost());
		entity.setSensor(alert.getSensor());
		entity.setVigencia(alert.getVigencia());
		entity.setArea(alert.getArea());
		entity.setMotivo(alert.getMotivo());
		entity.setEstatus(alert.getEstatus());
		alertRepository.save(entity);
		return entity;
	}
}
