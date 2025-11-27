import { createMMKV } from "react-native-mmkv";

const storage = new createMMKV();

export const clearCache = () => {
	storage.clearAll();
};

export const deleteCache = (key) => {
	storage.remove(key);
};

export const setCache = (key, value) => {
	storage.set(key, typeof value === "object" ? JSON.stringify(value) : value);
};

export const getCache = (key) => {
	try {
		const data = storage.getString(key);
		return data;
	} catch (error) {
		console.log("Error get cache: ", error);
	}
};

export const allCacheKeys = () => storage.getAllKeys();
