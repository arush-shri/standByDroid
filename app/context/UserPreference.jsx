import * as Brightness from "expo-brightness";
import * as ScreenOrientation from "expo-screen-orientation";
import { createContext, useContext, useEffect, useState } from "react";
import { Platform } from "react-native";
import { getCache, setCache } from "./Storage";

const Preference = createContext();
export const PreferenceProvider = ({ children }) => {
	const [userPref, setUserPref] = useState({});

	const getOrientation = async () => {
		const data = getCache("orientation") || "landscape";
		setUserPref((prev) => ({ ...prev, Orientation: data }));
		switch (data) {
			case "portrait":
				await ScreenOrientation.lockAsync(
					ScreenOrientation.OrientationLock.PORTRAIT
				);
				break;
			case "landscape":
				await ScreenOrientation.lockAsync(
					ScreenOrientation.OrientationLock.LANDSCAPE
				);
				break;
			default:
				await ScreenOrientation.unlockAsync(); // allow system default
				break;
		}
	};

	const getBrightness = async () => {
		const setBrightness = () => {
			const pref = getCache("Brightness");
			if (pref) {
				Brightness.setBrightnessAsync(Number(Number(pref) / 100));
				setUserPref((prev) => ({
					...prev,
					Brightness: Number(Number(pref) / 100),
				}));
			}
		};
		if (Platform.OS === "android") {
			await setBrightness();
		} else {
			const { status } = await Brightness.requestPermissionsAsync();
			if (status === "granted") await setBrightness();
		}
	};

	const getOtherData = () => {
		const randomTime = getCache("randomTime") || "5";
		setUserPref((prev) => ({
			...prev,
			Randomness: Number(Number(randomTime) * 1000 * 60),
		}));
	};

	useEffect(() => {
		getOrientation();
		getBrightness();
		getOtherData();
	}, []);

	const contextValue = { userPref, setUserPref };

	return (
		<Preference.Provider value={contextValue}>
			{children}
		</Preference.Provider>
	);
};

export const useUserPreferences = () => useContext(Preference);

export const StorePref = (key, value) => {
	if (!key || !value) return;
	setCache(key, value);
};
