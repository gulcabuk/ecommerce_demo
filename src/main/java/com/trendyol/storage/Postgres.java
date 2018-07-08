package com.trendyol.storage;

/**
 * @author zehragulcabukkeskin
 */
public class Postgres {

	/**
	 * Single instance.
	 */
	private static Postgres instance;

	/**
	 * Private constructor.
	 */
	private Postgres() {
	}

	/**
	 * Singleton instance.
	 * 
	 * @return
	 */
	public static Postgres getInstance() {
		if (instance == null) {
			instance = new Postgres();
		}
		return instance;
	}

	/**
	 * This method is expected to be a generic postgresql method to save entity with
	 * hibernate.
	 */
	public int saveObject(Object object) {
		return 0;
	}

	/**
	 * This method is expected to be a generic postgresql method to get object
	 * according to id.
	 */
	public <T> T getObjectByID(long id, Class<T> type) {
		return null;
	}

}
