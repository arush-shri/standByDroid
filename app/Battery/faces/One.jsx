import { useEffect, useState } from "react";
import { StyleSheet as RNStyle, Text, View } from "react-native";
import { scale, StyleSheet } from "react-native-size-scaling";
import Svg, { Circle } from "react-native-svg";
import DriftingView from "../../../components/DriftingView";
import { addOpacity, GetRandomColor } from "../../context/Randomizer";
import { useUserPreferences } from "../../context/UserPreference";

const One = ({ info }) => {
	const { userPref } = useUserPreferences();
	const [color, setColor] = useState("	hsl(108, 63%, 52%)");
	const [boxSize, setBoxSize] = useState({ width: 1, height: 1 });

	const size = Math.min(boxSize.width * 0.8, boxSize.height * 0.8);
	const strokeWidth = size * 0.08; // 10% of size
	const radius = (size - strokeWidth) / 2;
	const circumference = 2 * Math.PI * radius;
	const strokeDashoffset = circumference * (1 - info.batteryPercent / 100);

	useEffect(() => {
		const interval = setInterval(() => {
			const randColor = GetRandomColor();
			setColor(randColor);
		}, userPref?.Randomness || 5000);

		return () => clearInterval(interval);
	}, [userPref]);

	return (
		<DriftingView
			onLayout={(e) => {
				const { width, height } = e.nativeEvent.layout;
				setBoxSize({ width, height });
			}}
			styling={styles.container}
		>
			<Svg width={size} height={size}>
				<Circle
					stroke={addOpacity(color, 0.4)}
					fill="none"
					cx={size / 2}
					cy={size / 2}
					r={radius}
					strokeWidth={strokeWidth}
				/>
				<Circle
					stroke={color}
					fill="none"
					cx={size / 2}
					cy={size / 2}
					r={radius}
					strokeWidth={strokeWidth}
					strokeDasharray={`${circumference} ${circumference}`}
					strokeDashoffset={strokeDashoffset}
					strokeLinecap="round"
					transform={`rotate(-90 ${size / 2} ${size / 2})`}
				/>
			</Svg>
			<View style={[RNStyle.absoluteFill, styles.center]}>
				<Text
					style={[
						styles.percentText,
						{
							fontSize: size * 0.22,
							color: color,
						},
					]}
				>
					{Math.round(info.batteryPercent)}%
				</Text>
				<Text
					style={[
						styles.percentText,
						{
							fontSize: size * 0.09,
							color: color,
						},
					]}
				>
					{info.isCharging ? "Charging" : "Discharging"}
				</Text>
				<Text
					style={[
						styles.percentText,
						{
							fontSize: size * 0.07,
							color: color,
							marginTop: scale(size * 0.02),
						},
					]}
				>
					{info.voltage?.toFixed(2)} V {info.current?.toFixed(2)} A
				</Text>
				<Text
					style={[
						styles.percentText,
						{
							fontSize: size * 0.07,
							color: color,
							marginTop: scale(size * 0.02),
						},
					]}
				>
					{info.wattage?.toFixed(2)} W
				</Text>
			</View>
		</DriftingView>
	);
};

const styles = StyleSheet.create({
	container: {},
	progressBox: {},
	percentText: {
		fontFamily: "formula1",
	},
	center: {
		alignItems: "center",
		justifyContent: "center",
	},
});

export default One;
