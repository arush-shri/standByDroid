import * as Brightness from "expo-brightness";
import { useState } from "react";
import { ScrollView, Text } from "react-native";
import { scale, StyleSheet } from "react-native-size-scaling";
import FontSelector from "../components/Modals/FontSelector";
import SettingSlider from "../components/SettingSlider";
import { setCache } from "./context/Storage";
import { useUserPreferences } from "./context/UserPreference";

const AddFont = () => {
	const [show, setShow] = useState(false);

	return (
		<>
			<Text
				style={{ color: "#FFF", fontSize: scale(20) }}
				onPress={() => setShow(true)}
			>
				Custom Font
			</Text>
			<FontSelector visible={show} closeModal={() => setShow(false)} />
		</>
	);
};

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
			<AddFont />
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
