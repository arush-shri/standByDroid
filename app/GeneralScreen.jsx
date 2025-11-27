import * as Brightness from "expo-brightness";
import { ScrollView } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import SettingSlider from "../components/SettingSlider";
import { setCache } from "./context/Storage";
import { useUserPreferences } from "./context/UserPreference";

const GeneralScreen = () => {
	const { setUserPref } = useUserPreferences();

	return (
		<ScrollView
			showsVerticalScrollIndicator={false}
			alwaysBounceVertical={true}
			contentContainerStyle={styles.contatiner}
		>
			<SettingSlider
				label={"Brightness"}
				minValue={0}
				maxValue={100}
				valChange={async (val) => {
					const normalized = val / 100;
					await Brightness.setBrightnessAsync(normalized);
				}}
				storeKey={"Brightness"}
			/>
			<SettingSlider
				label={"Color Randomness"}
				minValue={5}
				maxValue={60}
				valChange={(val) => {
					setCache("randomTime", val.toString());
					setUserPref((prev) => ({
						...prev,
						Randomness: Number(val * 1000 * 60),
					}));
				}}
				desc={"Duration in minutes to change colors randomly"}
				storeKey={"Randomness"}
			/>
		</ScrollView>
	);
};

const styles = StyleSheet.create({
	contatiner: {
		paddingLeft: 20,
		paddingRight: 5,
	},
});

export default GeneralScreen;
