import Slider from "@react-native-community/slider";
import { useEffect, useState } from "react";
import { Text, View } from "react-native";
import { scale, StyleSheet } from "react-native-size-scaling";
import { getCache } from "../app/context/Storage";
import { StorePref } from "../app/context/UserPreference";

const SettingSlider = ({
	label,
	minValue = 0,
	maxValue = 100,
	valChange,
	storeKey,
	desc,
}) => {
	const [value, setValue] = useState(1);

	useEffect(() => {
		const getVal = () => {
			const val = getCache(storeKey) || `${minValue}`;
			setValue(Number(val));
		};
		getVal();
	}, [storeKey]);

	return (
		<View style={styles.container}>
			<View style={styles.valRow}>
				<Text style={styles.text}>{label}</Text>
				<Text style={styles.text}>{value}</Text>
			</View>
			<Text
				style={[
					styles.text,
					{
						paddingLeft: scale(10),
						fontSize: scale(12),
						color: "#A1968F",
					},
				]}
			>
				{desc}
			</Text>
			<Slider
				style={styles.slider}
				value={value}
				minimumValue={minValue}
				maximumValue={maxValue}
				minimumTrackTintColor="#3CB371"
				maximumTrackTintColor="#F28500"
				onValueChange={(val) => {
					setValue(Math.floor(val));
					valChange(val);
				}}
				onSlidingComplete={(val) => {
					const intVal = Math.floor(val);
					StorePref(storeKey, intVal.toString());
				}}
				step={1}
				thumbTintColor={"#6e9b3fff"}
			/>
			<View style={styles.valRow}>
				<Text style={styles.value}>{minValue}</Text>
				<Text style={styles.value}>{maxValue}</Text>
			</View>
		</View>
	);
};

const styles = StyleSheet.create({
	container: {
		paddingVertical: 25,
		paddingRight: 10,
	},
	valRow: {
		flexDirection: "row",
		justifyContent: "space-between",
		paddingHorizontal: 10,
	},
	text: {
		fontSize: 15,
		color: "#F4F1BB",
		fontWeight: "bold",
	},
	slider: {
		width: "100%",
		height: 40,
	},
	value: {
		fontSize: 12,
		color: "#6caf91ff",
	},
});

export default SettingSlider;
