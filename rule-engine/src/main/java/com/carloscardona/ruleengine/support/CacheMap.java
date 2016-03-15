package com.carloscardona.ruleengine.support;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheMap<K, V> implements Map<K, V> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheMap.class);

	private static final String INVALID_EXPIRATION = "Invalid expiration";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

	private final Map<K, V> delegate;
	private final Map<K, Date> timestampMap;
	private long expiration = 5 * 60 * 5;

	/**
	 * Crea un CacheMap con el tiempo de expiracion por defecto de 5 minutos.
	 * 
	 * @param expiration
	 */
	public CacheMap() {
		this.delegate = new ConcurrentHashMap<K, V>();
		this.timestampMap = new HashMap<K, Date>();

	}

	/**
	 * Crea un CacheMap con el tiempo de expiracion indicado (en milisegundos).
	 * 
	 * @param expiration
	 */
	public CacheMap(int initialCapacity) {
		this.delegate = new ConcurrentHashMap<K, V>(initialCapacity);
		this.timestampMap = new HashMap<K, Date>(initialCapacity);
		if (expiration <= 0) {
			throw new IllegalArgumentException(INVALID_EXPIRATION);
		}
	}

	public CacheMap(long expiration, Map<? extends K, ? extends V> map) {
		this.delegate = new ConcurrentHashMap<K, V>(map);
		this.timestampMap = new HashMap<K, Date>();
		this.expiration = expiration;
		if (expiration <= 0) {
			throw new IllegalArgumentException(INVALID_EXPIRATION);
		}
	}

	public CacheMap(long expiration, int initialCapacity, float loadFactor) {
		this.delegate = new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
		this.timestampMap = new HashMap<K, Date>(initialCapacity, loadFactor);
		this.expiration = expiration;
		if (expiration <= 0) {
			throw new IllegalArgumentException(INVALID_EXPIRATION);
		}
	}

	public CacheMap(long expiration, int initialCapacity) {
		this.delegate = new ConcurrentHashMap<K, V>(initialCapacity);
		this.timestampMap = new HashMap<K, Date>(initialCapacity);
		this.expiration = expiration;
		if (expiration <= 0) {
			throw new IllegalArgumentException(INVALID_EXPIRATION);
		}
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	@Override
	public V get(Object key) {
		V value = delegate.get(key);
		if (value != null) {
			Date creationTime = timestampMap.get(key);
			if (creationTime == null) {
				creationTime = new Date();
				timestampMap.put((K) key, creationTime);
			}
			long lifeTime = System.currentTimeMillis() - creationTime.getTime();
			if (lifeTime > this.expiration) {
				LOGGER.trace("Invalidando entrada para la clave '" + key + "'; Tiempo de expiracion (" + lifeTime + " ms) superado.");
				remove(key);
				return null;
			}
		}
		return value;
	}

	@Override
	public V put(K key, V value) {
		V result = delegate.put(key, value);
		timestampMap.put(key, new Date());
		return result;
	}

	@Override
	public V remove(Object key) {
		V v = delegate.remove(key);
		timestampMap.remove(key);
		return v;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		delegate.putAll(m);
		for (K key : m.keySet()) {
			timestampMap.put(key, new Date());
		}
	}

	@Override
	public void clear() {
		delegate.clear();
		timestampMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return delegate.keySet();
	}

	@Override
	public Collection<V> values() {
		return delegate.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return delegate.entrySet();
	}
}